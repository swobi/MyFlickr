package myflcikr;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.rdbms.AppEngineDriver;
//import com.google.appengine.api.datastore.Query;

public class DeleteServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PersistenceManager pm=null;
		String uid = req.getParameter("uid");
		String referer = req.getHeader("Referer");
		Transaction txn = (Transaction) datastore.beginTransaction();
		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String pid = req.getParameter("pid");
		  
		    //select contact information
		    String delete_photo_sql1="DELETE FROM Photo Where pid='"+pid+"'";
		    String delete_photo_sql2="DELETE FROM UserUpload WHERE pid = '" +pid + "'";
		    String delete_photo_sql3="DELETE FROM CommentPhoto WHERE pid = '" +pid + "'";
		    String delete_photo_sql4="DELETE FROM UserFavorite WHERE pid = '" +pid + "'";
		    
		    int success = 2;
		    
		    if (referer.startsWith("http://kaomyflickr.appspot.com/contact") || referer.startsWith("http://kaomyflickr.appspot.com/ServeImage")) {
		    	
		    	//delete from Photo, UserUpload, CommentPhoto
		    	PreparedStatement stmt = c.prepareStatement(delete_photo_sql1);
			    success = stmt.executeUpdate();
			    stmt = c.prepareStatement(delete_photo_sql2);
			    success = stmt.executeUpdate();
			    stmt = c.prepareStatement(delete_photo_sql3);
			    success = stmt.executeUpdate();
			    
			    //delete photo in datastore
			    pm = PMF.get().getPersistenceManager();
		        Query query = pm.newQuery(Photo.class,"id==pid");
		        query.declareParameters("int pid");
		        query.setRange(0, 1);
		        List<Photo> results = (List<Photo>)query.execute(Integer.parseInt(pid));
		        //Key key = KeyFactory.createKey("Photo", pid);
		        for (Photo p : results) {
		        	datastore.delete(p.getKey());
		        }
		        
//		        query = pm.newQuery("Comment", photoCommentKey);
//		       // query.declareParameters(photoCommentKey);
//		        query.setRange(0, 1);
//		        Iterator<Entity> iter = datastore.prepare(query).asIterator();
		        
		        //delete comments in datastore
		        Key photoCommentKey = KeyFactory.createKey("PhotoComments", pid);
		        com.google.appengine.api.datastore.Query query2 = new com.google.appengine.api.datastore.Query("Comment", photoCommentKey);
	        	Iterator<Entity> iter = datastore.prepare(query2).asIterator();
	        	Entity current=null;
	        	Key currentKey=null;
	        	while(iter.hasNext())
	        	{
	        		current=iter.next();
	        		currentKey=current.getKey();
	        		datastore.delete(currentKey);
	        	}
	        	txn.commit();
		        
			    
			    if(success == 1 || success == 0) {
			    	if (referer.startsWith("http://kaomyflickr.appspot.com/contact")) {
			    		resp.setHeader("Refresh","1;url='" + referer + "'");
			    	}
			    	else if (referer.startsWith("http://kaomyflickr.appspot.com/ServeImage")) {
			    		//if delete request is from photo page, return to the user's photostream
			    		resp.setHeader("Refresh","1;url=/contact?uid=" + uid + "&cid=" + uid);
			    	}
		        }
		        else
		        {
		        	out.println("<html><head></head><body><script>" +
		    			 	"alert('Some error happened, please try again!')</script></body></html>");
		        	resp.setHeader("Refresh","1;url='" + referer + "'");
		        	//resp.setHeader("Refresh","1;url=/contact?uid="+cid);
		        }
		        
		    }
		    else if (referer.startsWith("http://kaomyflickr.appspot.com/favorites")) {
		    	//delete in UserFavorite
		    	PreparedStatement stmt = c.prepareStatement(delete_photo_sql4);
			    success = stmt.executeUpdate();
			    
			    if(success == 1 || success == 0) {
			    	resp.setHeader("Refresh","1;url='" + referer + "'");
		        }
		        else
		        {
		        	out.println("<html><head></head><body><script>" +
		    			 	"alert('Some error happened, please try again!')</script></body></html>");
		        	resp.setHeader("Refresh","1;url='" + referer + "'");
		        }
		    }
	       
	        
	        
	        //Entity photo = datastore.get(key);
	        
	        
	        //datastore.delete(key);
		    
		}catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (c != null) 
	            try {
	              c.close();
	              } catch (SQLException ignore) {
	            }
	        if (txn.isActive()) {
		        txn.rollback();
		    }
	        }
	}

}
