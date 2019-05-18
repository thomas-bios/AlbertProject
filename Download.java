package iNuage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebServlet("/iNuage/Download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String file_id = request.getParameter("fid");
		int user_id = null != session.getAttribute("user_id_int")? (int) session.getAttribute("user_id_int"): -1 ;
		
		try {
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			String l = "select * from jenuage_docs where file_id = \"" + file_id + "\";";
			PreparedStatement pst = con.prepareStatement(l);
			ResultSet result = pst.executeQuery();
			
			
			if(result.next()) {
				if(result.getInt("share") == 1 || (user_id != -1 && result.getInt("user")  == user_id)) {
										
					//if in serv
					String url = result.getString("path").replace("\\","/");
					String fileServName = url.substring(url.lastIndexOf("/") + 1).trim();
					String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/uploads/");
			        Path file = Paths.get(dataDirectory, fileServName);
		            
			        String resultName;
			        if(result.getString("name").indexOf(".") != -1)
			        	resultName = result.getString("name").substring(0,result.getString("name").indexOf(".")+1) + result.getString("ext");
			        else
			        	resultName = result.getString("name") + "." + result.getString("ext");

		         // obtains ServletContext
		            ServletContext context = getServletContext();
		             
		            // gets MIME type of the file
		            String mimeType = context.getMimeType(resultName);
		            response.setContentType(mimeType);

		         // force download
		            response.setHeader("Content-Disposition", "attachment; filename=" + resultName);
		            // obtains response's output stream
		            
		            Files.copy(file, response.getOutputStream());
	                response.getOutputStream().flush();
		             

		            
		    		//response.getWriter().append(TEST);

					//response.sendRedirect(request.getContextPath() + "/iNuage?path=");
					return;
				} else {
					request.getRequestDispatcher("unauthorized.jsp").forward(request, response);
					return;
				}
			}else {
				response.sendRedirect(request.getContextPath() + "/iNuage?status=32");
				return;
			}
		
		}catch(Exception e) {
			response.sendRedirect(request.getContextPath() + "/iNuage?error=" + e.getMessage());
		}
		//response.sendRedirect(request.getContextPath() + "/iNuage");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}