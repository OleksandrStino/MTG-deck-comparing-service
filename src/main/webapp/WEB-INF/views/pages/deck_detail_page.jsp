<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="mtg" uri="http://www.mtg.com/tags"%>

<div class="container">

	<a href="/decks/${deck.id}/compareDeck">Compare</a>
	
	<a href="/decks/${deck.id}/bulkAdd">Bulk add</a>

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
				<th class="text-center" width="15%">name</th>
				<th class="text-center" width="5%">set</th>
				<th class="text-center" width="10%">type</th>
				<th class="text-center" width="10%">mana cost</th>
				<th class="text-center" width="50%">text</th>
				<th class="text-center" width="10%">add</th>

			</tr>
			<c:forEach var="element" items="${cardList}">
				<tr style="font-size: 80%">
					<td class="text-center">
						<a class="card-name" href="${element.imageUrl}"><c:out value="${element.name}"/></a>
					</td>
					<td class="text-center"><i
							class="ss ss-${element.set.concat(" ss-").concat(element.rarity)}  ss-grad"
							style="font-size: 2em;" title="${element.setName}"></i></td>
					<td class="text-center"><c:out value="${element.type}"/></td>
					<td class="mana-cost text-center">
						${mtg:parseManaCost(element.manaCost)}
					</td>
					<td><c:out value="${element.text}"/></td>
					<td class="col-sm-12">
						<form action="/decks/${deck.id}/addCardFromList" class="form-inline" method="post">
							<div class="form-group col-sm-7">
								<input hidden name="cardName" type="hidden" value="${element.name}"
									   class="form-control form-text"/>
								<input name="set" type="hidden" value="${element.setName}"
									   class="form-control form-text"/>
								<input name="amount" value="1" type="text" class="form-control" style="width: 50px">
							</div>

							<button type="submit" class="btn btn-default col-sm-5" style="width: 35px"><span
									class="glyphicon glyphicon-plus" style="font-size: 1.4em;"></span>
							</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<h3>Cards in deck:</h3>
	<c:if test="${not empty deck.cards}">
		<table style="width: 100%" id="cards-in-deck" class="table table-bordered">
			<thead>
			<tr>
				<th class="text-center" width="15%">name</th>
				<th class="text-center" width="5%">set</th>
				<th class="text-center" width="10%">type</th>
				<th class="text-center" width="10%">mana cost</th>
				<th class="text-center" width="50%">text</th>
				<th class="text-center" width="10%">remove</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="entry" items="${deck.cards}">

				<tr class="paginate" style="font-size: 80%">
					<td class="text-center">
							<a class="card-name" href="${entry.key.imageUrl}"><c:out
							value="${entry.key.name}"/></a> X ${entry.value}
					</td>
					<td class="text-center"><i
							class="ss ss-${entry.key.set.concat(" ss-").concat(entry.key.rarity)}  ss-grad"
							style="font-size: 2em;" title="${entry.key.setName}"></i></td>
					<td class="text-center"><c:out value="${entry.key.type}"/></td>
					<td class="mana-cost text-center">
						${mtg:parseManaCost(entry.key.manaCost)}
					</td>
					<td><c:out value="${entry.key.text}"/></td>
					<td>
						<form action="/decks/${deck.id}/${entry.key.multiverseid}/removeCard" class="form-inline"
							  method="post">
							<div class="form-group col-sm-7">
								<input name="amount" type="number" class="form-control" value="1" style="width: 50px">
							</div>
							<button class="btn btn-default col-sm-5" type="submit" style="width: 35px"><span
									class="glyphicon glyphicon-trash" style="font-size: 1.4em;"></span></button>
						</form>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div id="page-nav"></div>
	</c:if>

	<%--<script type="text/javascript">

        $(document).ready( function () {
            $('#cards-in-deck').DataTable({
                fixedColumns: true
            });
        } );

	</script>--%>



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
		Format:<c:out value="${entry.key.format}"/>
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
                    $('#image').fadeIn(0);
                }, function () {
                    $('#image').fadeOut(0);
                    $('#image-url').attr('src', "");
                }
            );

        });

	</script>

</div>
