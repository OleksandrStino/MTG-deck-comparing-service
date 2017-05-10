//Pagination for table with car's search result
jQuery(function($) {
    // Grab whatever we need to paginate
    var pageParts = $(".found-card");
    // How many parts do we have?
    var numPages = pageParts.length;
    // How many parts do we want per page?
    var perPage = 5;

    // When the document loads we're on page 1
    // So to start with... hide everything else
    pageParts.slice(perPage).hide();
    // Apply simplePagination to our placeholder
    $("#page-nav-found-cards").pagination({
        items: numPages,
        itemsOnPage: perPage,
        cssStyle: "light-theme",
        // We implement the actual pagination
        //   in this next function. It runs on
        //   the event that a user changes page
        onPageClick: function(pageNum) {
            // Which page parts do we show?
            var start = perPage * (pageNum - 1);
            var end = start + perPage;

            // First hide all page parts
            // Then show those just for our page
            pageParts.hide()
                .slice(start, end).show();
        }
    });
});

//Pagination for table with cars in deck
jQuery(function($) {
    // Grab whatever we need to paginate
    var pageParts = $(".paginate");

    // How many parts do we have?
    var numPages = pageParts.length;
    // How many parts do we want per page?
    var perPage = 20;

    // When the document loads we're on page 1
    // So to start with... hide everything else
    pageParts.slice(perPage).hide();
    // Apply simplePagination to our placeholder
    $("#page-nav").pagination({
        items: numPages,
        itemsOnPage: perPage,
        cssStyle: "light-theme",
        // We implement the actual pagination
        //   in this next function. It runs on
        //   the event that a user changes page
        onPageClick: function(pageNum) {
            // Which page parts do we show?
            var start = perPage * (pageNum - 1);
            var end = start + perPage;

            // First hide all page parts
            // Then show those just for our page
            pageParts.hide()
                .slice(start, end).show();
        }
    });
});

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