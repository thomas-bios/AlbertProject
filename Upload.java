package iNuage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

@WebServlet("/Upload")
public class Upload extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request,
        HttpServletResponse response ) throws ServletException, IOException
    {
        // Redirect the user to the upload form
        response.sendRedirect( "Upload.html" );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
    	int status = 0;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    	Date date = new Date();
    	HttpSession session = request.getSession();
    	String user = (String) session.getAttribute("user_id_string");
    	
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        
        File repository = (File) servletContext
            .getAttribute( "javax.servlet.context.tempdir" );
        
        factory.setRepository( repository );

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload( factory );
        
        // The directory we want to save the uploaded files to.
        String fileDir = getServletContext().getRealPath( "/WEB-INF/uploads" );
        fileDir += "\\" + user;

        // Parse the request
        try
        {
            List<FileItem> items = upload.parseRequest( request );
            
            for( FileItem item : items )
            {
                // If the item is not a form field - meaning it's an uploaded
                // file, we save it to the target dir
                if( !item.isFormField() )
                {
                    // item.getName() will return the full path of the uploaded
                    // file, e.g. "C:/My Documents/files/test.txt", but we only
                    // want the file name part, which is why we first create a
                    // File object, then use File.getName() to get the file
                    // name.
                	// /var/usr/some/temp/dir/some-file.jpg
                	// /user/albert/3220/WEB-INF/uploads   some-file.jpg
                	
                	
                	Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
                	
                    String fileName = (new File( item.getName() )).getName();
                    if(fileName.isEmpty())
                    	throw new Exception("nof");
                    String newname = dateFormat.format(date) + fileName;
                    File file = new File( fileDir, newname );
                    item.write( file );
                    
                	fileDir = fileDir.replace("\\", "\\\\");
                	
                	PreparedStatement pst = con.prepareStatement("INSERT INTO `jenuage_docs` (`user`, `date`, `path`, `name`, `share`, `folder`) VALUES (" + user + ",\"" + dateFormat.format(date) + "\",\"" + fileDir + "\\\\" + newname + "\",\"" + fileName + "\",0,0 );");
                	pst.executeUpdate();
                } 
            }
        }
        catch (Exception e)
        {
        	if(e.getMessage().equals("nof"))
        		status = 1;
        	else
        		status = 2;
		}	

        /*response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        out.println( "<html><head><title>Upload</title></head><body>" );
        out.println( "<p>" + count + " file(s) uploaded to " + fileDir );
        out.println( "</body></html>" );*/
        
        response.sendRedirect(request.getContextPath() + "/iNuage?upload=" + status);
    }

}
