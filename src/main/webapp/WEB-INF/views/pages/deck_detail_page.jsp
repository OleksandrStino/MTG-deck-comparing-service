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

	<h3>List of cards with same name</h3>
	<c:if test="${not empty cardList}">
		<table style="width: 100%" class="table table-bordered">
			<tr>
				<th width="15%">name</th>
				<th width="15%">set name</th>
				<th width="10%">rarity</th>
				<th width="10%">type</th>
				<th width="45%">text</th>
				<th width="5%">remove</th>

			</tr>
			<c:forEach var="element" items="${cardList}">
				<tr style="font-size: 80%">
					<td>
						<a class="card-name" href="${element.imageUrl}"><c:out value="${element.name}"/></a>
					</td>
					<td><c:out value="${element.setName}"/></td>
					<td><c:out value="${element.rarity}"/></td>
					<td><c:out value="${element.type}"/></td>
					<td><c:out value="${element.text}"/></td>
					<td>
						<form action="/decks/${deck.id}/addCardFromList" method="post">
							<div class="input-group">
								<input hidden name="cardName" type="hidden" value="${element.name}"
									   class="form-control form-text"/>
								<input name="set" type="hidden" value="${element.setName}"
									   class="form-control form-text"/>
								<input name="amount" value="1" type="text" class="form-control form-text">
								<button type="submit">addCard</button>
							</div>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<h3>Cards in deck:</h3>
	<c:if test="${not empty deck.cards}">
		<table style="width: 100%" class="table table-bordered">
			<tr>
				<th width="15%" class="name">name</th>
				<th width="5%">amount</th>
				<th width="15%">set name</th>
				<th width="10%">rarity</th>
				<th width="10%">type</th>
				<th width="40%">text</th>
				<th width="5%">remove</th>
			</tr>
			<c:forEach var="entry" items="${deck.cards}">

				<tr style="font-size: 80%">
					<td>
						<a class="card-name" href="${entry.key.imageUrl}"><c:out value="${entry.key.name}"/></a>
					</td>
					<td><c:out value="${entry.value}"/></td>
					<td><c:out value="${entry.key.setName}"/></td>
					<td><c:out value="${entry.key.rarity}"/></td>
					<td><c:out value="${entry.key.type}"/></td>
					<td><c:out value="${entry.key.text}"/></td>
					<td>
						<form action="/decks/${deck.id}/${entry.key.multiverseid}/removeCard" method="post">
							<input name="amount" type="number" value="1" style="width: 100%">
							<button type="submit">remove</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<%--display card's image--%>
	<div hidden id="image" class="popover-card popover fade right in"
		 style=" display: none;">
		<div class="arrow" style="top: 50%;"></div>
		<div class="popover-inner">
			<div class="popover-content"><img id="image-url" src="" height="310" width="223"
											  style="padding:0;margin:0;"></div>
		</div>
	</div>


	<c:forEach var="entry" items="${mapOfComparingResult}">
		<br>
		Deck Name: <a href="${entry.key.deckUrl}">${entry.key.name}</a>
		matches:<c:out value="${entry.value}"/>
		<br>
	</c:forEach>
	<c:out value="${message}"/>

	<script type="text/javascript">
        $(document).ready(function () {

            //shows card image on mouse focus
            $('.card-name').hover(
                function () {
                    var image_url = $(this).attr('href');
                    var position = $(this).position();
                    var width = $(this).width();
                    $('#image-url').attr('src', image_url);
//                  replace positions values with constants
                    $('#image').css({left: position.left + width + 5, top: position.top - 158});
                    $('#image').fadeIn();
                }, function () {
                    $('#image').fadeOut();
                }
            );
        });

	</script>

</div>
