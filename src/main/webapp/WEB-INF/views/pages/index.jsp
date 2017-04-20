<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="container">

	<sec:authorize access="hasRole('ADMIN')">
		<form action="/uploadDecks" method="post">
			<button type="submit">upload</button>
		</form>
	</sec:authorize>

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