<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Profile</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script type="text/javascript">
	function check(id)
  	{

  	document.getElementById(id).checked=true

  	}
	function uncheck(id)
  	{
  	document.getElementById(id).checked=false

  	}
	</script>
</head>
<body>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="js/bootstrap.min.js"></script>
	
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
		                <a href="friends.jsp"><i class="icon-book icon-white"></i>Friends</a>
		              </li>
		              <li class="">
		                <a href="groups.jsp"><i class="icon-globe icon-white"></i>Groups</a>
		              </li>
		            </ul>
		           <a class="btn btn-primary" href="upload.jsp"><i class="icon-upload icon-white"></i>Upload</a>
		            
		            <!--
					<form class="navbar-search pull-right">
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
	
	<% String email=(String)request.getAttribute("email");
	   String uname=(String)request.getAttribute("uname");
	   String gender="";
	   String description="";
	   String b1;
	   String b2;
	   if (request.getAttribute("description")!=null)
		   description=(String)request.getAttribute("description");  
	   if (request.getAttribute("gender")!=null)
		   gender=(String)request.getAttribute("gender");
	   
	   	
	 %>
	<div class="span4" style="padding-top:250px">
		
	 </div>
		<div class="span4" id="try" style="padding-top:250px">
			<%
			if (request.getParameterNames().hasMoreElements())
			{
				String status=(String)request.getAttribute("status");
				if (!status.equals(""))
				{
					if(status.equals("nameTaken"))
					{
						out.println("<div class='alert span6'><button type='button' class='close' data-dismiss='alert'>x</button>"+
						"<strong>Warning!</strong> User name already taken, choose another one</div>");
					}
					else if(status.equals("SystemFail"))
					{
						out.println("<div class='alert alert-error span6'><button type='button' class='close' data-dismiss='alert'>x</button>"+
								"<strong>Warning!</strong> System Fails, try later...</div>");
					}
					else if(status.equals("Success"))
					{
						out.println("<div class='alert alert-success span6'><button type='button' class='close' data-dismiss='alert'>x</button>"+
								"<strong>Success!</strong> Profile Changed!</div>");
					}
				}
			}
			%>
	      	<form class="form-horizontal" action="/profile" name="profileForm" method="post">
			  <div class="control-group">
			    <label class="control-label" for="inputEmail">Email</label>
			    <div class="controls">
			      <input class="input-xlarge" id="disabledInput" type="text" placeholder="<%=email %>" disabled>
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="uname">User Name</label>
			    <div class="controls">
			      <input class="input-xlarge" name="uname" type="text" value="<%=uname %>" placeholder="<%=uname %>" >
			    </div>
			  </div>
			 <div class="control-group">
			  <div class="controls">
			  <label class="radio">
  				<input type="radio" name="optionsRadios" id="gender_male" value="male">Male
			 	 </label>
				<label class="radio">
  				<input type="radio" name="optionsRadios" id="gender_female" value="female">Female
				</label>
				</div>
			</div>
			<%
			if(gender.equals("female"))
			   {
				   
				   out.println("<script type='text/javascript' language='JavaScript'>check('gender_female')</script>");
				   out.println("<script type='text/javascript' language='JavaScript'>uncheck('gender_male')</script>");

			   }
			   else
			   {
				   out.println("<script type='text/javascript' language='JavaScript'>check('gender_male')</script>");
				   out.println("<script type='text/javascript' language='JavaScript'>uncheck('gender_female')</script>");


			   }
			%>
			  <div class="control-group">
				 <label class="control-label" for="description">Description</label>
			   	<div class="controls">
			  		<input  class="input-xxlarge" placeholder="Description" name="description" id="focusedInput" value="<%=description %>">
				</div>
			</div>
			<div class="control-group">
			    <div class="controls">
			      <button type="submit" class="btn btn-primary">Save</button>
			    </div>
			  </div>
			</form>
	    </div>
</body>
</html>