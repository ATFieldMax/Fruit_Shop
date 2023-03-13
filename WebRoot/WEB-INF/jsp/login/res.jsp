<%@page language="java" contentType="text/html; character=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>首页</title>
    <link type="text/css" rel="stylesheet" href="${ctx}/resource/user/css/style.css">
    <script src="${ctx}/resource/user/js/jquery-1.8.3.min.js"></script>
    <script src="${ctx}/resource/user/js/jquery.luara.0.0.1.min.js"></script>
</head>
<body>
<div class="width100 hidden_yh" style="border-top: 1px solid #ddd">
    <div class="width1200 hidden_yh center_yh" style="margin-top: 75px">
        <form action="${ctx}/login/toRes" method="post" id="form">
            <div class="center_yh hidden_yh" style="width: 475px;margin-bottom: 40px;">
                <span style="margin-right: 40px;height: 42px;line-height: 42px;width: 100px;" class="left_yh block_yh tright">用戶名：</span>
                <input type="text" name="username" id="username" placeholder="請輸入用戶名(3-11位英/數)" style="border:1px solid #c9c9c9;width: 292px;height: 42px;font-size: 16px;text-indent: 22px;" class="left_yh">
            </div>
            <div class="center_yh hidden_yh" style="width: 475px;margin-bottom: 40px;">
                <span style="margin-right: 40px;height: 42px;line-height: 42px;width: 100px;" class="left_yh block_yh tright">設置密碼：</span>
                <input type="text" name="password" id="password" placeholder="請輸入密碼(3-11位英/數)" style="border:1px solid #c9c9c9;width: 292px;height: 42px;font-size: 16px;text-indent: 22px;" class="left_yh">
            </div>
            <div class="center_yh hidden_yh" style="width: 475px;margin-bottom: 40px;">
                <span style="margin-right: 40px;height: 42px;line-height: 42px;width: 100px;" class="left_yh block_yh tright">手機號：</span>
                <input type="text" name="phone" id="phone" placeholder="請輸入手機號" style="border:1px solid #c9c9c9;width: 292px;height: 42px;font-size: 16px;text-indent: 22px;" class="left_yh">
            </div>
            <div class="center_yh hidden_yh" style="width: 475px;margin-bottom: 40px;">
                <span style="margin-right: 40px;height: 42px;line-height: 42px;width: 100px;" class="left_yh block_yh tright">email：</span>
                <input type="text" name="email" id="email" placeholder="請輸入email" style="border:1px solid #c9c9c9;width: 292px;height: 42px;font-size: 16px;text-indent: 22px;" class="left_yh">
            </div>
            <p class="font14 c_66" style="text-indent: 500px;margin-top: 30px;">
                <input type="checkbox" id="checkbox">我已閱讀並同意<a href="#" class="red">《會員註冊協議》</a>和<a href="#" class="red">《隱私保護政策》</a>
            </p>
            <input type="submit" value="提交" class="ipt_tj" style="width: 295px;height: 44px;margin-left: 500px;">
        </form>
    </div>
</div>
<%@include file="/common/ufooter.jsp"%>
</body>
<script>
    let form = document.getElementById("form");
    let username = document.getElementById("username")
    let password = document.getElementById("password");
    let phone = document.getElementById("phone");
    let email = document.getElementById("email");
    let checkbox = document.getElementById("checkbox");

    form.onsubmit = function(){
        //非空判斷
        if(username.value === ""){
            alert("請填寫用戶名!")
            return false;
        }
        if(password.value === ""){
            alert("請填寫密碼!");
            return false;
        }

        if(!checkbox.checked){
            alert("請勾選同意!");
            return false;
        }

        var usernameRegex = /^[a-zA-Z0-9]{3,11}$/;
        var passwordRegex = /^[a-zA-Z0-9]{3,11}$/;
        var phoneRegex = /^09[0-9]{8}$/;
        var emailRegex = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;

        //正則判斷
        if(!usernameRegex.test(username.value)){
            alert("用戶名格式錯誤")
            return false;
        }
        if(!passwordRegex.test(password.value)){
            alert("密碼格式錯誤")
            return false;
        }
        if(!phoneRegex.test(phone.value)){
            alert("手機號格式錯誤");
            return false;
        }
        if(!emailRegex.test(email.value)){
            alert("email格式錯誤");
            return false;
        }
    }

</script>
</html>



















