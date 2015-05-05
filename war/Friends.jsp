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
	
	Cookie[] cookies=request.getCookies();
	if (cookies != null)
	{
		for (int i = 0; i < cookies.length; i++) 
		{
			if (cookies [i].getName().equals ("uid"))
			{
				uid=cookies[i].getValue();
				break;
			}
		}
	}
	if(uid.equals("-1"))
	{
    	 response.sendRedirect("/signin");
	 }
	/*
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
	}*/
	
	ArrayList rs = (ArrayList)request.getAttribute("rs");
	int id = Integer.parseInt((String)request.getAttribute("id"));
	String uname = (String)request.getAttribute("uname");
	//int num_friend = Integer.parseInt((String)request.getAttribute("num_friend"));
	
	int pg = Integer.parseInt((String)request.getAttribute("page"));
	int ps = Integer.parseInt((String)request.getAttribute("pagesize"));
	int num_attribute = 3, total_pages = 0;
	int num_friends = Integer.parseInt((String)request.getAttribute("num_friends"));
	
	if (num_friends % ps == 0)
		total_pages = num_friends/ps;
	else
		total_pages = (int)num_friends/ps + 1;
	
	%>
	<div><br><br>
		<table class="table" style="width: 500px" align="center">
			<tbody>
				<tr>
					<td></td>
					<% out.println("<td><h3>" + uname + "'s Friends (" + num_friends + ")</h3></td>"); %>
				</tr>
				<tr>
					<td>
					<%  //range of friends displaying
						if (pg*ps <= num_friends)
							out.println("Friend " + ((pg-1)*ps + 1) + " - " + pg*ps);
						else
							out.println("Friend " + ((pg-1)*ps + 1) + " - " + num_friends); %>
					</td>
					<td>Page:
					<% 	//pagination
						if (pg != 1) {
							out.print("<a href='/friends?uid="+id+"&pg="+(pg-1)+"&ps="+ps+"'><i class='icon-chevron-left'></i></a>");
						}
						for (int i = 1; i<=total_pages; i++) {
							if (i != pg)
								out.print("  <a href='/friends?uid="+id+"&pg="+i+"&ps="+ps+"'>"+i+"</a>");
							else
								out.print("  " + pg);
						}
						if (pg != total_pages) {
							out.print("  <a href='/friends?uid="+id+"&pg="+(pg+1)+"&ps="+ps+"'><i class='icon-chevron-right'></i></a>");
						}  %>
					</td>
				</tr>
				<% 
				if (rs.size() == 0) {
					out.println("<tr>");
					out.println("<td></td>");
					out.println("<td>You have no Friends!</td>");
					out.println("</tr>");
				}
				else {
					for (int i=0; i<rs.size(); i+=3) {
						out.println("<tr>");
						out.println("<td>" + ((pg-1)*ps + 1 + i/3) + "</td>");
						out.println("<td><strong><a href=\"/contact?uid="+uid+"&cid=" + rs.get(i) + "\">" + rs.get(i+1) + "</a></strong>");
						out.println("<br>Description: " + rs.get(i+2) + "</td>");
						out.println("</tr>");
						//out.println("<p align=center>Hi,your friend <a href=\"/contact?user_id=" + rs.get(i) + "\">" + rs.get(i+1) + "</a>'s user id is "+ rs.get(i)+"!</p>");
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
		                <a href="/home?start=0&end=6"><i class="icon-home icon-white"></i>Home</a>
		              </li>
					  <li class="">
		                <a href="/contact?uid=<%=uid%>&cid=<%=uid%>"><i class="icon-user icon-white"></i>Me</a>
		              </li>
		              <li class="active">
		                <a href="/friends?uid=<%=uid%>"><i class="icon-book icon-white"></i>Friends</a>
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