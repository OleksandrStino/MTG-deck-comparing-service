<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
							style="font-size: 2em;"></i></td>
					<td class="text-center"><c:out value="${element.type}"/></td>
					<td class="mana-cost text-center">
						<p hidden>${element.manaCost}</p>
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
		<table style="width: 100%" class="table table-bordered">
			<tr>
				<th class="text-center" width="15%">name</th>
				<th class="text-center" width="5%">set</th>
				<th class="text-center" width="10%">type</th>
				<th class="text-center" width="10%">mana cost</th>
				<th class="text-center" width="50%">text</th>
				<th class="text-center" width="10%">remove</th>
			</tr>
			<c:forEach var="entry" items="${deck.cards}">

				<tr style="font-size: 80%">
					<td class="text-center">
							${entry.value} X <a class="card-name" href="${entry.key.imageUrl}"><c:out
							value="${entry.key.name}"/></a>
					</td>
					<td class="text-center"><i
							class="ss ss-${entry.key.set.concat(" ss-").concat(entry.key.rarity)}  ss-grad"
							style="font-size: 2em;"></i></td>
					<td class="text-center"><c:out value="${entry.key.type}"/></td>
					<td class="mana-cost text-center">
						<p hidden>${entry.key.manaCost}</p>
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
                    $('#image').fadeIn(10);
                }, function () {
                    $('#image').fadeOut(10);
                }
            );

        });

	</script>

	<script type="text/javascript">
        $(document).ready(function () {
            $('.mana-cost').each(function (index) {
                var message = $.trim($(this).find('p').html());
                var array = message.split(" ");
                if (array.length == 1 && $.trim(array[0]).split("/").length == 1) {
                    $(this).append('<i class="ms ms-' + $.trim(array[0]) + '" style="font-size: 1.4em;"></i>');
                }
                else {
                    for (i = 0; i < array.length; i++) {
                        if (array[i].split("/").length == 2) {
                            var splited = array[i].split('/');
                            $(this).append('<i class="ms ms-' + splited[0] + splited[1] + ' + ms-split ms-cost" style="font-size: 1.4em;"> </i>');
                        }
                        if (!isBlank(array[i]) && array[i].split("/").length == 1) {
                            $(this).append('<i class="ms ms-' + array[i] + '" style="font-size: 1.4em;"></i>');
                        }
                    }
                }
            });
        });

        function isBlank(str) {
            return (!str || /^\s*$/.test(str));
        }
	</script>
</div>
