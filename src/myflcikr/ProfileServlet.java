package myflcikr;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.rdbms.AppEngineDriver;

public class ProfileServlet extends HttpServlet {
	
	public HashMap<String,String> getInfo(String uid)
	{
		HashMap<String,String> results=new HashMap<String,String>();
		Connection c = null;

		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
			String sql="SELECT * FROM Users Where uid='"+uid+"'";
			ResultSet rs = c.createStatement().executeQuery(sql);

			if(rs.next())
			{
				results.put("id",(String)rs.getString("uid"));
				results.put("uname",(String)rs.getString("uname"));
				results.put("email",(String)rs.getString("email"));
				results.put("gender",(String)rs.getString("gender"));
				results.put("description",(String)rs.getString("description"));
				
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
		return results;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		String uid="-1";
		for(Cookie cookie:req.getCookies())
		{
			if(cookie.getName().equals("uid"))
			{
				uid=cookie.getValue();	
			}
		}
		if(uid=="-1")
		{
			out.println("<html><head></head><body><script>" +
				 	"alert('Please go back to log in!')</script></body></html>");
	    	resp.setHeader("Refresh","1;url=/Login.jsp");

		}
		else
		{
			HashMap<String,String> map=getInfo(uid);
			if(map.keySet()!=null)
			{
				req.setAttribute("uname", map.get("uname"));
				req.setAttribute("email", map.get("email"));
				req.setAttribute("gender", map.get("gender"));
				req.setAttribute("description", map.get("description"));
			
				RequestDispatcher dispatcher = req.getRequestDispatcher("/Profile.jsp");
				if (dispatcher != null){
					dispatcher.forward(req, resp);
				} 
			}
			else{ 
				out.println("<html><head></head><body><script>alert('Record does not exist, check email and password')</script></body></html>");
				resp.setHeader("Refresh","1;url=/Home.jsp");
			}
		}
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		String uid="-1";
		for(Cookie cookie:req.getCookies())
		{
			if(cookie.getName().equals("uid"))
			{
				uid=cookie.getValue();	
			}
		}
		if(uid=="-1")
		{
			out.println("<html><head></head><body><script>" +
				 	"alert('Please go back to log in!')</script></body></html>");
	    	resp.setHeader("Refresh","1;url=/Home.jsp");

		}
		else
		{
			try {
				String uname=req.getParameter("uname");
				String gender=req.getParameter("optionsRadios");
				String description=req.getParameter("description");
				DriverManager.registerDriver(new AppEngineDriver());
				c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
				String sql1="SELECT * FROM Users Where uname='"+uname+"'";
				ResultSet rs1 = c.createStatement().executeQuery(sql1);
		    	HashMap<String,String> map=getInfo(uid);
		    	req.setAttribute("uname", uname);
				req.setAttribute("email", map.get("email"));
				req.setAttribute("gender", gender);
				req.setAttribute("description", description);
				if(rs1.next() && !uname.equals(map.get("uname")))
				{
					String status="nameTaken";
			    	req.setAttribute("status", status);
			    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Profile.jsp");
					if (dispatcher != null){
						  dispatcher.forward(req, resp);
					} 

				}
				else
				{
					String statement ="Update Users set uname= ?, gender= ?, description= ? where uid='"+uid+"'";
			        PreparedStatement stmt = c.prepareStatement(statement);	 
			        stmt.setString(1, uname);
			        stmt.setString(2, gender);
			        stmt.setString(3, description);

			        int success = 2;
			        success = stmt.executeUpdate();
			        if(success == 1)
			        {
			        	String status="Success";
				    	req.setAttribute("status", status);
				    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Profile.jsp");
						if (dispatcher != null){
							  dispatcher.forward(req, resp);
						} 
			        }
			        else
			        {
			        	String status="SystemFail";
				    	req.setAttribute("status", status);
				    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Profile.jsp");
						if (dispatcher != null){
							  dispatcher.forward(req, resp);
						} 

			        }
			        /*
					String sql2="Update Users SET uname='"+uname+"'"+", gender='"+gender+", description='"+description+"'";
					ResultSet rs2 = c.createStatement().executeQuery(sql2);
					*/
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
}
