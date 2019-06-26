<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 2018/11/30
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>修改页面</title>
    <script src="https://cdn.bootcss.com/jquery/3.0.0/jquery.js"></script>
</head>
<body>
<form action="/update" method="post">
    <table>
        <tr>
            <td>学生id</td>
            <td>学生姓名</td>
            <td>密码</td>
            <td colspan="3" align="center">操作</td>
        </tr>
        <td><input type="text" name="id"></td>
        <td><input type="text" name="username"/></td>
        <td><input type="text" name="password"/></td>
        <td><input type="submit" value="保存修改" id="xiugaibtn"></td>
        </tr>
    </table>
</form>
<script>
    $(function () {
        $("#xiugaibtn").click(function () {
            var id = $('#id').val();
            var username = $('#username').val();
            var password = $('#password').val();
            var data = {
                data: JSON.stringify({
                    'id': id, 'username': username, 'password': password,
                }),
            }
            $.ajax({
                url: "/update",
                type: 'POST',
                data: data,
                dataType: 'json',
            })
        })
    })
</script>


<script type="text/javascript">
    function attention(){
        // var email=$('#email').val();
        if(confirm("确定要查看嘛吗？")) {
            $.ajax({
                url : "/getAttentionUser.do",
                type: "POST",
                dataType: "json",
                // contentType : "application/json",
                data: {
                    "userId1":$('#userId1').val()
                },
                success:function (data) {
                     alert("成功");
                     alert(data.msg)
                }
            });

        }
    }
    function DeleteAttention() {
        if(confirm("确定要取消关注嘛？")) {
            $.ajax({
                url : "/deleteAttention",
                type: "POST",
                dataType: "json",
                // contentType : "application/json",
                data: {
                    "attentionUsername":$('#attentionUsername').val()
                },
                success:function (data) {
                    alert("成功");
                    alert(data.status)
                },
                error:function (data) {
                    alert("失败")
                    alert(data.status)
                }
            });
        }
    }
    function touxiang() {
        // alert("ajax upload file");
        var formData = new FormData($('#uploadFileForm')[0]);
        $.ajax({
                type: "POST",
                url: "/blogSystem/user/upload",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                  alert("lalala");
                },
            error:function (data) {
                alert("怎么回事？")
            }
            }
        );
    }


    function setAttention() {
        if(confirm("确定要关注这个用户嘛？")) {
            $.ajax({
                url : "/setAttention",
                type: "POST",
                dataType: "json",
                data: {
                    "userId11":$('#userId11').val(),
                    "userId22":$('#userId22').val()
                },
                success:function (data) {
                    alert("成功");
                    alert(data.msg)
                },
                error:function (data) {
                    alert("失败")
                    alert(data.msg)
                }
            });
        }

    }

</script>
<br><br>
下面是通过输入id,来列出这个id对应用户的关注列表<br>
<input type="text" name="userId1" id="userId1">
<input type="button" id="attention" value="关注列表" onclick="attention()">
<br>
<br>
上面列出了这个id对应用户的关注列表，下面让我们输入一个名字，删除这个用户吧~
<input type="text" name="attentionUsername" id="attentionUsername"/>
<input type="button"id="deleteAttention" value="取消关注" onclick="DeleteAttention()">
<br>
<br>
下面的测试，输入两个id:userid1是当前正在登录的用户，userid2 是要被关注的用户
<form method="post" action="/setAttention">
    <input type="text" id="userId11" name="userId11">
    <input type="text" id="userId22" name="userId22">
    <input type="submit" value="关注" onclick="setAttention()">
</form>
修改用户的头像
<form method="post" action="/user/upload" enctype="multipart/form-data" id="uploadFileForm" >
    <input type="text" name="detailId">
    <input type="file" name="file"><br>
    <input type="button" value="修改头像" onclick="touxiang()">
</form>
</body>
</html>
