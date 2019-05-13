package iNuage;

import java.io.File;
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
import org.apache.commons.io.FileUtils;

import iNuage.Sql_id;

@WebServlet("/iNuage/Upload")
public class Upload extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.sendRedirect("iNuage");
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
    	int status = 0;
    	
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
        fileDir += "\\" + user;

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
                    
                	fileDir = fileDir.replace("\\", "\\\\");
                	
                	////////////////CHECK IF EQUALS ANY OTHER FILE ALREADY EXISTING 
                	
                	PreparedStatement ps = null;
                	String sql = "SELECT path FROM jenuage_docs";
                	ps = con.prepareStatement(sql);
                	ResultSet rs = ps.executeQuery();
                	
                	boolean isTwoEqual = false;
                	File newFile = new File(item.getName());
                	
                	if(newFile.length() != 0)
	                	while(rs.next()) 
	                	{
	                		isTwoEqual = FileUtils.contentEquals(new File(rs.getString("path")), newFile);
	                		
	                		response.getWriter().println(new File(rs.getString("path")).length());
	                		response.getWriter().println(newFile.length());
	                		
	                		if(isTwoEqual)
	                			throw new Exception("exi");
	                	} 
                	
                	//////////////////////
                	
                	PreparedStatement pst = con.prepareStatement("INSERT INTO `jenuage_docs` (`user`, `date`, `path`, `name`, `share`, `folder`) VALUES (" + user + ",\"" + dateFormat.format(date) + "\",\"" + fileDir + "\\\\" + newname + "\",\"" + fileName + "\",0,0 );");
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
		}
        
        response.sendRedirect(request.getContextPath() + "/iNuage?upload=" + status);
    }

}