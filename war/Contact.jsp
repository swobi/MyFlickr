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
		//Here home display user information
		out.println("Hi,your user id is "+ uid +"!<bs>");
	}*/
	
	ArrayList p_rs = (ArrayList)request.getAttribute("photoList");
	ArrayList f_rs = (ArrayList)request.getAttribute("favList");
	ArrayList contact = (ArrayList)request.getAttribute("contact");
	ArrayList con_friend = (ArrayList)request.getAttribute("con_friend");
	int num_photo = Integer.parseInt((String)request.getAttribute("num_photo"));

	int pg = Integer.parseInt((String)request.getAttribute("page"));
	int ps = Integer.parseInt((String)request.getAttribute("pagesize"));
	int num_attribute=9;
	int total_pages = 0;
	
	if (num_photo % ps == 0)
		total_pages = num_photo/ps;
	else
		total_pages = (int)num_photo/ps + 1;
	
	boolean current_user = false;
	
	int user_id = Integer.parseInt(uid);
	if (contact.get(0).equals(user_id))
		current_user = true;
	else
		current_user = false;
	%>
	<div style="paddingtop: 100px"><br><br>
		<table class="table" style="width: 700px" align="center">
			<tbody>
				<tr>
					<% if (!contact.isEmpty()) {
							out.println("<td><h3>" + contact.get(1) + "'s photostream (" + num_photo + " photos)</h3>");
							out.println("<br>"+contact.get(4)); //description
							out.println("<br>" + contact.get(1) + "'s - <a href='friends?uid="+contact.get(0)+"'>Friends("+con_friend.size()+")</a>   <a href='favorites?uid="+uid+"&cid="+contact.get(0)+"'>Favorites(" + f_rs.size()/num_attribute + ")</a></td>");
							
							boolean friend_flag=false;
							for (int i=0; i<con_friend.size(); i++) {
								if (con_friend.get(i).equals(user_id)) {
									//this contact is a friend of current user
									friend_flag = true;
								}
							}
							//if this is current user's contact page
							//display edit profile button
							if (current_user == true){
								out.println("<td><br><br><br><br><a class='btn btn-primary' href='/profile'>");
							    out.println("Edit Profile");
							    out.println("</a></td>");
							}
							//if this contact is not a friend of current user
							//display add friend button
							else if (friend_flag == false) { 
								out.println("<td><br><br><br><br><a class='btn btn-warning' href='/addfriend?uid="+uid+"&contact="+contact.get(0)+"'>");
							    out.println("Add to Friend!");
							    out.println("</a></td>");
							}
							else {
								out.println("<td></td>");
							}
						}
						else {
							out.println("<td><h3>Couldn't find this user!</h3></td>");
						}
					%>
					
				</tr>
				<% 
				if (!contact.isEmpty()) {
					if (p_rs.isEmpty()) {
						out.println("<tr>");
						out.println("<td>" + contact.get(1) + " has no Photo!</td>");
						out.println("<td></td>");
						out.println("</tr>");
					}
					else {
						out.println("<tr><td><small>");
						//Photo range that is displaying
						if (pg*ps <= num_photo)
							out.println("Photo " + ((pg-1)*ps + 1) + " - " + pg*ps + "<br>");
						else
							out.println("Photo " + ((pg-1)*ps + 1) + " - " + num_photo + "<br>");
						
						if (ps == 4) {
							out.println("    <i class='icon-list'></i> 4 <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg=1&ps=10'>10</a> <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg=1&ps=20'>20</a>");
						}
						else if (ps == 10) {
							out.println("    <i class='icon-list'></i> <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg=1&ps=4'>4</a> 10 <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg=1&ps=20'>20</a>");
						}
						else if (ps == 20) {
							out.println("    <i class='icon-list'></i> <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg=1&ps=4'>4</a> <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg=1&ps=10'>10</a> 20");
						}
						out.println("</small></td>");
						
						//pagination
						out.println("<td><small>Page: ");
						if (pg != 1) {
							out.print("<a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg="+(pg-1)+"&ps="+ps+"'><i class='icon-chevron-left'></i></a>");
						}
						for (int i = 1; i<=total_pages; i++) {
							if (i != pg)
								out.print("  <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg="+i+"&ps="+ps+"'>"+i+"</a>");
							else
								out.print("  " + pg);
						}
						if (pg != total_pages) {
							out.print("  <a href='/contact?uid="+uid+"&cid="+contact.get(0)+"&pg="+(pg+1)+"&ps="+ps+"'><i class='icon-chevron-right'></i></a>");
						}
						out.println("</small></td></tr>");
						
						for (int i=0; i<p_rs.size(); i+=num_attribute) {
							out.println("<tr>");
							
							//display if the publicity of this photo public, or you have private access
							if ((p_rs.get(i+6).equals(0)) || (p_rs.get(i+6).equals(1) && current_user == true)) {
								//display image here
								out.println("<td><ul class='thumbnails'><li class='span4'><div><a href='ServeImage.jsp?pid=" + p_rs.get(i) + "' class='thumbnail'><img src='/imageServe?key="+p_rs.get(i)+"' alt=''></a>");
								//out.println("<td><a href='/photo?pid=" + p_rs.get(i) + "'><img src='/imageServe?key="+p_rs.get(i)+"'></a>");
								out.println("<strong><a href='ServeImage.jsp?pid=" + p_rs.get(i) + "'>" + p_rs.get(i+2) + "</a></strong>");
								if (p_rs.get(i+5) != "") {
									out.println("<br>" + p_rs.get(i+5));	
								}
								out.println("<br><small>Uploaded on " + p_rs.get(i+7));
								if (current_user == true) {
									out.println("  |  <a href='/delete?pid=" + p_rs.get(i) + "'>Delete</a>");
								}
								out.println("<br><i class='icon-thumbs-up'></i>" + p_rs.get(i+3));
								out.println("  <i class='icon-thumbs-down'></i>" + p_rs.get(i+4));
								out.println("  <i class='icon-comment'></i>" + p_rs.get(i+8) + "</small></div></li></ul></td>");	
							}
							//display another image in the same row
							i+=num_attribute;
							if (i<p_rs.size()) {
								//display if the publicity of this photo public, or you have private access
								if ((p_rs.get(i+6).equals(0)) || (p_rs.get(i+6).equals(1) && current_user == true)) {
									//display image here
									out.println("<td><ul class='thumbnails'><li class='span4'><div><a href='ServeImage.jsp?pid=" + p_rs.get(i) + "' class='thumbnail'><img src='/imageServe?key="+p_rs.get(i)+"' alt=''></a>");
									out.println("<strong><a href='ServeImage.jsp?pid=" + p_rs.get(i) + "'>" + p_rs.get(i+2) + "</a></strong>");
									if (p_rs.get(i+5) != "") {
										out.println("<br>" + p_rs.get(i+5));	
									}
									out.println("<br><small>Uploaded on " + p_rs.get(i+7));
									if (current_user == true) {
										out.println("  |  <a href='/delete?pid=" + p_rs.get(i) + "'>Delete</a>");
									}
									out.println("<br><i class='icon-thumbs-up'></i>" + p_rs.get(i+3));
									out.println("  <i class='icon-thumbs-down'></i>" + p_rs.get(i+4));
									out.println("  <i class='icon-comment'></i>" + p_rs.get(i+8) + "</small></div></li></ul></td></tr>");	
								}
							}
							else {
								out.println("<td></td></tr>");
							}
						}
					}
				}
				%>
			</tbody>
		</table>
	</div>
	
	<p><p>
	
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
		              <li class="">
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