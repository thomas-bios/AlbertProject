package iNuage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        
        File repository = (File) servletContext
            .getAttribute( "javax.servlet.context.tempdir" );
        
        factory.setRepository( repository );

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload( factory );

        // Count how many files are uploaded
        int count = 0;
        
        // The directory we want to save the uploaded files to.
        String fileDir = getServletContext().getRealPath( "/WEB-INF/uploads" );

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
                	
                    String fileName = (new File( item.getName() )).getName();
                    File file = new File( fileDir, fileName );
                    item.write( file );
                    ++count; 
                    
                    Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
                	HttpSession session = request.getSession();
                	String user = (String) session.getAttribute("user_id_string");
                	
                	
                	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                	LocalDate localDate = LocalDate.now();
                	fileDir = fileDir.replace("\\", "\\\\");
                	
                	PrintWriter out = response.getWriter();
                	out.print("INSERT INTO 'jenuage_docs' VALUES (" + user + ",\""+ dtf.format(localDate) + "\",\"" + fileDir + "\",\"" + fileName + "\",\"0\",\"0\" );");
                	
                	PreparedStatement pst = con.prepareStatement("INSERT INTO `jenuage_docs` (`user`, `date`, `path`, `name`, `share`, `folder`) VALUES (" + user + ",\""+ dtf.format(localDate) + "\",\"" + fileDir + "\",\"" + fileName + "\",0,0 );");
                	pst.executeUpdate();
                } 
            }
        }
        catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/iNuage?uploaded=0");	
		}	

        /*response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        out.println( "<html><head><title>Upload</title></head><body>" );
        out.println( "<p>" + count + " file(s) uploaded to " + fileDir );
        out.println( "</body></html>" );*/
        
        response.sendRedirect(request.getContextPath() + "/iNuage?uploaded=1");
    }

}
