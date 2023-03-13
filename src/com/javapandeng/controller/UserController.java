package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.User;
import com.javapandeng.service.impl.UserServiceImpl;
import com.javapandeng.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserServiceImpl userService;

    //分頁查詢用戶列表
    @RequestMapping("/findBySql")
    public String findBySql(Model model, User user){
        String username=user.getUsername();
        String sql;

        if(username == null || username.equals("")){
            sql="select * from user order by id";
        }else{
            sql="select * from user where username like '%"+username+"%' order by id";
        }

        Pager<User> pagers = userService.findBySqlReturnEntity(sql);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",user);

        return "user/user";
    }

    //跳轉至個人中心(用戶端)
    @RequestMapping("/view")
    public String view(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userService.getById(userId);

        if(userId != null){
            //用戶已登錄，跳轉至個人中心
            model.addAttribute("obj",user);
            return "user/view";
        }
        //用戶未登錄，跳轉至註冊頁面
        return "/login/res";
    }

    //修改個人信息(用戶端)
    @RequestMapping("/exUpdate")
    public String exUpdate(HttpServletRequest request,User user){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        User oldUser = userService.getById(userId);

        user.setId(oldUser.getId());
        user.setUsername(oldUser.getUsername());
        user.setPassword(oldUser.getPassword());

        userService.updateById(user);

        return "redirect:/user/view.action";
    }
}
