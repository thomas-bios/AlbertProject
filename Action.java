package iNuage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import iNuage.Sql_id;

@WebServlet(name = "Action", urlPatterns = { "/iNuage/action" })
public class Action extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String action = request.getParameter("c");

		if(session.getAttribute("user_id_int") == null) {
			response.sendRedirect(request.getContextPath() + "/iNuage");
			return;
		}
		switch(action) {
			case("logout"):
				logout(request);
				response.sendRedirect(request.getContextPath() + "/iNuage?status=4");
				break;
			case("delete"):
				deleteAccount((String)session.getAttribute("user_id_string"),request);
				response.sendRedirect(request.getContextPath() + "/iNuage?status=5");
				break;
			case("del"):
				remove((String)session.getAttribute("user_id_string"),Integer.parseInt(request.getParameter("fid")));
				response.sendRedirect(request.getContextPath() + "/iNuage?status=01&parent=" + request.getParameter("parent"));
				break;
			case("sha"):
				shareFile((String)session.getAttribute("user_id_string"),Integer.parseInt(request.getParameter("fid")),Integer.parseInt(request.getParameter("state")));
				response.sendRedirect(request.getContextPath() + "/iNuage?status=69&parent=" + request.getParameter("parent"));
				break;
			case("ren"):
				renameFile((String)session.getAttribute("user_id_string"),Integer.parseInt(request.getParameter("fid")),request.getParameter("newname"));
				response.sendRedirect(request.getContextPath() + "/iNuage?status=02&parent=" + request.getParameter("parent"));
				break;
			case("folder"):
				if(createFolder(request, response))
					response.sendRedirect(request.getContextPath() + "/iNuage?status=04&parent=" + request.getParameter("parent"));
				else
					response.sendRedirect(request.getContextPath() + "/iNuage?status=31&parent=" + request.getParameter("parent"));
				break;
			default:
				response.sendRedirect(request.getContextPath() + "/iNuage");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected boolean logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return true;
	}
	
	protected void deleteAccount(String user_id,HttpServletRequest request) {
		try {		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			HttpSession session = request.getSession();
			
			remove(user_id, -1);
			String l = "delete from jenuage_docs where user = \"" + user_id + "\";";
			PreparedStatement pst = con.prepareStatement(l);
			pst.executeUpdate();
			
			l = "delete from jenuage_users where id = \"" + user_id + "\";";
			pst = con.prepareStatement(l);
			pst.executeUpdate();
			
			session.invalidate();
		} catch(Exception e) {}
	}
	
	protected void remove(String user_id, int file_id)
	{
		try	{		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			PreparedStatement pst = con.prepareStatement("select * from jenuage_docs where user = \"" + user_id + "\" and parent_id = " + file_id + ";");
			ResultSet result = pst.executeQuery();
			while(result.next())
			{
				if(result.getInt("folder") == 1)
					remove(user_id,result.getInt("file_id"));
				else
				{
					pst = con.prepareStatement("delete from jenuage_docs where file_id =" + result.getInt("file_id") + ";");
					pst.executeUpdate();
				}
			}
			String l = "delete from jenuage_docs where user = \"" + user_id + "\"and file_id = " + file_id + ";";
			pst = con.prepareStatement(l);
			pst.executeUpdate();
			
		} catch(Exception e) {}
	}
	
	protected void renameFile(String user_id, int file_id, String newname){
		try	{		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			
			String l = "update jenuage_docs set name=\"" + newname + "\" where user = \"" + user_id + "\"and file_id = " + file_id + ";";
			PreparedStatement pst = con.prepareStatement(l);
			pst.executeUpdate();
			
		} catch(Exception e) {}
	}
	
	protected void shareFile(String user_id, int file_id, int state){
		try	{		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			String l;
			if(state == 1)
				l = "update jenuage_docs set share=0 where user = \"" + user_id + "\"and file_id = " + file_id + ";";
			else
				l = "update jenuage_docs set share=1 where user = \"" + user_id + "\"and file_id = " + file_id + ";";
			PreparedStatement pst = con.prepareStatement(l);
			pst.executeUpdate();
			
		} catch(Exception e) {}
	}
	protected boolean createFolder(HttpServletRequest request, HttpServletResponse response){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    	Date date = new Date();
    	HttpSession session = request.getSession();
    	String user = (String) session.getAttribute("user_id_string");
    	Connection con;
		try {
			con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
	    	String dir = request.getParameter("dir");
	    	String parent = request.getParameter("parent");

	    	PreparedStatement ps = null;
	    	String sql = "SELECT hash FROM jenuage_docs WHERE user = " + user;
        	ps = con.prepareStatement(sql);
        	ResultSet rs = ps.executeQuery();

			byte[] salt = "0".getBytes();
			String hashed = Sql_id.hash(dir, salt);

			while(rs.next())
        		if(rs.getString("hash").equals(hashed))
        			return false;

			PreparedStatement pst = con.prepareStatement("INSERT INTO `jenuage_docs` (`user`, `date`, `path`, `name`, `share`, `folder`, `hash`, `parent_id`) "
				+ "VALUES (" + user + ",\"" + dateFormat.format(date) + "\",\"\",\"" + dir + "\",0,1,\"" + hashed + "\"," + parent + ");");
			pst.executeUpdate();
		} catch (Exception e) {}
		return true;
    }
}