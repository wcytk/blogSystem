<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 2019/5/12
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.bootcss.com/jquery/3.0.0/jquery.js"></script>
    <title>用户详细信息页面</title>

    <script>
        // function showDetail(){
        //     alert("kaishi");
        //     if (confirm("确定要查看用户详细信息嘛？")){
        //         $.ajax({
        //             type: 'post',
        //             url: '/user/showDetail.do',
        //             dataType: "json",
        //             // data:{id:$("#id").val()},
        //             success: function (data) {
        //                 alert("heng ");
        //                 alert(data.data.headImage)
        //             },
        //             error: function (data) {
        //                 alert("error")
        //             }
        //         });
        //     }
        //
        // }
        function showDetail() {
            $.ajax({
                type: 'post',
                url: '/blogSystem/user/showDetail.do',
                dataType: "json",
                // data: {
                //     detailId: $("#detailId").val(),
                //     neckname: $("#neckname").val(),
                //     sex: $("#sex").val(),
                //     birthday: $("#birthday").val(),
                //     place: $("#place").val(),
                //     personal_sign: $("#personal_sign").val()
                // },
                success: function (data) {
                    alert(data)
                },
                error: function (data) {
                    alert("error")
                }
            });
        }

        function updateDetail() {
            $.ajax({
                type: 'post',
                url: '/blogSystem/user/user_info_update.do',
                dataType: "json",
                data: {detailId: $("#id").val()},
                success: function (data) {
                    alert(data)
                },
                error: function (data) {
                    alert("error")
                }
            });
        }
    </script>
</head>
<body>
<form method="post" action="/showDetail.do">
    <input type="text" id="id" name="id"/>
    <input type="button" value="查看用户个人信息" onclick="showDetail()">
</form>
<form action="/user/user_info_update.do" method="post">
    detailId:<input type="text" name="detailId" id="detailId">
    昵称：<input type="text" name="neckname" id="neckname">
    性别：<input type="text" name="sex" id="sex">
    生日：<input type="text" name="birthday" id="birthday">
    地区：<input type="text" name="place" id="place">
    个性签名：<input type="text" name="personal_sign" id="personal_sign">
    <input type="submit" value="提交" onclick="updateDetail()">
</form>
</body>
</html>
