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

public class AddFriendServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
				
		PrintWriter out = resp.getWriter();
		Connection c = null;
		//String status = "";
		String referer = req.getHeader("Referer");
		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String uid = req.getParameter("uid");
		    String cid = req.getParameter("contact");
		    String sql1 = "INSERT INTO UserContact VALUE (?,?)";

		    PreparedStatement stmt = c.prepareStatement(sql1);	 
	        stmt.setString(1, uid);
	        stmt.setString(2, cid);
	        int success = 2;
	        success = stmt.executeUpdate();
	       
	        if(success != 1) {
	        	out.println("<html><head></head><body><script>" +
	    			 	"alert('Some error happened, please try again!')</script></body></html>");
	        	resp.setHeader("Refresh","1;url='" + referer + "'");
	        	//resp.setHeader("Refresh","1;url=/contact?uid="+cid);
	        }
	        
	        stmt.setString(1, cid);
	        stmt.setString(2, uid);
	        success = 2;
	        success = stmt.executeUpdate();
	       
	        if(success == 1) {
	        		
	        	resp.setHeader("Refresh","1;url='" + referer + "'");
	        	//resp.setHeader("Refresh","1;url=/contact?uid="+cid);
	        }
	        else
	        {
	        	out.println("<html><head></head><body><script>" +
	    			 	"alert('Some error happened, please try again!')</script></body></html>");
	        	resp.setHeader("Refresh","1;url='" + referer + "'");
	        	//resp.setHeader("Refresh","1;url=/contact?uid="+cid);
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
		
	}
}
