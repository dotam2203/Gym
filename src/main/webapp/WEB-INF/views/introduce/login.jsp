<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>ĐĂNG NHẬP</title>

<!-- Font Icon -->
<link rel="stylesheet"
	href="<c:url value='/resources/css/icons/material-design-iconic-font/css/material-design-iconic-font.min.css'/>">

<!-- Main css -->
<link rel="stylesheet"
	href="<c:url value='/resources/css/style_sign.css'/>">
</head>
<body>

	<!-- <div class="main"> -->

	<!-- Sing in  Form -->
	<section class="sign-in">
		<div class="container">
			<div class="signin-content">
				<div class="signin-image">
						<figure>
							<img src="<c:url value='/resources/img/signin-image.jpg'/>"
								alt="sing up image">
						</figure>
					
					
					<!-- <center><a href="http://localhost:2203/GymManager/home"><u>Thoát</u></a></center> -->
				</div>
				
				<div class="signin-form">
					
					<h2 class="form-title" style="text-align: center;">Đăng Nhập</h2>
					<form action="login" method="POST" class="register-form"
						id="login-form">

						<div class="form-group">
							<label for="your_name"><i
								class="zmdi zmdi-account material-icons-name"></i></label> <input
								type="text" name="username" id="username"
								placeholder="Your Name" />
						</div>
						<div class="form-group">
							<label for="your_pass"><i class="zmdi zmdi-lock"></i></label> <input
								type="password" name="password" id="password"
								placeholder="Password" />
						</div>
						<h4 style="color: red">${error}</h4>
						<div class="form-group">
							<input type="checkbox" name="remember-me" id="remember-me"
								class="agree-term" /> <label for="remember-me"
								class="label-agree-term"><span><span></span></span>Remember
								me</label>
						</div>
						<div class="form-group form-button" style="text-align: center;">
							<input type="submit" name="signin" id="signin"
								class="form-submit" value="Log in" />
						</div>
					</form>
					<br>
					<form action="home">
						<a href="#" class="signup-image-link">
							<button
								style="border: none; background-color: transparent; outline: none;">
								<u>Thoát</u>
							</button>
						</a>
					</form>
					
					<div class="social-login">
						<span class="social-label">Or login with</span>
						<ul class="socials">
							<li><a href="#"><i
									class="display-flex-center zmdi zmdi-facebook"></i></a></li>
							<li><a href="#"><i
									class="display-flex-center zmdi zmdi-twitter"></i></a></li>
							<li><a href="#"><i
									class="display-flex-center zmdi zmdi-google"></i></a></li>
						</ul>
					</div>
				</div>
			</div>
			</div>
	</section>

	<!-- </div> -->

	<!-- JS -->
	<script src="<c:url value='/resources/js/jquery.min.js'/>"></script>
	<%-- <script src="<c:url value='/resources/js/main.js'/>"></script> --%>
</body>
<!-- This templates was made by Colorlib (https://colorlib.com) -->
</html>