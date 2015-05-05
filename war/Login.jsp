

<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script>
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
	</script>
</head>
<style>
body
{
	background-image:url('img/Login.jpg');
}
</style>
<body>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<div class="container-fluid">
	  <div class="row-fluid">
	    <div class="span2">
	      <!--Sidebar1 content-->
	    </div>
	    <div class="span4" style="padding-top:450px">
	    </div>
	    <div class="span4" style="padding-top:420px">
	      	<h2 class="text-info">Welcome to MyFlickr</h2>
			  <h4 class="text-info"><b>Explore a colorful life by photos</b></h4>
			  <p>
			    <a class="btn btn-warning" href="/signin">
			    <i class="icon-envelope icon-white"></i>
			      Sign in with Gmail
			    </a>
			  </p>
	    </div>
		
		<div class="span2">
	      <!--Sidebar2 content-->
	    </div>
	  </div>
	</div>
	
</body>
</html>