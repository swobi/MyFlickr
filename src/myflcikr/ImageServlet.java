package myflcikr;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.commons.codec.binary.Base64;






import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;




	public class ImageServlet extends HttpServlet {
	    private static final long serialVersionUID = 1L;
	    
	    public ImageServlet() {
	        super();
	    }

	    protected void doGet ( HttpServletRequest req, HttpServletResponse res
	        ) throws ServletException, IOException {
	 
	    	PersistenceManager pm=null;
	    	int keyParam=Integer.parseInt(req.getParameter("key"));
	    	byte[] imageByte=null;
	    	String imageType=null;
	    	
	    	try
	    	{
	    		//try first to get the image from Cache
	    	    Cache cache;
		        try {
		        	 CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		             cache = cacheFactory.createCache(Collections.emptyMap());
		             imageByte = (byte[]) cache.get(keyParam);
		             // if the image doesn't exist in cache, read out from the datastore and update the cache
		             if (imageByte==null)
		             {
		            		pm = PMF.get().getPersistenceManager();
		        	        Query query = pm.newQuery(Photo.class,"id==keyParam");
		        	        query.declareParameters("int keyParam");
		        	        query.setRange(0, 1);
		        	      //  List<Photo> results = (List<Photo>)query.execute(keyParam);
		        	      //  results.size();
		        	        try {
		        	            List<Photo> results = (List<Photo>) query.execute(keyParam);
		        	            if (results.iterator().hasNext()) {
		        	                // If the results list is non-empty, return the first (and only)
		        	                // result
		        	            	Photo image=results.iterator().next();
		        	            	 imageType=image.getImageType();
		 		        	        imageByte = image.getImage();
		 		        	        // update the cache
		 		        	        cache.put(keyParam, imageByte);
		 		        	        System.out.println("Read data from Datastore!");
		        	            }
		        	            else
		        	            	System.out.println("The list get is null");
		        	        }
		       	    	 finally{
		    	    		 if(pm!=null)
		    	    			 pm.close();
		    	    		 
		    	    		
		    	    	 }
		        
		        	        
		        	       
		             }
		             else
		            	 System.out.println("Got data from Memcache!");
		             
		             //Try another method
		        /*     String data_uri=Base64.encodeBase64URLSafeString(imageByte);
		             req.setAttribute("data_uri", data_uri);
		             ServletContext context= getServletContext();
				        RequestDispatcher rd= context.getRequestDispatcher("/ServeImage.jsp");
				        try {
							rd.forward(req, res);
						} catch (ServletException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
		         
		            res.setContentType(imageType);
		 	        ByteArrayInputStream iStream = new ByteArrayInputStream(imageByte);
		 	        
		 	        // Determine the length of the content data.
		 	        int length = imageByte.length;
		 	        res.setContentType(imageType);
		 	        res.setContentLength(length);
		 	        
		 	        // Get the output stream from our response object, so we
		 	        // can write our image data to the client:
		 	        ServletOutputStream oStream = res.getOutputStream();
		 	        
		 	        // Now, loop through buffer reads of our content, and send it to the client:
		 	        byte [] buffer = new byte[1024];
		 	        int len;
		 	        while ((len = iStream.read(buffer)) != -1) {
		 	            oStream.write(buffer, 0, len);
		 	        }
		 	        
		 	        // Now that we've sent the image data to the client, close down all the resources:
		 	        iStream.close();
		 	        
		 	        oStream.flush();
		 	        oStream.close(); 

		             
		        } catch (CacheException e) {
		            // ...
		        }
	 


	
	     
	    	}
	    	 catch (Exception e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}

	    }

	    protected void doPost(
	        HttpServletRequest request, HttpServletResponse response
	        ) throws ServletException, IOException {

	        // Pass through to the doPost method:
	        doGet(request, response);
	    }
	} // end of ImageServlet



