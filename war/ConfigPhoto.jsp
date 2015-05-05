<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">

<style>

div.center
{
	position:absolute;
    top:50%;
    left:35%;
    width:350px;
}
</style>
</head>
<body>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="js/bootstrap.min.js"></script>

	<% 
	String uid="-1";
	for(Cookie c:request.getCookies())
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
    	response.sendRedirect("Login.jsp");

	}  	

	
%>

 <div class="center">
 <form action="/configPhoto" method="post" >
 	
	  <label for="public">Public</label>
      <input type="radio" name="publicity" id="public" value="0">
      <label for="private">Private</label>
      <input type="radio" name="publicity" id="private" value="1">
      <br>
      <label for="des">Description</label>
      <textarea id="des" name="description"> </textarea> 
      <input type="hidden" name="pname" value="<%=request.getAttribute("pname") %>">
      <input type="hidden" name="imageType" value="<%= request.getAttribute("imageType") %> ">
      <input type="hidden" name="pid" value="<%= request.getAttribute("pid") %>">
      <input type="hidden" name="uid" value="<%= uid %>">
	<input type="submit" class="btn btn-primary" value="Submit">
 </form>
</div>
  
	
</body>
</html>