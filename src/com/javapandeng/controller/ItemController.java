package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Comment;
import com.javapandeng.po.Item;
import com.javapandeng.po.ItemCategory;
import com.javapandeng.service.impl.CommentServiceImpl;
import com.javapandeng.service.impl.ItemCategoryServiceImpl;
import com.javapandeng.service.impl.ItemServiceImpl;
import com.javapandeng.utils.Pager;
import com.javapandeng.utils.SystemContext;
import com.javapandeng.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController extends BaseController {
    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemCategoryServiceImpl itemCategoryService;

    @Autowired
    private CommentServiceImpl commentService;

    //分頁查詢商品列表
    @RequestMapping("/findBySql")
    public String findBySql(Model model, Item item){
        String name=item.getName();
        String sql;

        if(name == null || name.equals("")){
            sql="select * from item where isDelete=0 order by id";
        }else{
            sql="select * from item where isDelete=0 and name like '%"+name+"%' order by id";
        }

        List<Item> items = itemService.listBySqlReturnEntity(sql);
        for (Item newItem : items) {
            Integer categoryIdOne = newItem.getCategoryIdOne();
            ItemCategory itemCategoryOne = itemCategoryService.getById(categoryIdOne);

            Integer categoryIdTwo = newItem.getCategoryIdTwo();
            ItemCategory itemCategoryTwo = itemCategoryService.getById(categoryIdTwo);

            newItem.setYiji(itemCategoryOne);
            newItem.setErji(itemCategoryTwo);
        }

        Pager<Item> pagers= itemService.createPagerEntity();
        pagers.setDatas(items);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",item);

        return "item/item";
    }

    //跳轉至添加商品頁面
    @RequestMapping("/add")
    public String toAdd(Model model){
        //商品類別下拉式選單
        String sql="select * from item_category where isDelete=0 and pid is not null";

        List<Item> items = itemService.listBySqlReturnEntity(sql);

        model.addAttribute("types",items);

        return "item/add";
    }

    //添加商品
    @RequestMapping("/exAdd")
    public String exAdd(Item item, @RequestParam("file")CommonsMultipartFile[] files) throws IOException {
        //文件上傳
        if(files.length>0){
            for(int i=0;i<files.length;i++){
                String s = UUIDUtils.create();
                String path = SystemContext.getRealPath()+"\\resource\\ueditor\\upload\\"+s+files[i].getOriginalFilename();

                File newFile = new File(path);

                files[i].transferTo(newFile); //將臨時文件轉存到指定位置
                if(i == 0){
                    item.setUrl1("\\test1_war_exploded\\resource\\ueditor\\upload\\"+s+files[i].getOriginalFilename());
                }
                if(i == 1){
                    item.setUrl2("\\test1_war_exploded\\resource\\ueditor\\upload\\"+s+files[i].getOriginalFilename());
                }
                if(i == 2){
                    item.setUrl3("\\test1_war_exploded\\resource\\ueditor\\upload\\"+s+files[i].getOriginalFilename());
                }
                if(i == 3){
                    item.setUrl4("\\test1_war_exploded\\resource\\ueditor\\upload\\"+s+files[i].getOriginalFilename());
                }
                if(i == 4){
                    item.setUrl5("\\test1_war_exploded\\resource\\ueditor\\upload\\"+s+files[i].getOriginalFilename());
                }
            }
        }

        Integer categoryIdTwo = item.getCategoryIdTwo();
        ItemCategory itemCategory = itemCategoryService.getById(categoryIdTwo);
        Integer categoryIdOne = itemCategory.getPid();
        item.setCategoryIdOne(categoryIdOne);

        item.setScNum(0);
        item.setGmNum(0);
        item.setIsDelete(0);

        itemService.insert(item);

        return "redirect:/item/findBySql.action";
    }

    //跳轉至修改商品頁面並回顯商品數據
    @RequestMapping("/update")
    public String update(int id, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        session.setAttribute("updateId",id);

        //商品類別下拉式選單
        String sql="select * from item_category where isDelete=0 and pid is not null";

        List<Item> items = itemService.listBySqlReturnEntity(sql);

        model.addAttribute("types",items);

        //商品數據回顯
        Item item = itemService.getById(id);
        model.addAttribute("obj",item);

        return "item/update";
    }

    //修改商品
    @RequestMapping("/exUpdate")
    public String exUpdate(Item item){

        itemService.updateById(item);

        return "redirect:/item/findBySql.action";
    }

    //商品下架
    @RequestMapping("/delete")
    public String delete(int id){
        Item item = itemService.getById(id);
        System.out.println(item);
        item.setIsDelete(1);
        itemService.updateById(item);

        return "redirect:/item/findBySql.action";
    }

    //按分類查詢商品(用戶端)
    //模糊查詢、頁碼顯示有問題
    @RequestMapping("/shoplist")
    public String toShopList(Item item, Model model, String condition){
        Integer categoryIdTwo = item.getCategoryIdTwo();
        String price = item.getPrice();
        Integer gmNum = item.getGmNum();

        System.out.println(price+","+gmNum);

        //按價格排序
        if("1".equals(price)){
            System.out.println("進入價格排序");
            List<Item> itemList = itemService.listBySqlReturnEntity("select * from item where category_id_two="+categoryIdTwo+" and isDelete=0 order by price desc");

            Pager<Item> pagers = itemService.createPagerEntity();
            pagers.setDatas(itemList);

            model.addAttribute("obj",item);
            model.addAttribute("pagers",pagers);

            return "/item/shoplist";
        }

        //按銷量排序
        if(gmNum != null && gmNum == 1){
            System.out.println("進入銷量排序");
            List<Item> itemList = itemService.listBySqlReturnEntity("select * from item where category_id_two="+categoryIdTwo+" and isDelete=0 order by gmNum desc");

            Pager<Item> pagers = itemService.createPagerEntity();
            pagers.setDatas(itemList);

            model.addAttribute("obj",item);
            model.addAttribute("pagers",pagers);

            return "/item/shoplist";
        }

        //模糊查詢
        if(!isEmpty(condition)){
            System.out.println("進入模糊查詢");
            List<Item> itemList = itemService.listBySqlReturnEntity("select * from item where name like '%"+condition+"%' and isDelete=0");

            Pager<Item> pagers = itemService.createPagerEntity();
            pagers.setDatas(itemList);

            model.addAttribute("obj",item);
            model.addAttribute("pagers",pagers);

            return "/item/shoplist";
        }

        //默認排序
        List<Item> itemList = itemService.listBySqlReturnEntity("select * from item where category_id_two=" + categoryIdTwo+" and isDelete=0");

        Pager<Item> pagers = itemService.createPagerEntity();
        pagers.setDatas(itemList);

        model.addAttribute("obj",item);
        model.addAttribute("pagers",pagers);

        return "/item/shoplist";
    }

    //進入商品頁面(用戶端)
    //商品圖片無法切換
    @RequestMapping("/view")
    public String toItemPage(int id,Model model){
        Item item = itemService.getById(id);

        List<Comment> comments = commentService.listBySqlReturnEntity("select * from comment where item_id=" + id);
        item.setPls(comments);

        model.addAttribute("obj",item);

        return "item/view";
    }
}
