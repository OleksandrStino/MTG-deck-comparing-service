<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<!-- Bootstrap core CSS -->
		<link href="<c:url value="/pages/css/bootstrap.css" />" rel="stylesheet">


		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
		<![endif]-->
	</head>

	<body>
		<div class="container">
			<form action="/register" method="post">
				<div class="input-group">
					<input name="username" type="text" class="form-control form-text"/>
					<input name="password" type="text" class="form-control form-text"/>
					<button type="submit">addUser</button>
				</div>
			</form>
		</div>
		<br>
		<c:if test="${not empty user.username}">
			<c:out value="${user.username}"> is added</c:out>
		</c:if>
		<c:if test="${empty user.username}">
			<c:out value="${message}"></c:out>
		</c:if>
	</body>
</html>

