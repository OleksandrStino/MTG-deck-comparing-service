<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Home</title>
</head>
<body>
<c:if test="${not empty card}">
	<img src="${card.imageUrl}"><br>
	<h3>${card.name}</h3><br>
	<p>${card.type}</p><br>
	<c:forEach var="var" items="${card.types}">
		<p><c:out value="${var}" /></p>
	</c:forEach><br>
	<p>${card.rarity}</p><br>
	<p>${card.setName}</p><br>
	<p>${card.text}</p><br>

</c:if>
<form action="/home" method="post">
	<div class="input-group">
		<input name="searchedCardName" type="text" class="form-control form-text"/>
		<button type="submit">Search</button>
	</div>
</form>
</body>
</html>
