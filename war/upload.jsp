<!DOCTYPE HTML>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap-responsive.css" rel="stylesheet">
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script>
	
	function formValidation()
{
	var ele=document.getElementById("uploadfile")
	var str=ele.value.toLowerCase();
	var pattern=/(.jpg|.png|.jpeg)$/;
	if (str==null || str=="")
		{
			alert("Please choose a file to upload!");
			ele.focus();
			return false;
		}
	else if (pattern.test(str)==true)
		return true;
	else
		{
			alert("The upload file type is wrong \n please upload an image!");
			ele.value="";
			return false;
		}
}
</script>
<style>
body
{
	background-color:black;
}
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
 <form action="/upload"  method="post" onsubmit="return formValidation()" enctype="multipart/form-data">
	<input type="file" class="file" name="myFile" id="uploadfile"/>
	<input type="submit" class="btn btn-primary" value="Submit">
 </form>
</div>
  
	
</body>
</html>