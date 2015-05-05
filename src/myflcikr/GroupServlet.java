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

import com.google.appengine.api.rdbms.AppEngineDriver;

public class GroupServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String gid=req.getParameter("gid"); //request should include the group_id
		    //select group information
		    String group_sql="SELECT * FROM Groups Where gid='"+gid+"'";
		    //select photos in this group
		    String photo_sql="SELECT p.* FROM Photo p, (SELECT * FROM GroupPhoto WHERE gid='"+gid+"') gp WHERE p.pid = gp.pid";
		    ResultSet g_rs = c.createStatement().executeQuery(group_sql);
		    ResultSet p_rs = c.createStatement().executeQuery(photo_sql);
		    if(g_rs.next())
		    {
		    	//gather group information (name, admin, photos..) and go to the corresponding group page
		    	
		    	//resp.setHeader("Refresh","1;url=/Group.jsp");
		    }
		    else{
		    	out.println("<html><head></head><body><script>" +
	    			 	"alert('Group Not Found!')</script></body></html>");
		    	//resp.setHeader("Refresh","1;url=/.jsp"); //should go back to previous page
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
