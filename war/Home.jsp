<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Home</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
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
		}
	}
	if(uid=="-1")
	{
		out.println("<html><head></head><body><script>" +
			 	"alert('Please go back to log in!')</script></body></html>");
    	response.setHeader("Refresh","1;url=/Home.jsp");

	}
	else
	{
		//Here home display user information
		out.println("<html><head></head><body><script>" +
			 	"alert('Hi,your user id is "+ uid+"!')</script></body></html>");
	}
	   	
	%>
	<div class="navbar navbar-inverse navbar-fixed-top" >
		<div class="navbar-inner">
			<div class="container">
		          <div class="nav-collapse collapse">
		            <ul class="nav">
		              <li class="active">
		                <a href="Home.jsp"><i class="icon-home icon-white"></i>Home</a>
		              </li>
					  <li class="">
		                <a href="/profile"><i class="icon-user icon-white"></i>Me</a>
		              </li>
		              <li class="">
		                <a href="friends?uid=<%=uid%>"><i class="icon-book icon-white"></i>Friends</a>
		              </li>
		              <li class="">
		                <a href="usergroups?uid=<%=uid%>"><i class="icon-globe icon-white"></i>Groups</a>
		              </li>
		           </ul>
		           <a class="btn btn-primary" href="upload.jsp"><i class="icon-upload icon-white"></i>Upload</a>
		           <!-- 
					<form class="navbar-search pull-left">
				  		<input type="text" class="search-query" placeholder="Search">
				 	</form>
				 	 -->
				 	<div class="pull-right">
  						<a class="btn btn-warning" href="/logout"><i class="icon-off icon-white"></i>Logout</a>
					</div>
		          </div>
			</div>
		</div>
	</div>
</body>
</html>