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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;


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
				if(result.getInt(5) == 1 || (user_id != -1 && result.getInt(1)  == user_id)) {
					
					//DONT WORK IN LOCAL : ONLY WITH HTTP://FILE
					
					//if in serv
					//String url = result.getString(3).replace("\\","/");
					//if in local : stupid file for testing
					String url = "https://www.j2eebrain.com/wp-content/uploads/c11e504954111c54b467c154d4abc2d3.png";
					String newpath = "/home/" + result.getString(4);

					//dialog box for choosing the new path
					JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					jfc.setDialogTitle("Choose a directory to save your file: ");
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					int returnValue = jfc.showSaveDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile().isDirectory()) {
						newpath = jfc.getSelectedFile().getAbsolutePath();
					} else {
						//error choosing the path
						throw new Exception();
					}
					
					newpath += "/" + result.getString(4);
					
					//downloading 
					FileUtils.copyURLToFile(new URL(url), 
							  new File(newpath), 10000, 10000);
					
					response.sendRedirect(request.getContextPath() + "/iNuage?path="+newpath);
					return;
				}else {
					request.getRequestDispatcher("unauthorized.jsp").forward(request, response);
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
