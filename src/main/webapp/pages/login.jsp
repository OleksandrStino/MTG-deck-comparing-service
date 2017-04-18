<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">

	<title>Spring Security</title>

	<!-- Bootstrap core CSS -->
	<link href="<c:url value="/pages/css/bootstrap.css" />" rel="stylesheet">


	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
</head>

<body>

<div class="container" style="width: 300px;">

	<form method="POST" action="login" class="form-signin">
		<h2 class="form-heading">Log in</h2>

		<div class="form-group ${error != null ? 'has-error' : ''}">
			<span>${message}</span>
			<input name="username" type="text" class="form-control" placeholder="Username"
				   autofocus="true"/>
			<input name="password" type="password" class="form-control" placeholder="Password"/>


			<button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
			<h4 class="text-center"><a href="/register">Create an account</a></h4>
		</div>

	</form>

</div>


</body>
</html>