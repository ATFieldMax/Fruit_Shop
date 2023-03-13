package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Item;
import com.javapandeng.po.ItemCategory;
import com.javapandeng.service.impl.ItemCategoryServiceImpl;
import com.javapandeng.service.impl.ItemServiceImpl;
import com.javapandeng.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/itemCategory")
public class ItemCategoryController extends BaseController {
    @Autowired
    private ItemCategoryServiceImpl itemCategoryService;

    @Autowired
    private ItemServiceImpl itemService;

    //分頁查詢類別列表
    @RequestMapping("/findBySql")
    public String findBySql(Model model,ItemCategory itemCategory){
        String sql="select * from item_category where isDelete=0 and pid is null order by id";

        Pager<ItemCategory> pagers = itemCategoryService.findBySqlReturnEntity(sql);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",itemCategory);

        return "itemCategory/itemCategory";
    }

    //跳轉至新增一級類別頁面
    @RequestMapping("/add")
    public String toAdd(){
        return "itemCategory/add";
    }

    //新增一級類別
    @RequestMapping("/exAdd")
    public String exAdd(ItemCategory itemCategory){
        itemCategory.setIsDelete(0);

        itemCategoryService.insert(itemCategory);

        return "redirect:/itemCategory/findBySql.action";
    }

    //跳轉至修改一級類別頁面
    @RequestMapping("/update")
    public String update(int id, HttpServletRequest request){
        HttpSession session = request.getSession();
        ItemCategory itemCategory = itemCategoryService.getById(id);
        session.setAttribute("itemCategory",itemCategory);

        return "itemCategory/update";
    }

    //修改一級類別
    @RequestMapping("/exUpdate")
    public String exUpdate(ItemCategory itemCategory,HttpServletRequest request){
        HttpSession session = request.getSession();
        ItemCategory oldItemCategory = (ItemCategory) session.getAttribute("itemCategory");

        oldItemCategory.setName(itemCategory.getName());

        itemCategoryService.updateById(oldItemCategory);

        return "redirect:/itemCategory/findBySql.action";
    }

    //刪除一級類別
    @RequestMapping("/delete")
    public String delete(int id){
        itemCategoryService.deleteById(id);

        String sql = "delete from item_category where pid = "+id;
        itemCategoryService.deleteBySql(sql);

        return "redirect:/itemCategory/findBySql.action";
    }

    //查看二級分類
    @RequestMapping("/findBySql2")
    public String add2(int pid,Model model,ItemCategory itemCategory){
        String sql="select * from item_category where pid="+pid+" and isDelete=0 order by id";

        Pager<ItemCategory> pagers = itemCategoryService.findBySqlReturnEntity(sql);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",itemCategory);

        return "itemCategory/itemCategory2";
    }

    //跳轉至新增二級類別頁面
    @RequestMapping("/add2")
    public String toAdd2(int pid,HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("pid",pid);

        return "itemCategory/add2";
    }

    //新增二級類別
    @RequestMapping("/exAdd2")
    public String exAdd2(ItemCategory itemCategory,HttpServletRequest request){
        HttpSession session = request.getSession();
        int pid = (int) session.getAttribute("pid");

        itemCategory.setPid(pid);
        itemCategory.setIsDelete(0);

        itemCategoryService.insert(itemCategory);

        return "redirect:/itemCategory/findBySql.action";
    }

    //跳轉至修改二級類別頁面
    @RequestMapping("/update2")
    public String update2(int id, HttpServletRequest request){
        HttpSession session = request.getSession();
        ItemCategory itemCategory = itemCategoryService.getById(id);
        session.setAttribute("itemCategory",itemCategory);

        return "itemCategory/update2";
    }

    //修改二級類別
    @RequestMapping("/exUpdate2")
    public String exUpdate2(ItemCategory itemCategory,HttpServletRequest request){
        HttpSession session = request.getSession();
        ItemCategory oldItemCategory = (ItemCategory) session.getAttribute("itemCategory");

        oldItemCategory.setName(itemCategory.getName());

        itemCategoryService.updateById(oldItemCategory);

        return "redirect:/itemCategory/findBySql.action";
    }

    //刪除二級類別
    @RequestMapping("/delete2")
    public String delete2(int id,int pid){ //因id不重複，pid無用
        itemCategoryService.deleteById(id);

        return "redirect:/itemCategory/findBySql.action";
    }

    //顯示統計圖
    @RequestMapping("/tj")
    public String tj(Model model){
        String sql="select * from item_category where isDelete=0 and pid is null order by id desc";

        List<ItemCategory> list = itemCategoryService.listBySqlReturnEntity(sql);
        List<Map<String,Object>> maps=new ArrayList<>();

        if(!CollectionUtils.isEmpty(list)){
            for(ItemCategory category : list){
                int total=0;

                List<Item> items = itemService.listBySqlReturnEntity("select * from item where isDelete=0 and category_id_one=" + category.getId());
                if(!CollectionUtils.isEmpty(items)){
                    for(Item item : items){
                        total+=item.getGmNum();
                    }
                }

            Map<String,Object> map=new HashMap<>();
            map.put("name",category.getName());
            map.put("value",total);

            maps.add(map);
            }
        }

        model.addAttribute("maps",maps);
        return "itemCategory/tj";
    }
}
