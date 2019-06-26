<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 2018/11/30
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <script src="../js/jquery/2.0.0/jquery.min.js"></script>
    <link href="../css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="../js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/people.css">
</head>
<body>
<div class="body">
    <div class="header">
        <p>稳妥（WebTop）</p>
    </div>
    <div class="middle_left">
        <div class="middle_left1">
            <ul class="nav nav-pills nav-stacked " style="width:150px">
                <li role="presentation" ><a href="">管理员管理</a></li>
                <li role="presentation" class="active"><a href="/admin/admin_listAll">用户管理</a></li>
                <li role="presentation"><a href="/admin/articles">文章管理</a></li>
            </ul>
        </div>
    </div>
    <div class="middle_right">
        <div class="middle_right1">
            <form action="/admin/updateUser?id=<%=request.getParameter("id")%>" method="post">
                <table class="table table-hover" name='user_massage'>
                    <thead style="text-align: center">
                    <th>用户名</th>
                    <th>密码</th>
                    <th>邮箱</th>
                    <th>操作</th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <input type="text" name="username" value="<%=request.getParameter("email")%>" />
                        </td>
                        <td>
                            <input type="password" name="password" value="<%=request.getParameter("email")%>" />
                        </td>
                        <td>
                            <input type="text" name="email" value="<%=request.getParameter("email")%>" />
                        </td>
                        <td>
                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal" style="font-size:13px "  type="button">保存修改</button>
                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button data-dismiss="modal" class="close" type="button"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                                            <h4 class="modal-title">确定要修改信息吗？</h4>
                                        </div>
                                        <div class="modal-footer">
                                            <button data-dismiss="modal" class="btn btn-default" type="button" id="xiugaibtn">取消</button>
                                            <button class="btn btn-primary" type="submit">保存</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
<%--<form action="/admin_updateUser" method="post">
    <table>
        <tr>
            <td>学生姓名</td>
            <td>密码</td>
            <td>邮箱</td>
            <td colspan="4" align="center">操作</td>
        </tr>
        &lt;%&ndash;<td><input type="text" name="id"></td>&ndash;%&gt;
        <td><input type="text" name="username"/></td>
        <td><input type="text" name="password"/></td>
        <td><input type="text" name="email"></td>
        <td><input type="submit" value="保存修改" id="xiugaibtn"></td>
        <<input type="submit" value="tijiao">
        </tr>
    </table>
</form>--%>
<script>
    $(function () {
        $("#xiugaibtn").click(function () {
            var id = $('#id').val();
            var username = $('#username').val();
            var password = $('#password').val();
            var data = {
                data: JSON.stringify({
                    'id': id, 'username': username, 'password': password
                })
            };
            $.ajax({
                url: "/admin/updateUser",
                type: 'POST',
                data: data,
                dataType: 'json'
            })
        })
    })
</script>
</body>
</html>
