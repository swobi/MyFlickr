<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/DTD/strict.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Example Image Servlet Application</title>
    </head>
    <body>
        <h2>Example Image Servlet Application</h2>
        <h3>Here is my image:</h3>
      <!-- <img src="data:image/png;base64,<%= request.getAttribute("data_uri") %>" ... /> --> 
  	
   <img src="${pageContext.request.contextPath}/imageServe?key=<%= request.getParameter("key") %>" />
          
         
    </body>
</html>

