<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">

<head>
<title>Image Tracker-Home</title>

<!--===============================================================================================-->

<link rel="stylesheet" type="text/css"
	th:href="@{/vendor/bootstrap/css/bootstrap.min.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/fonts/font-awesome-4.7.0/css/font-awesome.min.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/fonts/Linearicons-Free-v1.0.0/icon-font.min.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/vendor/animate/animate.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/vendor/css-hamburgers/hamburgers.min.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/vendor/animsition/css/animsition.min.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/vendor/select2/select2.min.css}" />
<link rel="stylesheet" type="text/css"
	th:href="@{/vendor/daterangepicker/daterangepicker.css}" />
<link rel="stylesheet" type="text/css" th:href="@{/css/util.css}" />
<link rel="stylesheet" type="text/css" th:href="@{/css/main.css}" />
</head>
<body>
	<h1 th:inline="text">Hello [[${#httpServletRequest.remoteUser}]]!</h1>
	<form th:action="@{/logout}" method="post">
		<input type="submit" value="Sign Out" />
	</form>


	<div class="limiter">
		${objModel.key}
	</div>
	<!--===============================================================================================-->
	<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
	<!--===============================================================================================-->
	<script src="vendor/animsition/js/animsition.min.js"></script>
	<!--===============================================================================================-->
	<script src="vendor/bootstrap/js/popper.js"></script>
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
	<!--===============================================================================================-->
	<script src="vendor/select2/select2.min.js"></script>
	<!--===============================================================================================-->
	<script src="vendor/daterangepicker/moment.min.js"></script>
	<script src="vendor/daterangepicker/daterangepicker.js"></script>
	<!--===============================================================================================-->
	<script src="vendor/countdowntime/countdowntime.js"></script>
	<!--===============================================================================================-->
	<script src="js/main.js"></script>
</body>

</html>