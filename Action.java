package iNuage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import iNuage.Sql_id;

@WebServlet(name = "Actions", urlPatterns = { "/iNuage/action" })
public class Action extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String action = request.getParameter("c");
		
		switch(action) {
			case("logout"):
				logout(request);
				response.sendRedirect(request.getContextPath() + "/iNuage?status=4");
				break;
			case("delete"):
				deleteAccount((String)session.getAttribute("user_id_string"),request);
				response.sendRedirect(request.getContextPath() + "/iNuage?status=5");
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
	
	protected boolean deleteAccount(String user_id,HttpServletRequest request) {
		try {		
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			HttpSession session = request.getSession();
			
			String l = "delete from jenuage_docs where user_id = \"" + user_id + "\";";
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
}
