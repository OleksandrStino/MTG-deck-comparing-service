<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

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

	<h1>${deck.name} page</h1>
	<h2>User: <sec:authentication property="principal.username"/></h2>
	<br>
	<h2>Add card</h2>
	<br>

	<form action="/addCart" method="post">
		<div class="input-group">
			<input name="cardName" type="text" class="form-control form-text"/>
			<input name="id" type="text" class="form-control form-text"/>
			<button type="submit">addCard</button>
		</div>
	</form>
	<br>

	<br>
	<c:if test="${not empty card.name}">
		card <c:out value="${card.name}"/> is added
	</c:if>

</div>
</body>
</html>

