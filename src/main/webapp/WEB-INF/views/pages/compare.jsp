<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="container">

	COMPARE PAGE
	<%-- <h1>${deck.name} page</h1> --%>
	<table>
		<thead>
			<td>Deck name</td>
			<td>Format</td>
			<td>Matches</td>
		</thead>
		<c:forEach var="entry" items="${mapOfComparingResult}">
			<tr>
				<td><a href="${entry.key.deckUrl}">${entry.key.name}</a></td>
				<td><c:out value="${entry.key.format}" /></td>
				<td><c:out value="${entry.value}" /></td>
			</tr>
		</c:forEach>
	</table>
	<c:out value="${message}" />

	<script type="text/javascript">
		$(document).ready(function() {

			//shows card image on mouse focus
			$('.card-name').hover(function() {
				var image_url = $(this).attr('href');
				var position = $(this).position();
				var width = $(this).width();
				$('#image-url').attr('src', image_url);
				//                  replace positions values with constants
				$('#image').css({
					left : position.left + width + 5,
					top : position.top - 158
				});
				$('#image').fadeIn(10);
			}, function() {
				$('#image').fadeOut(10);
			});

		});
	</script>

	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							$('.mana-cost')
									.each(
											function(index) {
												var message = $.trim($(this)
														.find('p').html());
												var array = message.split(" ");
												if (array.length == 1
														&& $.trim(array[0])
																.split("/").length == 1) {
													$(this)
															.append(
																	'<i class="ms ms-'
																			+ $
																					.trim(array[0])
																			+ '" style="font-size: 1.4em;"></i>');
												} else {
													for (i = 0; i < array.length; i++) {
														if (array[i].split("/").length == 2) {
															var splited = array[i]
																	.split('/');
															$(this)
																	.append(
																			'<i class="ms ms-' + splited[0] + splited[1] + ' + ms-split ms-cost" style="font-size: 1.4em;"> </i>');
														}
														if (!isBlank(array[i])
																&& array[i]
																		.split("/").length == 1) {
															$(this)
																	.append(
																			'<i class="ms ms-' + array[i] + '" style="font-size: 1.4em;"></i>');
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
