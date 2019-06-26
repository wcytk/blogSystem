<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2019/5/12
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理员</title>
    <link rel="stylesheet" type="text/css" href="../css/people.css">
    <script src="../js/jquery/2.0.0/jquery.min.js"></script>
    <link href="../css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="../js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.2/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.2/js/bootstrap-switch.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $.fn.bootstrapSwitch.defaults.size = 'large';
        $.fn.bootstrapSwitch.defaults.onColor = 'success';
        $(function(){
            $(".checkbox").bootstrapSwitch({
                onColor : "success",
                offColor : "info",
                size : "mini",
                handleWidth:"20",
            });
        })
    </script>
</head>
<body>
<div class="body">
    <div class="header">
        <p>稳妥（WebTop）</p>
    </div>
    <div class="middle_left">
        <div class="middle_left1">
            <ul class="nav nav-pills nav-stacked " style="width:150px">
                <li role="presentation" ><a href="Administrator.html">管理员管理</a></li>
                <li role="presentation" class="active"><a href="/admin/admin_listAll">用户管理</a></li>
                <li role="presentation"><a href="article.html">文章管理</a></li>
            </ul>
        </div>
    </div>
    <!-- </div> -->
    <div class="middle_right">
        <div class="sousuo_1">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="请输入搜索关键词">
                <span class="input-group-btn">
        <button class="btn btn-default" type="button" href="/admin/admin_search">搜索</button>
      </span>
            </div>
        </div>
        <div class="middle_right1">
            <table class="table table-hover" name='user_massage'>
                <thead>
                <th>编号</th>
                <th>用户名</th>
                <th>密码</th>
                <th>邮箱</th>
                <th>上次在线时间</th>
                <th>权限</th>
                </thead>
                <c:forEach var="user" items="${userlist}">
                    <tbody>
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.password}</td>
                        <td>${user.email}</td>
                        <td>2019-4-24(22:00)</td>
                        <td>
                            <input type="checkbox" class="checkbox" checked value=""/>
                        </td>
                    </tr>
                    </tbody>
                </c:forEach>
            </table>
        </div>
    </div>
</div>


</table>
</body>
</html>
