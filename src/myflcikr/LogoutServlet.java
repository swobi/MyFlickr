package myflcikr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LogoutServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {
        UserService userService = UserServiceFactory.getUserService();
		String uid;
		for(Cookie cookie:req.getCookies())
		{
			if(cookie.getName().equals("uid"))
			{
				uid=cookie.getValue();	
				cookie.setMaxAge(0);
			}
		}
		resp.sendRedirect(userService.createLogoutURL("/Logout.jsp"));
	}

}
