package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Comment;
import com.javapandeng.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
    @Autowired
    private CommentServiceImpl commentService;

    //添加評價(用戶端)
    @RequestMapping("/exAdd")
    public String exAdd(Comment comment, HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer itemId = (Integer) session.getAttribute("itemId");
        Integer userId = (Integer) session.getAttribute("userId");

        comment.setItemId(itemId);
        comment.setUserId(userId);

        Date date=new Date();
        comment.setAddTime(date);

        commentService.insert(comment);

        return "login/uIndex";
    }
}
