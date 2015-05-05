package myflcikr;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class UserGroupsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String uid=req.getParameter("uid"); //request should include the uid
		    String user_sql ="SELECT * FROM Users WHERE uid='"+uid+"'";
		    String group_sql="SELECT g.* FROM Groups g, (SELECT * FROM Member WHERE uid='"+uid+"') m WHERE g.gid = m.gid";
		    ResultSet u_rs = c.createStatement().executeQuery(user_sql);
		    ResultSet g_rs = c.createStatement().executeQuery(group_sql);
		    
		    ArrayList resultArray = new ArrayList();

		    if (!g_rs.next()) {
		    	
		    }
		    else {
		    	resultArray.add(g_rs.getInt(1));
		    	resultArray.add(g_rs.getString(3));
		    	resultArray.add(g_rs.getString(4));
		    	while(g_rs.next()) {
		    		resultArray.add(g_rs.getInt(1));
			    	resultArray.add(g_rs.getString(3));
			    	resultArray.add(g_rs.getString(4));
			    }
		    }
		    req.setAttribute("rs", resultArray);
		    
		    if (u_rs.next()) {
		    	req.setAttribute("id", u_rs.getInt(1));
			    req.setAttribute("uname", u_rs.getString(2));
		    }
		    else {
		    	req.setAttribute("id", -1);
		    	req.setAttribute("uname", "");
		    }
		    
		    RequestDispatcher rd = req.getRequestDispatcher("/UserGroups.jsp");
	    	if (rd != null) {
	    		rd.forward(req, resp);
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
