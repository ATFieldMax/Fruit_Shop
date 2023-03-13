package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.*;
import com.javapandeng.service.impl.EmployeeServiceImpl;
import com.javapandeng.service.impl.ItemCategoryServiceImpl;
import com.javapandeng.service.impl.ItemServiceImpl;
import com.javapandeng.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ItemCategoryServiceImpl itemCategoryService;

    @Autowired
    private ItemServiceImpl itemService;

    //跳轉至員工登錄頁面
    @RequestMapping("/mLogin")
    public String mLogin(){
        return "/login/mLogin";
    }

    //跳轉至用戶登錄頁面
    @RequestMapping("/uLogin")
    //@RequestMapping("login")
    public String uLogin(){
        return "/login/uLogin";
    }

    //員工登錄驗證
    @RequestMapping("/toLogin")
    public String toLogin(Employee employee, HttpServletRequest request){
        HttpSession session= request.getSession();

        //調用業務層方法查詢
        Employee emp = employeeService.getByEntity(employee);
        if(emp != null){
            //登錄成功，將員工id存入Session
            session.setAttribute("empId",emp.getId());
            //進入管理員後台頁面
            return "/login/mIndex";
        }

        //登錄失敗
        return "/login/mLogin";
    }

    //員工登出
    @RequestMapping("mtuichu")
    public String mLogout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("empId");

        return "/login/mLogin";
    }

    //用戶登錄驗證
    @RequestMapping("/utoLogin")
    public String utoLogin(User user, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        //調用業務層方法查詢
        try{
            User u = userService.getByEntity(user);
            if(u != null){
                //登錄成功，將用戶id存入Session
                session.setAttribute("userId",u.getId());

                //顯示歡迎語句(BUG)
                model.addAttribute("userId",u.getId());
                model.addAttribute("username",u.getUsername());

                //已有帳號，進入用戶首頁
                return "redirect:/login/uIndex.action";
            }

            //沒有帳號，進入首頁
            return "redirect:/login/uIndex.action";
        }catch(Exception ex){
            //沒有帳號，進入首頁
            return "redirect:/login/uIndex.action";
        }
    }

    //用戶登出
    @RequestMapping("/uTui")
    public String uLogout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("userId");

        return "/login/uLogin";
    }

    //跳轉至用戶註冊頁面
    @RequestMapping("/res")
    public String toRegister(){
        return "/login/res";
    }

    //用戶註冊
    @RequestMapping("/toRes")
    public String register(User user, HttpServletResponse resp) throws IOException, InterruptedException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();

        //已存在判斷
        String username = user.getUsername();
        String email = user.getEmail();
        String phone = user.getPhone();

        User user2 = userService.getBySqlReturnEntity("select * from user where username=" + username);
        if(user2 != null){
            writer.write("用戶名已存在，請重試!");
            throw new RuntimeException();
        }

        User user3 = userService.getBySqlReturnEntity("select * from user where email='"+email+"'");
        if(user3 != null){
            writer.write("email已存在，請重試!");
            throw new RuntimeException();
        }

        User user4 = userService.getBySqlReturnEntity("select * from user where phone=" + phone);
        if(user4 != null){
            writer.write("手機號已存在，請重試!");
            throw new RuntimeException();
        }

        userService.insert(user);

        return "/login/uLogin";
    }

    //跳轉至首頁(用戶端)
    @RequestMapping("/uIndex")
    public String toIndex(Model model, HttpServletRequest request){
        //分類列表
        List<ItemCategory> itemCategories = itemCategoryService.listBySqlReturnEntity("select * from item_category where isDelete=0");
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        for (ItemCategory itemCategory : itemCategories) {
            CategoryDto categoryDto=new CategoryDto();
            Integer pid = itemCategory.getPid();

            if(pid != null){ //二級類目
                //找出父類設置father屬性
                ItemCategory father = itemCategoryService.getById(pid);
                categoryDto.setFather(father);

                //設置childrens屬性
                List<ItemCategory> childrens = itemCategoryService.listBySqlReturnEntity("select * from item_category where pid="+pid+" and isDelete=0");
                categoryDto.setChildrens(childrens);

                //去除重複數據
                Iterator<CategoryDto> itr = categoryDtoList.iterator();
                while(itr.hasNext()){
                    CategoryDto categoryDto1 = itr.next();
                    if(categoryDto1.getFather().getName().equals(categoryDto.getFather().getName())){
                        System.out.println("數據重複");
                        itr.remove();
                    }
                }
                categoryDtoList.add(categoryDto);
            }
        }

        model.addAttribute("lbs",categoryDtoList);

        //折扣大促銷
        List<Item> zkItems = itemService.listBySqlReturnEntity("select * from item order by zk");
        model.addAttribute("zks",zkItems);

        //熱門商品
        List<Item> gmNumItems = itemService.listBySqlReturnEntity("select * from item order by gmNum desc");
        model.addAttribute("rxs",gmNumItems);

        return "/login/uIndex";
    }

    //跳轉至修改密碼頁面(用戶端)
    @RequestMapping("/pass")
    public String toPass(Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userService.getById(userId);

        model.addAttribute("obj",user);

        return "/login/pass";
    }

    //修改密碼(用戶端)
    @RequestMapping("/upass")
    public String uPass(User user,HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        user.setId(userId);

        userService.updateById(user);
        return "/login/uIndex";
    }

}