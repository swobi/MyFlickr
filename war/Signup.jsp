
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script>
	function validateForm()
	{
		var userName=document.forms["signupform"]["inputUserName"].value;
	
		if (userName==null || userName=="")
  		{
  			alert("Username must be filled out");
  			return false;
  		}
	}
	/*
	function validateForm1()
	{
		var email=document.forms["signinform"]["email"].value;
		var password=document.forms["signinform"]["password"].value;
		if (email==null || email=="")
  		{
  			alert("Email must be filled out");
  			return false;
  		}
		if (password==null || password=="")
  		{
  			alert("Password must be filled out");
  			return false;
  		}
	}
	*/
	</script>
	
</head>
<body>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<h1 class="text-info" style="padding-top:80px;margin:auto;width:70%;text-align:center;">Choose a user name to sign up!</h1>
	
	</br>
	</br>
	<div class="container-fluid">
	  <div class="row-fluid">
	    <div class="span6" style="padding-top:100px;padding-bottom:100px">
	      <img src="img/Signup.jpg" class="img-rounded">
	    </div>
	    <%
	    UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String email=user.getEmail();
		if (request.getParameterNames().hasMoreElements())
		{
			String status=(String)request.getAttribute("status");
			if (!status.equals(""))
				if(status.equals("userNameTaken"))
				{
					out.println("<div class='alert span6'><button type='button' class='close' data-dismiss='alert'>x</button>"+
					"<strong>Warning!</strong> User name already taken, choose another one</div>");
				}
				else if(status.equals("NewUser"))
				{
					out.println("<div class='alert alert-info span6'><button type='button' class='close' data-dismiss='alert'>x</button>"+
						"<strong>Warning!</strong><strong>Welcome!</strong> You are a new user,please sign in</div>");
				}
				else if(status.equals("SystemFail"))
				{
					out.println("<div class='alert alert-error span6'><button type='button' class='close' data-dismiss='alert'>x</button>"+
							"<strong>Warning!</strong> System Fails, try later...</div>");
				}

		}
	    %>
	    <div class="span6" style="padding-top:200px;padding-bottom:200px">
	        <form class="form-horizontal" name="signupform" onsubmit="return validateForm()" action="/signup" method="post">
			<!--  <div class="control-group">
			    <label class="control-label" for="inputEmail">Email</label>
			    <div class="controls">
			      <input type="text" id="inputEmail" name="inputEmail" placeholder="Email">
			    </div>
			  </div>   -->
			  <div class="control-group">
			    <label class="control-label" for="Email">Email</label>
			    <div class="controls">
			      <input id="disabledInput" type="text" placeholder="<%=email %>" disabled>
			    </div>
			  </div>
			  <div class="control-group">
			    <label class="control-label" for="inputUserName">User Name</label>
			    <div class="controls">
			      <input type="text" id="inputUserName" name="inputUserName" placeholder="User Name">
			    </div>
			  </div>
			  <!--
			  <div class="control-group">
			    <label class="control-label" for="inputPassword">Password</label>
			    <div class="controls">
			      <input type="password" id="inputPassword" name="inputPassword" placeholder="Password">
			    </div>
			  </div>
			  -->
			  <div class="control-group">
			    <div class="controls">
			      <button type="submit" class="btn btn-warning">Sign up</button>
			    </div>
			  </div>
			</form>
	    </div>
	  </div>
	</div>
	

			
	    
</body>
</html>