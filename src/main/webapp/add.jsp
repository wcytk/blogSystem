<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 2018/11/12
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.bootcss.com/jquery/3.0.0/jquery.js"></script>
    <title>Title</title>
    <script>
        $(function () {
            $("#insertBtn").click(function () {
                var username = $('#username').val();
                var password = $('#password').val();
                var data = {
                    data: JSON.stringify({
                        'username': username,
                        'password': password
                    }),
                }
                $.ajax({
                    url: '/insert',
                    type: 'POST',
                    data: data,
                    dataType: 'json',
                    success:function (data) {
                        alert(data.result);
                    },
                    error:function (er) {
                        alert(er);
                    }
                })
            })
        })
    </script>

    <script type="text/javascript">
        function yanzheng(){
            // var email=$('#email').val();
            if(confirm("确定要发送吗？")) {
                $.ajax({
                    url : "/youxiang",
                    type: "POST",
                    dataType: "json",
                    // contentType : "application/json",
                    data: {
                        "email":$("input[name='email']").val()
                    },
                    success:function (data) {
                        // alert("发送成功");
                    }
                });

            }
        }
    </script>
</head>
<body>
<form action="/insert" method="post">
    用户名：<input type="text" name="username">
    密码：<input type="password" name="password">
    邮箱：<input type="text" name="email">
    <input type="button" id="yanzhengma" value="发送验证码" onclick="yanzheng()">
    <input type="text" name="identifyingCode" placeholder="验证码"/><br>
    <input id="insertBtn" type="submit" value="提交">

</form>

</body>
</html>
