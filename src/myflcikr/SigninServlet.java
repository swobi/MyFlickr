package myflcikr;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class SigninServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		
		 UserService userService = UserServiceFactory.getUserService();
		 User user = userService.getCurrentUser();
		 PrintWriter out = resp.getWriter();
		 Connection c = null;
	     int userid=-1;
		 if (user != null) {
			 String email=user.getEmail();
			 try {
					DriverManager.registerDriver(new AppEngineDriver());
				    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
				    String sql="SELECT * FROM Users Where email='"+email+"'";
				    ResultSet rs = c.createStatement().executeQuery(sql);
				    if(rs.next())
				    {
				    	userid=rs.getInt("uid");
				    	Cookie cook=new Cookie("uid",Integer.toString(userid));
				    	resp.addCookie(cook);		//send it to the browser
				    	resp.setHeader("Refresh","1;url=/Home.jsp");
				    }
				    else{ 
				    	//System.out.println("Enter here");
				    	String status="NewUser";
				    	req.setAttribute("status", status);
				    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Signup.jsp");
						if (dispatcher != null){
							  dispatcher.forward(req, resp);
					    	 // System.out.println("Forward request");

						}
				    }
				    
				}catch (SQLException e) {
			        e.printStackTrace();
			    }finally {
			        if (c != null) 
			            try {
			              c.close();
			              } catch (SQLException ignore) {
			            }
			        }
			 
	     } else {
	         resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	     }
		
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		
		
	}

}
