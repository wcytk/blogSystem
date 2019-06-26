<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 2019/5/11
  Time: 20:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.bootcss.com/jquery/3.0.0/jquery.js"></script>
    <title></title>

    <script>
        function submit1(){
            $.ajax({
                type: 'post',
                url: '/blogSystem/user/login.do',
                dataType: "json",
                data:{username:$("#username").val(),password:$("#password").val()},
                success: function (data) {
                    alert(data.code+" "+data.data.username+" "+data.data.email)
                    alert("登录成功，返回一个字符串")
                    // $(window).attr("location","detailMessage.jsp");
                    // showDetail(data.data.id)
                    // window.location.href ="/showDetail?id="+data.data.id;
                },
                error: function (data) {
                    alert("error")
                }
            });
        }

        // function showDetail(data){
        //     $.ajax({
        //         type: 'get',
        //         url: '/showDetail',
        //         dataType: "json",
        //         data: {id:data},
        //         success: function (data) {
        //             alert(data)
        //         },
        //         error: function (data) {
        //             alert("error")
        //         }
        //     });
        // }

        $(function () {
            $("#showButton").click(function () {
                $.ajax({
                    type: 'get',
                    url: '/blogSystem/ListAll1',
                    dataType: "json",
                    success: function (data) {
                        $.each(data, function (i, result) {
                            item = "<tr><td>" + result['id'] + "</td><td>" + result['username'] + "</td><td>" + result['password'] + "</td></tr>";
                            $('.table').append(item);
                        });
                    },
                    error: function (data) {
                        alert("error")
                    }
                });
            })
        })
    </script>
</head>
<body>


    <form method="post">
        <input type="text" name="username" id="username">
        <input type="password" name="password" id="password">
        <input type="button" value="登录" onclick="submit1()">
    </form>

</body>
</html>
