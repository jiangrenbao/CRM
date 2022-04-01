<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //给登录按钮添加单击事件
            //document.getElementById("loginBtn") //javascript
            //jquery,效率高
            $("#loginBtn").click(function () {
                //alert("click");
                //获取用户名和密码
                var loginAct = $.trim($("#loginAct").val());
                //alert(loginAct.length);
                var loginPwd = $.trim($("#loginPwd").val());
                // alert(loginAct);
                // alert(loginPwd);
                var isRemPwd = $("#isRemPwd").prop("checked");
                // alert(isRemPwd);

                //表单验证
                if (loginAct == "") {
                    alert("用户名不可以为空");
                    return;
                }

                if (loginPwd == "") {
                    alert("密码不可以为空");
                    return;
                }

                $.ajax({
                    url: 'settings/qx/user/login.do',
                    data: {
                        loginAct: loginAct,
                        loginPwd: loginPwd,
                        isRemPwd: isRemPwd
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {  //data是服务器返回前端数据 data就是returnObject json对象
                        // alert(data.code);
                        if (data.code == "1") {
                            //跳转到业务主业务
                            window.location.href = "workbench/index.do";
                        } else {
                            //alert(data.message);
                            $("#msg").text(data.message);
                        }
                    }
                })


            });
        });
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2012&nbsp;meiming</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.html" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" id="loginAct" value="${cookie.loginAct.value}" type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" id="loginPwd" value="${cookie.loginPwd.value}" type="password"
                           placeholder="密码">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">
                    <label>
                        <c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
                            <input type="checkbox" id="isRemPwd" checked="true">
                        </c:if>
                        <c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
                            <input type="checkbox" id="isRemPwd">
                        </c:if>
                        十天内免登录
                    </label>
                    &nbsp;&nbsp;
                    <span id="msg" style="color: red"></span>
                </div>
                <button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>