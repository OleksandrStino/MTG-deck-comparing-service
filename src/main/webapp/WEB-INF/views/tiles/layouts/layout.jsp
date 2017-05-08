<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><tiles:getAsString name="title"/></title>
	<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>

	<link rel="shortcut icon" href="">

	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
		  integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
		  integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
			integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
			crossorigin="anonymous"></script>
	<link href="<c:url value='/static/css/header.css' />" rel="stylesheet"/>

	<%--set icons--%>
	<link href="//cdn.jsdelivr.net/keyrune/latest/css/keyrune.css" rel="stylesheet" type="text/css"/>

	<%--mana icons--%>
	<link href="/static/css/mana.css" rel="stylesheet" type="text/css"/>

	<%--simple pagination--%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery.simplePagination.js"></script>
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/simplePagination.css"/>




</head>

<body>
<header id="header">
	<tiles:insertAttribute name="header"/>
</header>

<section id="sidemenu">
	<tiles:insertAttribute name="menu"/>
</section>

<section id="site-content">
	<tiles:insertAttribute name="body"/>
</section>

<footer id="footer">
	<tiles:insertAttribute name="footer"/>
</footer>
</body>
</html>