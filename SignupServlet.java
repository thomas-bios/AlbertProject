package iNuage;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import iNuage.Sql_id;

@WebServlet("/iNuage/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/iNuage?status=11");	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		    String uN = request.getParameter("userName");
			Connection con = DriverManager.getConnection(Sql_id.connection, Sql_id.user, Sql_id.password);
			
			//2 test 
			PreparedStatement pst = con.prepareStatement("select * from jenuage_users where user_name = \""+ uN + "\";");
			ResultSet result = pst.executeQuery();
			if(result.next()) {
				con.close();
				response.sendRedirect(request.getContextPath() + "/iNuage?status=21");	
				//response.sendRedirect(request.getContextPath() + "/home.jsp?status=21");	
				return;
			}
			String pass1 = request.getParameter("password");
			String pass2 = request.getParameter("password2");
			if(!pass1.equals(pass2)) {
				con.close();
				response.sendRedirect(request.getContextPath() + "/iNuage?status=22");	
				return;
			}
			//get new id
			Random rnd = new Random();
			int id;
			do {
				id = rnd.nextInt(999999);
				pst = con.prepareStatement("SELECT * FROM jenuage_users where id = \"" + id + "\";");
				result = pst.executeQuery();

			} while(result.next());

			//Add in the database
			String pwdHashed = getSaltedHash(pass1);
			pst = con.prepareStatement("INSERT INTO `jenuage_users` (`id`, `user_name`, `password`) VALUES (\"" + id
					+ "\",\"" + uN + "\",\"" + pwdHashed + "\");");
            pst.executeUpdate();
            
   
			if (!con.isClosed()) {
			      con.close();
			}
    		response.sendRedirect(request.getContextPath() + "/iNuage?status=0");	

		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/iNuage?status=3");	
		}	

	
	}

	
	//The following is not from us; from https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java

	
	private static final int iterations = 2000;
    private static final int saltLen = 16;
    private static final int desiredKeyLen = 128;

    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
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