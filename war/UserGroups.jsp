<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<body>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<%@page import="java.util.ArrayList" %>
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
		//Here home display uiser information
		out.println("Hi,your user id is "+ uid +"!<bs>");
	}
	
	ArrayList rs = (ArrayList)request.getAttribute("rs");
	//int id = request.getAttribute("id");
	String uname = (String)request.getAttribute("uname");
		
	%>
	<div><br><br>
		<table class="table" style="width: 500px" align="center">
			<tbody>
				<tr>
					<td></td>
					<% out.println("<td><h3>" + uname + "'s Groups (" + rs.size()/3 + ")</h3></td>"); %>
				</tr>
				<tr>
					<td>#</td>
					<td>Name</td>
				</tr>
				<%
					if (rs.size() == 0) {
						out.println("<tr>");
						out.println("<td></td>");
						out.println("<td>You have no Groups!</td>");
						out.println("</tr>");
					}
					else {
						for (int i=0; i<rs.size(); i+=3) {
							int n = i/3 + 1;
							out.println("<tr>");
							out.println("<td>" + n + "</td>");
							out.println("<td><strong><a href=\"/group?gid=" + rs.get(i) + "\">" + rs.get(i+1) + "</a></strong>");
							out.println("<br>Description: " + rs.get(i+2) + "</td>");
							out.println("</tr>");
						}
					}
				%>
			</tbody>
		</table>
	</div>
	
	<div class="navbar navbar-inverse navbar-fixed-top" >
		<div class="navbar-inner">
			<div class="container">
		          <div class="nav-collapse collapse">
		            <ul class="nav">
		              <li class="">
		                <a href="Home.jsp"><i class="icon-home icon-white"></i>Home</a>
		              </li>
					  <li class="">
		                <a href="profile.jsp"><i class="icon-user icon-white"></i>Me</a>
		              </li>
		              <li class="">
		                <a href="friends?uid=<%=uid%>"><i class="icon-book icon-white"></i>Friends</a>
		              </li>
		              <li class="active">
		                <a href="usergroups?uid=<%=uid%>"><i class="icon-globe icon-white"></i>Groups</a>
		              </li>
		            </ul>
					<form class="navbar-search pull-right">
				  		<input type="text" class="search-query" placeholder="Search">
				 	</form>
					<a href="upload.jsp" class="btn btn-primary">
						<i class="icon-upload icon-white"></i>Upload</a>
		          </div>
			</div>
		</div>
	</div>
</body>
</html>