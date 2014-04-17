<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head></head>
	<body> <%
        session.invalidate();
        response.sendRedirect("/faces/secure/home.xhtml");
	%>
	</body>
</html>