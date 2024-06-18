<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<style>
			h2 {
				margin-top: 30px;
			}

			body {
				display: flex;
				justify-content: center;
				align-items: center;
				flex-direction: column;
			}

			div {
				margin-top: 5px;
			}

			form {
				margin-top: 10px;
			}

			select {
				border: 2px solid black;
				border-radius: 5px;
				width: 100%;
			}

			input[type='submit'] {
				border: 2px solid black;
				margin-top: 10px;
				padding: 0 10px;
				border-radius: 5px;
			}
		</style>
	</head>

	<body>
		<h2>Hello ${sessionScope.name}!</h2>
		<div>Make your order</div>
		<form method="post" action="shop">
			<select id="products" name="products" id="select" class="select" multiple>
				<c:forEach items="${data}" var="product">
        			<option value="${product}">${product}</option>
    			</c:forEach>
			</select>
			<br />
			<input type="submit" value="Submit" />
		</form>
	</body>
</html>
