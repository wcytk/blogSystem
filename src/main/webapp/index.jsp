<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.bootcss.com/jquery/3.0.0/jquery.js"></script>
    <title>登录页面</title>

    <script>

        $(function () {
            $("#showButton").click(function () {
                $.ajax({
                    type: 'get',
                    url: '/ListAll1',
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

<input type="button" value="显示全部数据" id="showButton"/>

<table class="table"> 
    <tr>
        <th>id</th>
        <th>username</th>
        <th>password</th>
    </tr>
</table>


<a href="/logout" >退出登录</a>

</body>
</html>
