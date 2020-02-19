<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>    
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="/resource/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="/resource/js/jquery-3.2.1/jquery.js" ></script>
<script type="text/javascript" src="/resource/bootstrap-4.3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="/resource/js/jqueryvalidate/jquery.validate.js"></script>
<script type="text/javascript" src="/resource/js/jqueryvalidate/localization/messages_zh.js"></script>


<link rel="stylesheet" href="/resource/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="/resource/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="/resource/kindeditor/plugins/code/prettify.js"></script>
	<script charset="utf-8" src="/resource/kindeditor/kindeditor-all.js"></script>
    <script charset="utf-8" src="/resource/kindeditor/lang/zh-CN.js"></script>



	我的收藏夹
	<form action="flist" method="post">
	<a href="toaddfavorite?"><input type="button" value="添加"></a>
		<table>
			<thead>
	          <tr>
	            <th scope="col">标题</th>
	            <th scope="col">发布时间</th>
	            <th scope="col">操作</th>
	          </tr>
	        </thead>
	        <c:forEach items="${list}" var="f">
	        	<tr>
	        		<td>${f.text}</td>
	        		<td><ftm:formatDate value="${f.created}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
	        		<td>
	        			<a href="delfavorite?id=${f.id}"><input type="button" value="删除"></a>
	        		</td>
	        	</tr>
	        </c:forEach>
	        <tr>
	        	<td colspan="20">
	        		<!-- <button name="page" value="1">首页</button> -->
	        		<%-- <button name="page" value="${page.prePage==0?1:page.prePage}">下页</button> --%>
	        		当前页：总页数：${page.pageNum}/${page.pages}
	        	</td>
	        </tr>
	       
		</table>
	</form>
</body>
</html>