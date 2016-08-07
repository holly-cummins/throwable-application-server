<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="webjars/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="custom.css">

<title>Poll-tastic</title>
</head>
<body>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="webjars/jquery/1.11.1/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="webjars/bootstrap/3.3.1/js/bootstrap.min.js"></script>


	<script type="text/javascript" src="bounce.js"></script>
	<script type="text/javascript" src="refreshreadings.js"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="center-block">
	<h1></h1>
	<h2>Do the poll!</h2>

	<%
		final boolean mode = Boolean.valueOf(request.getAttribute("runningEmbedded") + "");
		if (mode) {
			out.println("<img src='liberty.png' width='200'/>");
		} else {
			out.println("<img src='bluemix.png' width='200'/>");
		}
	%>

	<div id="messages">...</div>
	<div id="bouncecount"></div>


	<script type="text/javascript">
		register();
	</script>

	<h4>Poll: Why are you interested in IoT?</h4>

	<form name="createComment" method="get" action="PlaceOrder">

		<input type="radio" name="reason" value="fun">It's just so
		<i>fun</i> <br /> <input type="radio" name="reason" value="work"
			checked> Business-case all the way

		<div class="textEntry">
			<label>What's your first name?<input type="text"
				name="name" /></label>
		</div>

		<input class="submit btn btn-default" type="submit" value="Submit" name="Submit"
			onclick="storeCommentContent()" />
	</form>
	<p />
	<div id="temperature-information"></div>
	<p />
	<div id="location"></div>
</div>
    </div>
    </div>
</body>
</html>