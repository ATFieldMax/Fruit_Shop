package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Message;
import com.javapandeng.po.User;
import com.javapandeng.service.impl.MessageServiceImpl;
import com.javapandeng.service.impl.UserServiceImpl;
import com.javapandeng.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {
    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private UserServiceImpl userService;

    //分頁查詢留言列表
    @RequestMapping("/findBySql")
    public String findBySql(Model model, Message message){
        String name=message.getName();
        String sql;

        if(name == null || name.equals("")){
            sql="select * from message order by id";
        }else{
            sql="select * from message where name like '%"+name+"%' order by id";
        }

        Pager<Message> pagers = messageService.findBySqlReturnEntity(sql);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",message);

        return "message/message";
    }

    //刪除留言
    @RequestMapping("/delete")
    public String delete(int id){
        messageService.deleteById(id);

        return "redirect:/message/findBySql.action";
    }

    //跳轉至添加留言頁面(用戶端)
    @RequestMapping("/add")
    public String toAdd(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if(userId != null){
            //用戶已登錄，跳轉至添加留言頁面
            User user = userService.getById(userId);

            model.addAttribute("obj",user);

            return "message/add";
        }
        //用戶未登錄，跳轉至註冊頁面
        return "/login/res";
    }

    //提交留言(用戶端)
    @RequestMapping("/exAdd")
    public String exAdd(Message message) throws IOException , InterruptedException{
        //檢查姓名與手機號是否與個人資料中的內容一致
        String name = message.getName();
        String phone = message.getPhone();

        User user = userService.getBySqlReturnEntity("select * from user where realName='" + name + "'");
        User user1 = userService.getBySqlReturnEntity("select * from user where phone='" + phone + "'");

        if(user == null){
            return "/login/uIndex";
        }
        if(user1 == null){
            return "/login/uIndex";
        }

        messageService.insert(message);
        return "/login/uIndex";
    }
}
