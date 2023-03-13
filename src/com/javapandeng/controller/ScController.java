package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Item;
import com.javapandeng.po.Sc;
import com.javapandeng.service.impl.ItemServiceImpl;
import com.javapandeng.service.impl.ScServiceImpl;
import com.javapandeng.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/sc")
public class ScController extends BaseController {
    @Autowired
    private ScServiceImpl scService;
    @Autowired
    private ItemServiceImpl itemService;

    //分頁查詢收藏列表(用戶端)
    @RequestMapping("/findBySql")
    public String toSc(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        String sql="select * from sc where user_id="+userId;

        List<Sc> scList = scService.listBySqlReturnEntity(sql);
        for (Sc sc : scList) {
            Integer itemId = sc.getItemId();
            Item item = itemService.getById(itemId);
            sc.setItem(item);
        }

        Pager<Sc> pagers = scService.createPagerEntity();
        pagers.setDatas(scList);

        model.addAttribute("pagers",pagers);

        return "sc/my";
    }

    //取消收藏(用戶端)
    @RequestMapping("/delete")
    public String delete(int id){
        //更新item表中的收藏數量
        Sc sc = scService.getById(id);
        Integer itemId = sc.getItemId();
        Item item = itemService.getById(itemId);
        Integer scNum = item.getScNum();
        scNum--;
        item.setScNum(scNum);
        itemService.updateById(item);

        scService.deleteById(id);

        return "redirect:/sc/findBySql.action";
    }

    //加入收藏(用戶端)
    @RequestMapping("/exAdd")
    public void exAdd(int itemId,HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        //判斷是否已收藏此商品
        Sc oldSc = scService.getBySqlReturnEntity("select * from sc where item_id=" + itemId + " and user_id=" + userId);
        if(oldSc != null){
            //商品已收藏
            return;
        }

        Sc sc=new Sc();

        sc.setItemId(itemId);
        sc.setUserId(userId);

        scService.insert(sc);

        //更新item表中的收藏數量
        Item item = itemService.getById(itemId);
        Integer scNum = item.getScNum();
        scNum++;
        item.setScNum(scNum);
        itemService.updateById(item);
    }
}
