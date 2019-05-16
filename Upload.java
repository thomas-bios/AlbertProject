package iNuage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import iNuage.Sql_id;

@WebServlet("/iNuage/Upload")
public class Upload extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
    	doPost(request,response);
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
    	int status = 0;
    	String parent = "";
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    	Date date = new Date();
    	HttpSession session = request.getSession();
    	String user = (String) session.getAttribute("user_id_string");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute( "javax.servlet.context.tempdir" );
        
        factory.setRepository(repository);

        ServletFileUpload upload = new ServletFileUpload(factory);
        
        String fileDir = getServletContext().getRealPath("/WEB-INF/uploads");

        try
        {
            List<FileItem> items = upload.parseRequest(request);
            
            for(FileItem item : items)
            {
                if(!item.isFormField())
                {                	
                	Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
                	
                    String fileName = (new File( item.getName() )).getName();
                    if(fileName.isEmpty())
                    	throw new Exception("nof");
                    String newname = dateFormat.format(date) + fileName;
                    File file = new File( fileDir, newname );
                    item.write( file );
                  
                	////////////////CHECK IF EQUALS ANY OTHER FILE ALREADY EXISTING 
                	PreparedStatement ps = null;
                	String sql = "SELECT hash FROM jenuage_docs WHERE user = " + user;
                	ps = con.prepareStatement(sql);
                	ResultSet rs = ps.executeQuery();
                	
                	byte[] salt = "0".getBytes();
                	
                	FileInputStream fis = new FileInputStream(file);
                	byte[] data = new byte[(int) file.length()];
                	fis.read(data);
                	fis.close();

                	String content = new String(data, "UTF-8");
                	String hashed = Sql_id.hash(content, salt);

                	while(rs.next())
                		if(rs.getString("hash").equals(hashed))
                			throw new Exception("exi");
                	//////////////////////
                	
                	parent = request.getParameter("parent").isEmpty() ? "-1" : request.getParameter("parent");
                	                	
                	PreparedStatement pst = con.prepareStatement(
                			"INSERT INTO `jenuage_docs` (`user`, `date`, `path`, `name`, `share`, `folder`, `hash`, `parent_id`, `ext`) "
                			+ "VALUES (" + user + ",\"" + dateFormat.format(date) + "\",\"" + fileDir + "/" + newname + "\",\"" + fileName + "\",0,0,\"" + hashed + "\"," + parent + ",\"" + FilenameUtils.getExtension(fileName) + "\");");
                	pst.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
        	if(e.getMessage().equals("nof"))
        		status = 1;
        	else if(e.getMessage().equals("exi"))
        		status = 3;
        	else	
        	{
        		status = 2;
        	}
        	
        	response.getWriter().println(e.getMessage());
		}
        
        response.sendRedirect(request.getContextPath() + "/iNuage?upload=" + status + "&parent=" + parent);
    }

}