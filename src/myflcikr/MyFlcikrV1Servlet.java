package myflcikr;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.rdbms.AppEngineDriver;
import java.io.IOException;
import java.sql.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class MyFlcikrV1Servlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String head="<!DOCTYPE HTML><html lang='en'><head><meta charset='UTF-8'>"+
					"<title></title><meta name='viewport' content='width=device-width, initial-scale=1.0'>"+
					"<link href='/css/bootstrap-responsive.css' rel='stylesheet'>"+
					"<link href='/css/bootstrap.min.css' rel='stylesheet' media='screen'></head>"+
					"<body><script src='http://code.jquery.com/jquery-latest.js'></script>"+
					"<script src='/js/bootstrap.min.js'></script>";
		String tail="</body></html>";
		
		
		PrintWriter out = resp.getWriter();
		Connection c = null;
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		String status="";
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String userName=req.getParameter("inputUserName");
		    String email=user.getEmail();
		    String sql1="SELECT * FROM Users Where uname='"+userName+"'";
		    String sql2="SELECT * FROM Users Where email='"+email+"'";

		    ResultSet rs1 = c.createStatement().executeQuery(sql1);
		    ResultSet rs2 = c.createStatement().executeQuery(sql2);

		    if(rs1.next())
		    {
		    
		    	status="userNameTaken";
		    	req.setAttribute("status", status);
		    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Signup.jsp");
				if (dispatcher != null){
					  dispatcher.forward(req, resp);
				} 

		    }
		    else if(rs2.next())
		    {
		    	 out.println("<html><head></head><body><script>" +
		    			 	"alert('There is one record of this gmail account.')</script></body></html>");
		         resp.setHeader("Refresh","1;url=/signin");

		    }
		    else{ 
		    	String statement ="INSERT INTO Users (uname,email) VALUES( ? , ? )";
		        PreparedStatement stmt = c.prepareStatement(statement);	 
		        stmt.setString(1, userName);
		        stmt.setString(2, email);
		        int success = 2;
		        success = stmt.executeUpdate();
		       
		        if(success == 1) {
		        		
		        	resp.setHeader("Refresh","1;url=/signin");
		        } 
		        else if (success == 0) {
		        	status="SystemFail";
			    	req.setAttribute("status", status);
			    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Signup.jsp");
					if (dispatcher != null){
						  dispatcher.forward(req, resp);
					} 

		        }
		        else
		        {
		        	out.println("<html><head></head><body><script>" +
		    			 	"alert('Sql mistake')</script></body></html>");
			    	 resp.setHeader("Refresh","1;url=Signup.jsp");

		        }
		    }
		    
		}catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	        if (c != null) 
	            try {
	              c.close();
	              } catch (SQLException ignore) {
	            }
	        } 
	}
}
