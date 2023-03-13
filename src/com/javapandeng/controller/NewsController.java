package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.News;
import com.javapandeng.service.impl.NewsServiceImpl;
import com.javapandeng.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
    @Autowired
    private NewsServiceImpl newsService;

    //分頁查詢公告列表
    @RequestMapping("/findBySql")
    public String findBySql(Model model, News news){
        String name=news.getName();
        String sql;

        if(name == null || name.equals("")){
            sql="select * from news order by id";
        }else{
            sql="select * from news where name like '%"+name+"%' order by id";
        }

        Pager<News> pagers = newsService.findBySqlReturnEntity(sql);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",news);

        return "news/news";
    }

    //跳轉至添加頁面
    @RequestMapping("/add")
    public String toAdd(){
        return "news/add";
    }

    //添加公告
    @RequestMapping("/exAdd")
    public String exAdd(News news){
        Date date=new Date();
        news.setAddTime(date);

        newsService.insert(news);

        return "redirect:/news/findBySql.action";
    }

    //跳轉至修改頁面並回顯公告數據
    @RequestMapping("/update")
    public String update(int id, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("updateId",id);

        //公告數據回顯
        News news = newsService.getById(id);
        model.addAttribute("obj",news);

        return "/news/update";
    }

    //修改公告
    @RequestMapping("/exUpdate")
    public String exUpdate(News news){
        Date date=new Date();
        news.setAddTime(date);

        newsService.updateById(news);

        return "redirect:/news/findBySql.action";
    }

    //刪除公告
    @RequestMapping("/delete")
    public String delete(int id){
        newsService.deleteById(id);

        return "redirect:/news/findBySql.action";
    }

    //公告列表(用戶端)
    @RequestMapping("/list")
    public String list(Model model){
        String sql="select * from news order by id";

        Pager<News> pagers = newsService.findBySqlReturnEntity(sql);

        model.addAttribute("pagers",pagers);

        return "/news/list";
    }

    //查看公告(用戶端)
    @RequestMapping("/view")
    public String view(int id,Model model){
        News news = newsService.getById(id);

        model.addAttribute("obj",news);

        return "/news/view";
    }
}
