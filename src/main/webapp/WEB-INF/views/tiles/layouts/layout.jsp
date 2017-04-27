<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title><tiles:getAsString name="title"/></title>
		<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"/>
		<link href="<c:url value='/static/css/header.css' />" rel="stylesheet"/>

		<script
				src="https://code.jquery.com/jquery-2.2.4.min.js"
				integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
				crossorigin="anonymous"></script>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
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