<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
	<c:out value="${message}"/>
</c:if>
