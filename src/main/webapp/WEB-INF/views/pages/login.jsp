<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container" style="width: 300px;">

	<form method="POST" action="login" class="form-signin">
		<h2 class="form-heading">Log in</h2>

		<div class="form-group ${error != null ? 'has-error' : ''}">
			<span>${message}</span>
			<input name="username" type="text" class="form-control" placeholder="Username"
				   autofocus="true"/>
			<input name="password" type="password" class="form-control" placeholder="Password"/>

			<button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
			<h4 class="text-center"><a href="/register">Create an account</a></h4>
		</div>

	</form>

</div>
