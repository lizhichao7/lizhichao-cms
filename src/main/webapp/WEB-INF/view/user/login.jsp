<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" class="no-js">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<title>cms-登录</title>
<script type="text/javascript" src="/resource/js/jquery-3.2.1/jquery.js" ></script>
<link href="/resource/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="/resource/bootstrap-4.3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="/resource/js/jqueryvalidate/jquery.validate.js"></script>
<script type="text/javascript" src="/resource/js/jqueryvalidate/localization/messages_zh.js"></script>
<!-- <link rel="stylesheet"  href="/csslogin/normalize.css"/> -->
<!-- <link rel="stylesheet"  href="/csslogin/demo.css" /> -->
<!--必要样式-->
<link rel="stylesheet"  href="/csslogin/component.css" />
</head>
<body background="/resource/images/123.jpeg">
		<div class="container demo-1">
		<div class="row">
		 		${error}
		</div>
			<div class="content">
				<div id="large-header" class="">
					<%-- <canvas id="demo-canvas"></canvas> --%>
					<div class="logo_box">
						<h3>欢迎登录CMS系统</h3>
						<form  modelAttribute="user" max="8" min="2" id="form" action="" method="post" >
							<div class="form-group">
								<input name="username" class="form-control" placeholder="请输入账户">
							</div>
							<div class="form-group">
								<input name="password" class="form-control" type="password" placeholder="请输入密码">
							</div>
							<div class="mb2"><button type="submit" class="btn btn-outline-dark" style="color: #FFFFFF">登录</button></div>
							<a href="register"> <font color="#FF0033"> 尚无账号，点击这里直接去注册</font> </a>
						</form>
					</div>
				</div>
			</div>
		</div><!-- /container -->
		<script src="/jslogin/TweenLite.min.js"></script>
		<script src="/jslogin/EasePack.min.js"></script>
		<script src="/jslogin/rAF.js"></script>
		<script src="/jslogin/demo-1.js"></script>
		<div style="text-align:center;">
		
</div>
	</body>
</html>