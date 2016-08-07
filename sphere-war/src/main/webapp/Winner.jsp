<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.ibm.wasdev.sphere.TempReading"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="webjars/bootstrap/3.3.1/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="custom.css">

<title>The winner is ...</title>
</head>
<body>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="webjars/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="jwebjars/bootstrap/3.3.1/css/bootstrap.min.css"></script>
    
<script type="text/javascript" src="refreshreadings.js"></script>
<div class="container">
    <div class="row">
        <div class="center-block">
<% 
final boolean mode = Boolean.valueOf(request.getAttribute ("runningEmbedded") + "");
if (mode)
{
	out.println("<img src='liberty.png' width='200'/>");
} else
{
	out.println("<img src='bluemix.png' width='200'/>");
}
%>

<p>
${winner}
</p>

<div id="temperature-information"></div>
<p/>
<div id="location"></div>
</div>
</div>
</div>
</body>
</html>