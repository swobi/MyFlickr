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

public class ContactServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
		PrintWriter out = resp.getWriter();
		Connection c = null;
		
		try {
			DriverManager.registerDriver(new AppEngineDriver());
		    c = DriverManager.getConnection("jdbc:google:rdbms://cphatnyu:myflcikrdatabase/MyFlickr");
		    String uid = req.getParameter("uid");
		    String cid = req.getParameter("cid");

		    int publicity = -1;
		    if (!uid.equals(cid)) {
		    	publicity = 0;
		    }
		    
		    int page = 1, pagesize = 4;
		    if (req.getParameter("pg") != null)
		    	page = Integer.parseInt(req.getParameter("pg"));
		    if (req.getParameter("ps") != null)
		    	pagesize = Integer.parseInt(req.getParameter("ps"));
		    
		    //select contact information
		    String contact_sql="SELECT * FROM Users Where uid='"+cid+"'";
		    String friend_sql="SELECT contact FROM UserContact WHERE uid='"+cid+"'";
		    
		    //select photos uploaded and favored by this contact
		    String photo_sql1="SELECT p.*, up.upload_date FROM Photo p, (SELECT * FROM UserUpload WHERE uid='"+cid+"') up WHERE p.pid = up.pid ORDER BY up.upload_date DESC LIMIT "+(page-1)*pagesize+", "+pagesize+"";
		    String fav_sql="SELECT p.*, uf.fav_date FROM Photo p, (SELECT * FROM UserFavorite WHERE uid='"+cid+"') uf WHERE p.pid = uf.pid ORDER BY uf.fav_date DESC";

		  //select total photo number
		    String photo_sql2 = "SELECT COUNT(*) FROM UserUpload WHERE uid='" + cid + "'";

		    //if it's not the same user accessing the contact page, only return public photo information
		    if (publicity == 0) {
		    	//public photos
		    	photo_sql1="SELECT p.*, up.upload_date FROM Photo p, (SELECT * FROM UserUpload WHERE uid='"+cid+"') up WHERE p.pid = up.pid && p.Publicity = 0 ORDER BY up.upload_date DESC LIMIT "+(page-1)*pagesize+", "+pagesize+"";
		    	//number of public photos
		    	photo_sql2 = "SELECT COUNT(*) FROM Photo p, (SELECT * FROM UserUpload WHERE uid = '"+cid+"') up WHERE p.pid = up.pid && p.Publicity = 0";
		    }

		    		    		    
		    ResultSet c_rs = c.createStatement().executeQuery(contact_sql);
		    ResultSet p_rs = c.createStatement().executeQuery(photo_sql1);
		    ResultSet f_rs = c.createStatement().executeQuery(fav_sql);
		    ResultSet friend_rs = c.createStatement().executeQuery(friend_sql);
		    ResultSet num_photo_rs = c.createStatement().executeQuery(photo_sql2);
		    
		    ArrayList contact = new ArrayList();
		    ArrayList con_friend = new ArrayList();
		    ArrayList photoList = new ArrayList();
		    ArrayList favList = new ArrayList();
		    String num_photo="";
		    
		    if (!c_rs.next()) {
		    	//there's no contact with this uid
		    }
		    else { 
		    	contact.add(c_rs.getObject(1)); //uid
		    	contact.add(c_rs.getObject(2)); //uname
		    	contact.add(c_rs.getObject(3)); //email
		    	contact.add(c_rs.getObject(4)); //gender
		    	contact.add(c_rs.getObject(5)); //description
		    }
		    req.setAttribute("contact", contact);
		    
		    if (!friend_rs.next()) {
		    	//this contact has not added any friends
		    }
		    else {
		    	//add each friend's id into the list
		    	con_friend.add(friend_rs.getObject(1));
		    	while (friend_rs.next())
		    		con_friend.add(friend_rs.getObject(1));
		    }
		    req.setAttribute("con_friend", con_friend);
		    
		    if (!p_rs.next()) {
		    	
		    }
		    else {
		    	//get the number of comments of this photo
		    	String num_comment_sql = "SELECT COUNT(*) FROM CommentPhoto WHERE pid='"+p_rs.getInt(1)+"'";
		    	ResultSet num_comment = c.createStatement().executeQuery(num_comment_sql);
		    	num_comment.next();
		    	photoList.add(p_rs.getInt(1)); //add pid into list
		    	photoList.add(p_rs.getString(2)); //add image type into list
		    	photoList.add(p_rs.getString(3)); //add pname into list
		    	photoList.add(p_rs.getInt(4)); //add likes into list
		    	photoList.add(p_rs.getInt(5)); //add dislikes into list
		    	photoList.add(p_rs.getString(6)); //add description into list
		    	photoList.add(p_rs.getInt(7)); //add publicity into list
		    	photoList.add(p_rs.getString(8)); //add upload_date into list
		    	photoList.add(num_comment.getInt(1)); // number of comments
		    	while (p_rs.next()) {
		    		num_comment_sql = "SELECT COUNT(*) FROM CommentPhoto WHERE pid='"+p_rs.getInt(1)+"'";
		    		num_comment = c.createStatement().executeQuery(num_comment_sql);
		    		num_comment.next();
		    		photoList.add(p_rs.getInt(1)); //add pid into list
			    	photoList.add(p_rs.getString(2)); //add image type into list
			    	photoList.add(p_rs.getString(3)); //add pname into list
			    	photoList.add(p_rs.getInt(4)); //add likes into list
			    	photoList.add(p_rs.getInt(5)); //add dislikes into list
			    	photoList.add(p_rs.getString(6)); //add description into list
			    	photoList.add(p_rs.getInt(7)); //add publicity into list
			    	photoList.add(p_rs.getString(8)); //add upload_date into list
			    	photoList.add(num_comment.getInt(1)); // number of comments
		    	}
		    }
		    req.setAttribute("photoList", photoList);
		    
		    if (!f_rs.next()) {
		    	
		    }
		    else {
		    	//get the number of comments of this photo
		    	String num_comment_sql = "SELECT COUNT(*) FROM CommentPhoto WHERE pid='"+f_rs.getInt(1)+"'";
		    	ResultSet num_comment = c.createStatement().executeQuery(num_comment_sql);
		    	num_comment.next();
		    	favList.add(f_rs.getInt(1)); //add pid into list
		    	favList.add(f_rs.getString(2)); //add image type into list
		    	favList.add(f_rs.getString(3)); //add pname into list
		    	favList.add(f_rs.getInt(4)); //add likes into list
		    	favList.add(f_rs.getInt(5)); //add dislikes into list
		    	favList.add(f_rs.getString(6)); //add description into list
		    	favList.add(f_rs.getInt(7)); //add publicity into list
		    	favList.add(f_rs.getString(8)); //add upload_date into list
		    	favList.add(num_comment.getInt(1)); // number of comments
		    	while (f_rs.next()) {
		    		num_comment_sql = "SELECT COUNT(*) FROM CommentPhoto WHERE pid='"+f_rs.getInt(1)+"'";
		    		num_comment = c.createStatement().executeQuery(num_comment_sql);
		    		num_comment.next();
		    		favList.add(f_rs.getInt(1)); //add pid into list
			    	favList.add(f_rs.getString(2)); //add image type into list
			    	favList.add(f_rs.getString(3)); //add pname into list
			    	favList.add(f_rs.getInt(4)); //add likes into list
			    	favList.add(f_rs.getInt(5)); //add dislikes into list
			    	favList.add(f_rs.getString(6)); //add description into list
			    	favList.add(f_rs.getInt(7)); //add publicity into list
			    	favList.add(f_rs.getString(8)); //add upload_date into list
			    	favList.add(num_comment.getInt(1)); // number of comments
		    	}
		    }
		    req.setAttribute("favList", favList);
		    
		    if (!num_photo_rs.next()) {
		    	
		    }
		    else { 
		    	num_photo = num_photo_rs.getString(1);
		    }
		    req.setAttribute("num_photo", num_photo);
		    
		    req.setAttribute("page", Integer.toString(page));
		    req.setAttribute("pagesize", Integer.toString(pagesize));
		    
		    RequestDispatcher rd = req.getRequestDispatcher("/Contact.jsp");
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
