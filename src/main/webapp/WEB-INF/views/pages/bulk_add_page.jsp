<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="container">


	BULK PAGE ID: ${deckId} Name: ${deck.name}

	<form action="/decks/${deckId}/bulkAdd" method="post">
		<TEXTAREA name="cardRows" id="cardRows" rows="15" cols="65"></TEXTAREA>
		<button type="submit">bulkAdd</button>
	</form>
	
	
	
	<c:if test="${not empty errorLines}">
		<h3>List of error lines</h3>
		<c:forEach var="item" items="${errorLines}">
			
			${item}

			
		</c:forEach>

		<img id="clicked" src="">
	</c:if>


</div>
