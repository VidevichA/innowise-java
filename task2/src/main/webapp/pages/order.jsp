<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<style>
		body {
				display: flex;
				justify-content: center;
				align-items: center;
			}
		div {
			display: flex;
			flex-direction: column	;
		}
		h2 {
			margin-top: 30px;
		}
		ul {
			padding: 0;
			line-height: 30px;
		}
		li {
			list-style-type: none;
		}
	</style>
</head>
<body>
	<div>
		<h2>Dear ${sessionScope.name}, your order:</h2>
		<ul>
			<c:forEach items="${sessionScope.products}" var="product" varStatus="status">
				<li>${status.index + 1}) ${product}</li>
			</c:forEach>
		</ul>
		<div>Total: $ ${sessionScope.total}</div>
	</div>
</body>
</html>