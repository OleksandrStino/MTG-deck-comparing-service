<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <p>This is the homepage!</p>
        <h1>${key}</h1>
        card: ${card}
            <br>
        image: ${image}
            <br>
        text: ${text}
    <br>
		image url: ${imageUrl}<br>
        cards size: ${cards.size()}<br>
        <c:forEach items="${cards}" var="url">
            url: ${url}
			<img src="${url}"><br>
        </c:forEach>
    </body>
</html>
