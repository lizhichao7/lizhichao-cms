<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
			<div class="content">
				<div id="large-header" class="">
					<div class="logo_box">
						<h3>欢迎注册CMS系统</h3>
						<form:form modelAttribute="user" max="8" min="2" id="form" action="" method="post" >
						    <div class="form-group">
						    <form:input path="username" 
						     aria-describedby="emailHelp" remote="/user/checkname" class="form-control" placeholder="请输入账户"/><br>
							<form:errors path="username"></form:errors>
							</div>
							
						   <div class="form-group">
						    <form:password  path="password"  aria-describedby="emailHelp" class="form-control" placeholder="请输入密码"/><br>
							<form:errors path="password"></form:errors>
						 <div class="mb2">
						 	<button type="submit" class="btn btn-outline-dark" style="color: #FFFFFF">submit</button><br>
						 	<a href="login"> <font color="#FF0033">已经账号，点击这里直接去登录</font> </a>
						 </div>
						 </div>
						</form:form>
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
						
	<script type="text/javascript">
		$("#form").validate();
		function add(){
			alert('校验开始')
			$("#form").valid();
			alert('校验结束')
		}
	</script>
</body>
</html>