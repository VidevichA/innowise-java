<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

			form {
				margin-top: 30px;
			}

			input[type='text'] {
				border: 2px solid black;
				padding: 5px;
			}

			input[type='submit'] {
				border: 2px solid black;
				margin-top: 10px;
				width: 100%;
				border-radius: 5px;
			}
		</style>
	</head>

	<body>
		<h2>Welcome to Online Shop</h2>
		<form method="post" action="">
			<input type="text" placeholder="Enter your name" id="name" name="name" />
			<br />
			<input type="submit" value="Enter" />
		</form>
	</body>
</html>
