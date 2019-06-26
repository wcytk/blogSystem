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
    <title>Document</title>
    <script src="../js/jquery/2.0.0/jquery.min.js"></script>
    <link href="../css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="../js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/article.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.2/css/bootstrap3/bootstrap-switch.min.css" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.2/js/bootstrap-switch.min.js"></script>
    <link rel="stylesheet" type="text/javascript" href="some_.js">
    <script type="text/javascript">
        $.fn.bootstrapSwitch.defaults.size = 'large';
        $.fn.bootstrapSwitch.defaults.onColor = 'success';
        $(function(){
            $(".checkbox").bootstrapSwitch({
                onColor : "success",
                offColor : "info",
                size : "mini",
                handleWidth:"20px",
            });
        })
        $(function(){
            $("#checkall").click(function(){
                $(":checkbox[name='checkbox']").prop("checked",this.checked);
            })
        })
    </script>
</head>
<body>
<div class="body">
    <div class="header">
        <p>稳妥（WebTop）</p>
        <div class="middle_left">
            <div class="middle_left1">
                <ul class="nav nav-pills nav-stacked " style="width:150px">
                    <li role="presentation"><a href="Administrator.html">管理员管理</a></li>
                    <li role="presentation"><a href="/admin/admin_listAll">用户管理</a></li>
                    <li role="presentation" class="active"><a href="/admin/articles">文章管理</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="middle_right">
        <div class="header_">
            <div class="sousuo">
                <div class="input-group">
                    <form action="/admin/admin_search" method="post">
                        <input type="text" class="form-control" placeholder="请输入搜索关键词">
                        <span class="input-group-btn">
        <button class="btn btn-default" type="button">搜索</button>
      </span>
                    </form>
                </div>
            </div>
            <div class="part">
                <span class="label label-info">已审核</span>
                <span class="label label-primary">666</span>&emsp;&emsp;
                <span class="label label-info">待审核</span>
                <span class="label label-primary">666</span>
                <button type="button" class="btn btn-danger" style="margin-left:330px;font-size:15px" onclick="del()">批量删除</button>
            </div>
        </div>
        <table class="table table-hover" name='user_massage' text-aligin="center">
            <thead>
            <th><input type="checkbox" style="square" name="checkall" value="" id="checkall"/></th>
            <th>序号</th>
            <th>用户编号</th>
            <th style="width: 20%">文章标题</th>
            <th>内容</th>
            <th>发布时间</th>
            <th colspan="2">发布状态</th>
            <th>操作</th>
            </thead>
            <c:forEach var="article" items="${articleList}">
                <tbody class="content">
                <tr>
                    <td>
                        <input type="checkbox" style="square" name="checkbox" value="1"/>
                    </td>

                    <td>${article.user_id}</td>
                    <td>${article.title}</td>
                    <td>${article.content}</td>
                    <td>${article.collectNum}</td>
                    <td>${article.publishTime}</td>
                    <td>${article.updateTime}</td>
                    <td>待审核</td>
                    <td>
                        <button type="button" class="btn btn-warning" style="font-size:8px">审核</button>
                    </td>
                    <td><a href="/admin/deleteArticle?id=${article.id}">删除</a></td>
                    <td><a href="../admin/updateArticle.jsp?id=${article.id}&user_id=${article.user_id}&title=${article.title}&content=${article.content}&thumbNum=${article.thumbNum}&collectNum=${article.collectNum}">修改</a></td>
                </tr>
                <td>
                    <button type="button" class="btn btn-warning" style="font-size:8px" >审核</button>
                </td>
                <td>
                    <input type="checkbox" class="checkbox" value=""/>
                </td>
                <td>
                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModal" style="font-size:8px "  >删除</button>
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button data-dismiss="modal" class="close" type="button"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                                    <h4 class="modal-title">确定要删除这篇文章吗？</h4>
                                </div>
                                <div class="modal-footer">
                                    <button data-dismiss="modal" class="btn btn-default" type="button">取消</button>
                                    <button class="btn btn-danger" type="submit">删除</button>

                                </div>
                            </div>
                        </div>
                    </div>
                </td>
                </tr>
                </tbody>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>