<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>后台首页</title>

   
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="css/content.css" rel="stylesheet">

   
    <script src="js/jquery.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<body>

    <!--路径导航-->
    <ol class="breadcrumb breadcrumb_nav">
        <li class="active">首页</li>
    </ol>
    <!--路径导航-->

    <div class="page-content">
		<div class="panel-heading">${sessionScope.globle_user.name}欢迎回来</div>
    </div>



</body>
</html>