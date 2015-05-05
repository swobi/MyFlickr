package myflcikr;



import java.io.IOException;
import java.io.PrintWriter;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.cloud.sql.jdbc.Connection;

public class ConfigPhotoServlet extends HttpServlet {
	
    public void doPost(HttpServletRequest req, HttpServletResponse res)  throws IOException {
        // Get the image representation
        Connection con = null;
        PrintWriter out = res.getWriter();
        try {
        	
        	// get all the parameter from the previous ConfigPhoto.jsp
        	 String publicity=req.getParameter("publicity");
		     String pname=req.getParameter("pname");
		     String imageType=(String)req.getParameter("imageType");
		     int pid=Integer.parseInt((String)req.getParameter("pid"));
		     int uid=Integer.parseInt((String)req.getParameter("uid"));
		     String des=req.getParameter("description");
		    
		    // for debuging
		     System.out.println("here");
		     System.out.println(publicity);
		     System.out.println(des);
		     System.out.println(pname);
		     System.out.println(imageType);
		     System.out.println(pid);
		     System.out.println(uid);
		     System.out.println("leave");
		    
		     // create database connection
		     Timestamp time=new Timestamp(Calendar.getInstance().getTime().getTime());
		     System.out.println(time.toString());
			 DriverManager.registerDriver(new AppEngineDriver());
			 con = (Connection) DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
    		 // Insert new photo to Photo table
    		 String statement="INSERT INTO Photo (pid,imageType,pname,description,Publicity) VALUES ( ?, ?, ?, ?, ?)";
    		 PreparedStatement stmt=con.prepareStatement(statement);
    		 	stmt.setInt(1, pid);
    		    stmt.setString(2,imageType);
    		    stmt.setString(3, pname);
    		    stmt.setString(4, des);
    		    stmt.setString(5, publicity);
    		    int success = 2;
    		    success = stmt.executeUpdate();
    		    if(success == 1) 
    		    {
    		  // If inserting photo success, update the UserUpload table
    		        statement ="INSERT INTO UserUpload (uid, pid, upload_date) VALUES( ? , ?, ? )";
        		    stmt = con.prepareStatement(statement);
        		    stmt.setInt(1, uid); 
        		    stmt.setInt(2, pid);
        		    stmt.setTimestamp(3, time);
        		    success = 2;
        		    success = stmt.executeUpdate();
        	  // If success go to anthers serveImage page show the uploaded photo
        		    if(success == 1)
        		    	res.sendRedirect("/ServeImage.jsp?key="+pid);
        		    else if (success == 0) 
        		        out.println("<html><head></head><body>Failure! Please try again...</body></html>");
    		     } 
    		    else if (success == 0) 
    		        out.println("<html><head></head><body>Failure to insert Photo! Please try again...</body></html>");
    		      
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			
			if (con!=null)
				try 
				{
					con.close();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
   

  
    }
    protected void doGet(
	        HttpServletRequest request, HttpServletResponse response
	        ) throws ServletException, IOException {

	        // Pass through to the doPost method:
	        doPost(request, response);
	    }

}
