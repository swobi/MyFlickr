package myflcikr;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.cloud.sql.jdbc.Connection;

import javax.jdo.PersistenceManager;

public class UploadServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(UploadServlet.class.getName());
    public void doPost(HttpServletRequest req, HttpServletResponse res)  throws IOException {
        // Get the image upload
        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator iter=null;
        PersistenceManager pm=null;
        Connection con = null;
		PrintWriter out = res.getWriter();
    	String uid="-1";
    	for(Cookie c:req.getCookies())
    	{
    		if(c.getName().equals("uid"))
    		{
    			uid=c.getValue();	
    			break;
    		}
    	}
    	if(uid=="-1")
    	{
    		out.println("<html><head></head><body><script>" +
    			 	"alert('Please go back to log in!')</script></body></html>");
        	res.sendRedirect("Login.jsp");

    	}  	
        
		try {
			 // get the upload ImageStream
			 iter = upload.getItemIterator(req);
		     FileItemStream imageItem = iter.next();
		     InputStream imgStream = imageItem.openStream();
		     String pname=imageItem.getName();
		     String imageType=imageItem.getContentType();
		     byte[] imageByte=IOUtils.toByteArray(imgStream);
		     Date time=new Date();
		     // get the user name
		     DriverManager.registerDriver(new AppEngineDriver());
    		 con = (Connection) DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
    		 String sql="SELECT uname FROM Users WHERE uid='"+uid+"'";
			 ResultSet rs = con.createStatement().executeQuery(sql);
			 String uname=null;
			    if(rs.next())
			    	{
			    		uname=rs.getString("uname");
			    		System.out.println("User name is "+uname);
			    	}
			    else{ 
			    	System.out.println("Enter here");
			    	String status="NewUser";
			    	req.setAttribute("status", status);
			    	RequestDispatcher dispatcher = req.getRequestDispatcher("/Signup.jsp");
					if (dispatcher != null){
						  dispatcher.forward(req, res);
				    	  System.out.println("Forward request");

					}
			    }
    		 // construct our entity objects
		     Photo image = new Photo(pname,imageByte,imageType,uname,time);
		     
		     // persist image
		     pm = PMF.get().getPersistenceManager();
		     pm.makePersistent(image);
		     
		     
		     // get the pid of the upload image
		     int pid=image.getqueryId();
		     
		     //add the new upload photo to memcache
		      Cache cache;
		      try {
		        	 CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		             cache = cacheFactory.createCache(Collections.emptyMap());
		             cache.put(pid, imageByte);
		             
		        } catch (CacheException e) {
		        	log.info("Failed to put item to cache!");
		        	System.out.println("Failed to put item to cache!");
		        	e.printStackTrace();
		        }
		      
		        // pass the pid, imageType and imageName to ConfigPhoto.jsp to save data in SQL  
		        req.setAttribute("pid", pid);
		        req.setAttribute("imageType", imageType);
		        req.setAttribute("pname", pname);
		        ServletContext context= getServletContext();
		        RequestDispatcher rd= context.getRequestDispatcher("/ConfigPhoto.jsp");
		       
		        try {
					rd.forward(req, res);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		

		        // respond to query
				// res.setContentType("text/plain");
		        // res.getOutputStream().write("OK!".getBytes());
		        // res.sendRedirect("/config?pid=" + pid+",");
		}
		 catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			// close the persistance manager
			if (pm!=null)
				pm.close();
			if(con!=null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
   

  
    }

}
