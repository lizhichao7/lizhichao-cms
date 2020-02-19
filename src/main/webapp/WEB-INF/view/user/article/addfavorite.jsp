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
<link href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet">
<script type="text/javascript" src="jquery/jquery-1.8.2.js"></script>
<script type="text/javascript" src="jquery/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
	tr:HOVER {
	background-color: #9d9f9e;
}
</style>
</head>
<body>
	<form:form  modelAttribute="favorite" 
	action="addfavorite" method="post" enctype="multipart/form-data">
		标题：<input type="text" name="text"><br><%-- ${article.title} --%>
		地址：<input type="text" name="url"><br><%-- <form:input path="url" />
		    <form:errors path="url" cssStyle="color:red"></form:errors> --%>
		<input type="submit" value="保存">
	</form:form>
</body>
</html>