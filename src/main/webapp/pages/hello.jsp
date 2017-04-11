<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Home</title>
</head>
<body>
	<form action="/" method="post">
		<div class="input-group">
			<input name="name" type="text" class="form-control form-text"/>
			<button type="submit">addUser</button>
		</div>
	</form>

	<c:if test="${not empty user.name}">
		<c:out value="${user.name}"> is added</c:out>
	</c:if>
	<c:if test="${empty user.name}">
		<c:out value="${message}"></c:out>
	</c:if>
</body>
</html>
