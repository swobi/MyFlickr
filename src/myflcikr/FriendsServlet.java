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

public class FriendsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String uid=req.getParameter("uid"); //request should include the user_id
		    
		    int page = 1, pagesize = 10;
		    if (req.getParameter("pg") != null)
		    	page = Integer.parseInt(req.getParameter("pg"));
		    if (req.getParameter("ps") != null)
		    	pagesize = Integer.parseInt(req.getParameter("ps"));
		    
		    String user_sql="SELECT * FROM Users WHERE uid='"+uid+"'";
		    String friend_sql="SELECT u.* FROM Users u, (SELECT * FROM UserContact WHERE uid='"+uid+"') uc WHERE u.uid = uc.contact ORDER BY u.uname ASC LIMIT "+(page-1)*pagesize+", "+pagesize+"";
		    String num_friend_sql="SELECT COUNT(*) FROM UserContact WHERE uid='"+uid+"'";
		    ResultSet u_rs = c.createStatement().executeQuery(user_sql);
		    ResultSet f_rs = c.createStatement().executeQuery(friend_sql);
		    ResultSet num_friend_rs = c.createStatement().executeQuery(num_friend_sql);
		    
		    ArrayList resultArray = new ArrayList();

		    if (!f_rs.next()) {
		    	
		    }
		    else {
		    	resultArray.add(f_rs.getInt(1));
		    	resultArray.add(f_rs.getString(2));
		    	resultArray.add(f_rs.getString(5));
		    	while(f_rs.next()) {
		    		resultArray.add(f_rs.getInt(1));
			    	resultArray.add(f_rs.getString(2));
			    	resultArray.add(f_rs.getString(5));
			    }
		    }
		    req.setAttribute("rs", resultArray);
		    
		    if (u_rs.next()) {
		    	req.setAttribute("id", Integer.toString(u_rs.getInt(1)));
			    req.setAttribute("uname", u_rs.getString(2));
		    }
		    else {
		    	req.setAttribute("id", Integer.toString(-1));
		    	req.setAttribute("uname", "");
		    }
		    
		    req.setAttribute("page", Integer.toString(page));
		    req.setAttribute("pagesize", Integer.toString(pagesize));
		    
		    if (num_friend_rs.next()) {
		    	req.setAttribute("num_friends", Integer.toString(num_friend_rs.getInt(1)));
		    }
		    else {
		    	req.setAttribute("num_friends", Integer.toString(-1));
		    }
		    
//		    com.foo.dbBean bean = new com.foo.dbBean();
//		    //call setters to initialize bean
//		    req.setAttribute("dbBean", bean);
		    
		    
		    
		    RequestDispatcher rd = req.getRequestDispatcher("/Friends.jsp");
	    	if (rd != null) {
	    		rd.forward(req, resp);
	    	}
		    
		    /*
		    out.println("<html><head></head><body>");
		    if(!rs.next())
		    {
		    	//pass all friends' names/descriptions to the Friends.jsp page
		    	out.println("User: " + uid + " has no friends.<br>");
		    	//resp.setHeader("Refresh","1;url=/Friend.jsp");
		    }
		    else{
		    	
		    	out.println("User: " + uid + " has friend <a href=\"/contact?user_id=" + rs.getInt(1) + "\">" + rs.getString(2) + "</a><br>");
		    	while(rs.next()) {
		    		out.println("User: " + uid + " has friend <a href=\"/contact?user_id=" + rs.getInt(1) + "\">" + rs.getString(2) + "<br>");
		    	}
		    	
		    	RequestDispatcher rd = req.getRequestDispatcher("/Friends.jsp");
		    	if (rd != null) {
		    		rd.forward(req, resp);
		    	}
		    }
		    
		    out.println("</body></html>");*/
		    
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
