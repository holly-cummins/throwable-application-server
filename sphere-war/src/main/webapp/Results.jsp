<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.ibm.wasdev.sphere.PollSubmission"%>
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


<title>Poll-tastic</title>
    <script type="text/javascript" src="bounce.js"></script>
    <script type="text/javascript" src="refreshreadings.js"></script>

    <script type="text/javascript" src="webjars/d3js/3.5.5/d3.min.js"></script>
    <script type="text/javascript" src="webjars/c3/0.4.11/c3.min.js"></script>
    <link href="webjars/c3/0.4.11/c3.min.css" rel="stylesheet" type="text/css">

        
  </head>
  <body>
    <script src="webjars/jquery/1.11.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.1/js/bootstrap.min.js"></script>
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
${summary}
</p>

  <div   id="messages" >...</div>
    <div   id="bouncecount" ></div>
          <div   id="temperature-readings" ></div>
  
<script type="text/javascript">register(); 
</script>

	<h4>Who's playing?</h4>
	<table>
	<tr><th>Name</th></tr>
	<%
	List<PollSubmission> orders = (List<PollSubmission>) request.getAttribute("result");
			if (orders != null) {
				for (PollSubmission order : orders) {
             	   out.println("<tr>");
                   out.println("<td>" + order.getName() + "</td>" );
			       out.println("</tr>");
			  }
			}
		%>
	</table>

	<h4>Is the computer cooking? Who knows? That needs network!</h4>
<div id="chart"></div>
      <script type="text/javascript" src="/c3chart.js"></script>

<form name="clearData" method="get" action="ClearData">

		<input class="submit btn btn-default" type="submit" value="Clear data" name="submit" onclick="storeCommentContent()"/>
</form>
	

<div id="temperature-information"></div>
<p/>
<div id="location"></div>
</div>
</div>
</div>

</body>
</html>