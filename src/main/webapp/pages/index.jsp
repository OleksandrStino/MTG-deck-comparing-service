<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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

	<h1>Welcome <sec:authentication property="principal.username"/></h1>
	<br>
	<h3>Create your deck</h3>
	<form action="/decks" method="post">
		<div class="input-group">
			<input name="name" type="text" class="form-control form-text"/>
			<button type="submit">addDeck</button>
		</div>
	</form>
	<br>
	<table class="table-bordered col-sm-6">
		<tr>
			<th colspan="2">List of decks:</th>
		</tr>
		<c:forEach items="${decks}" var="deck">
			<tr>
				<td class="col-sm-3"><a href="<spring:url value='/decks/${deck.id}'/>">
					<c:out value="${deck.name}" /></a></td>
				<td class="col-sm-3">
					<a href="<spring:url value='/decks/${deck.id}/remove' />" >remove</a>
				</td>
			</tr>
		</c:forEach>
	</table>

</div>
</body>
</html>

