<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="container">

	<form action="/decks/${deck.id}/compareDeck" method="post">
		<button type="submit">compare</button>
	</form>

	<h1>${deck.name} page</h1>

	<h2>User: <sec:authentication property="principal.username"/></h2>

	<br>
	<h2>Add card</h2>
	<br>

	<form action="/decks/${deck.id}/addCard" method="post">
		<div class="input-group">
			<input name="cardName" type="text" class="form-control form-text"/>
			<input name="amount" value="1" type="text" class="form-control form-text"/>
			<button type="submit">addCard</button>
		</div>
	</form>
	<br>


	<c:if test="${not empty cardList}">
		<h3>List of cards with same name</h3>
		<c:forEach var="item" items="${cardList}">
			<br>
			<form action="/decks/${deck.id}/addCardFromList" method="post">
				<div class="input-group">
					<p class="img">${item.name} ${item.setName}</p>
					<input id="${fn:replace((item.name.concat(" ").concat(item.setName)), " ", "_")}" type="hidden" value="${item.imageUrl}">
					<input name="cardName" type="hidden" value="${item.name}" class="form-control form-text"/>
					<input name="set" type="hidden" value="${item.setName}" class="form-control form-text"/>
					<input name="amount" value="1" type="text" class="form-control form-text"/>
					<button type="submit" id="${item.name}">addCard</button>
				</div>
			</form>

			<br>
		</c:forEach>

		<img id="clicked" src="">
	</c:if>


	<script type="text/javascript">
        $(document).ready(function() {
			$('.img').click(function() {
			    var id = strReplaceAll($(this).text(), " ", "_");
            	var input = (document.getElementById(id));
            	var src = input.value;
            	$("#clicked").attr('src', src);
        	});
        });

        function strReplaceAll(string, Find, Replace) {
            try {
                return string.replace( new RegExp(Find, "gi"), Replace );
            } catch(ex) {
                return string;
            }
        }
	</script>

	<br>
	<h3>Cards in buffer:</h3>
	<c:if test="${not empty mapOfCards}">
		<c:forEach var="entry" items="${mapOfCards}">
			<br>
			<form action="/decks/${deck.id}/${entry.key.multiverseid}/removeCardFromBuffer" method="post">
				Name: <c:out value="${entry.key.name}"/><input name="amount" type="number" value="1" class="form-text"><button type="submit">remove</button>
			</form>
			<br>
			<img src="${entry.key.imageUrl}">
			<br>
			amount: <c:out value="${entry.value}"/>
			<br>
		</c:forEach>
	</c:if>
	<br>
	<form action="/decks/${deck.id}/updateDeck" method="post">
		<button type="submit">updateDeck</button>
	</form>
	Cards in deck:
	<c:if test="${not empty deck.cards}">
		<c:forEach var="entry" items="${deck.cards}">
			<br>

			<form action="/decks/${deck.id}/${entry.key.multiverseid}/removeCardFromDeck" method="post">
				Name: <c:out value="${entry.key.name}"/><input name="amount" type="number" value="1" class="form-text"><button type="submit">remove</button>
			</form>
			<br>
			<img src="${entry.key.imageUrl}">
			<br>
			amount: <c:out value="${entry.value}"/>
			<br>
		</c:forEach>
	</c:if>

	<c:forEach var="entry" items="${mapOfComparingResult}">
		<br>
		Deck Name: <a href="${entry.key.deckUrl}">${entry.key.name}</a>
			matches: <c:out value="${entry.value}"/>
		<br>
	</c:forEach>
	<c:out value="${message}"/>

</div>
