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


@WebServlet("/iNuage/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String file_id = request.getParameter("fid");
		int user_id = null == session.getAttribute("user_id_int")? (int) session.getAttribute("user_id_int"): -1 ;
		try {
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			String l = "select * from jenuage_docs where file_id = \"" + file_id + "\";";
			PreparedStatement pst = con.prepareStatement(l);
			ResultSet result = pst.executeQuery();
			
			if(result.next()) {
				if(result.getInt(5) == 1 || (user_id != -1 && result.getInt(1)  == user_id)) {
					FileUtils.copyURLToFile(new URL(result.getString(3)), 
							  new File(result.getString(4)), 10000, 10000);
				}else {
					//request forward some jsp wrong rights
					return;
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		response.sendRedirect(request.getContextPath() + "/iNuage");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
