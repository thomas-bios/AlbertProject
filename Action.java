package iNuage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import org.apache.commons.io.FileUtils;

import iNuage.Sql_id;

@WebServlet(name = "Action", urlPatterns = { "/iNuage/action" })
public class Action extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String action = request.getParameter("c");
		
		if(action.equals("down")) {
			downloadFile((int)session.getAttribute("user_id_int"),request.getParameter("fid"));
			response.sendRedirect(request.getContextPath() + "/iNuage");
		}
		
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
				removeFile((String)session.getAttribute("user_id_string"),Integer.parseInt(request.getParameter("fid")));
				response.sendRedirect(request.getContextPath() + "/iNuage?status=01");
				break;
			case("sha"):
				shareFile((String)session.getAttribute("user_id_string"),Integer.parseInt(request.getParameter("fid")),Integer.parseInt(request.getParameter("state")));
				response.sendRedirect(request.getContextPath() + "/iNuage?status=03");
				break;
			case("ren"):
				renameFile((String)session.getAttribute("user_id_string"),Integer.parseInt(request.getParameter("fid")),request.getParameter("newname"));
				response.sendRedirect(request.getContextPath() + "/iNuage?status=02");
				break;
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
	
	protected boolean downloadFile(int user_id, String file_id) {
		try {
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			String l = "select * from jenuage_docs where file_id = \"" + file_id + "\";";
			PreparedStatement pst = con.prepareStatement(l);
			ResultSet result = pst.executeQuery();
			
			if(result.next()) {
				if(result.getInt(5) == 1 || result.getInt(1)  == user_id) {
					FileUtils.copyURLToFile(new URL(result.getString(3)), 
							  new File(result.getString(4)), 10000, 10000);
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected boolean deleteAccount(String user_id,HttpServletRequest request) {
		try {		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			HttpSession session = request.getSession();
			
			String l = "delete from jenuage_docs where user = \"" + user_id + "\";";
			PreparedStatement pst = con.prepareStatement(l);
			pst.executeUpdate();
			
			l = "delete from jenuage_users where id = \"" + user_id + "\";";
			pst = con.prepareStatement(l);
			pst.executeUpdate();
			
			session.invalidate();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	protected void removeFile(String user_id, int file_id )	{
		try	{		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			
			String l = "delete from jenuage_docs where user = \"" + user_id + "\"and file_id = " + file_id + ";";
			PreparedStatement pst = con.prepareStatement(l);
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
}