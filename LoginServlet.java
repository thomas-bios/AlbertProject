package iNuage;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import iNuage.Sql_id;

@WebServlet("/iNuage/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/iNuage?status=11");	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
			String uN = request.getParameter("userName");
			String pass = request.getParameter("password");

			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);

			//get user's info
			PreparedStatement pst = con.prepareStatement("select * from jenuage_users where user_name = \""+ uN + "\";");
			ResultSet result = pst.executeQuery();
			if(!result.next()) {
				//user do not exist
				con.close();
				response.sendRedirect(request.getContextPath() + "/iNuage?status=11");	
				return;
			}		
			int u_id = result.getInt("id");
			String u_hashedPassword = result.getString("password");
			
			//Password test 
			boolean isPasswordCorrect = check(pass,u_hashedPassword);
			
			if(!isPasswordCorrect) {
				//password not correct
				con.close();
				response.sendRedirect(request.getContextPath() + "/iNuage?status=11");
				return;
			}
			//password correct : go to user interface
			//add cookie if remember me check (soon ...)
			
			HttpSession session = request.getSession();
			session.setAttribute("user_id_int", u_id);
			session.setAttribute("user_id_string", String.valueOf(u_id));
			session.setAttribute("user_name_string", uN);

			//request.setAttribute("user_id", u_id);
			//request.getRequestDispatcher("/iNuage").forward(request, response);
			response.sendRedirect(request.getContextPath() + "/iNuage");	

			con.close();
	    } catch ( Exception e) {e.printStackTrace();}

	}
	
	
	//The following is not from us; from https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
	
	private static final int iterations = 2000;
    private static final int desiredKeyLen = 128;
	public static boolean check(String password, String stored) throws Exception{
    String[] saltAndHash = stored.split("\\$");
    if (saltAndHash.length != 2) {
        throw new IllegalStateException(
            "The stored password must have the form 'salt$hash'");
    }
    String hashOfInput = hash(password, Base64.getDecoder().decode(saltAndHash[0]));
    return hashOfInput.equals(saltAndHash[1]);
    
    
	}
	private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen));
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}