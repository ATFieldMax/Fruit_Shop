package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.*;
import com.javapandeng.service.impl.*;
import com.javapandeng.utils.Pager;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/itemOrder")
public class ItemOrderController extends BaseController {
    @Autowired
    private ItemOrderServiceImpl itemOrderService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private OrderDetailServiceImpl orderDetailService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private CarServiceImpl carService;

    //分頁查詢訂單列表
    @RequestMapping("/findBySql")
    public String findBySql(Model model, ItemOrder itemOrder){
        String code=itemOrder.getCode();
        String sql;

        if(code == null || code.equals("")){
            sql="select * from item_order where isDelete=0 order by id";
        }else{
            sql="select * from item_order where isDelete=0 and code like '%"+code+"%' order by id";
        }

        List<ItemOrder> itemOrders = itemOrderService.listBySqlReturnEntity(sql);
        for (ItemOrder order : itemOrders) {
            Integer userId = order.getUserId();
            User user = userService.getById(userId);
            order.setUser(user);
        }

        Pager<ItemOrder> pagers = itemOrderService.createPagerEntity();
        pagers.setDatas(itemOrders);

        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",itemOrder);

        return "itemOrder/itemOrder";
    }

    //查看我的訂單(用戶端)
    @RequestMapping("/my")
    public String toOrder(HttpServletRequest request, Model model){
        //使用userId查詢訂單
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        //全部訂單
        List<ItemOrder> all = itemOrderService.listBySqlReturnEntity("select * from item_order where user_id=" + userId);
        //已取消
        List<ItemOrder> yqx = itemOrderService.listBySqlReturnEntity("select * from item_order where user_id=" + userId + " and status=1");
        //待收貨
        List<ItemOrder> dsh = itemOrderService.listBySqlReturnEntity("select * from item_order where user_id=" + userId + " and status=2");
        //待發貨
        List<ItemOrder> dfh = itemOrderService.listBySqlReturnEntity("select * from item_order where user_id=" + userId + " and status=0");
        //已收貨
        List<ItemOrder> ysh = itemOrderService.listBySqlReturnEntity("select * from item_order where user_id=" + userId + " and status=3");
        //顯示訂單詳情
        for (ItemOrder itemOrder : ysh) {
            Integer id = itemOrder.getId();
            List<OrderDetail> orderDetails = orderDetailService.listBySqlReturnEntity("select * from order_detail where order_id=" + id);
            for (OrderDetail orderDetail : orderDetails) {
                Integer itemId = orderDetail.getItemId();
                Item item = itemService.getById(itemId);
                orderDetail.setItem(item);
            }

            itemOrder.setDetails(orderDetails);
        }

        model.addAttribute("all",all);
        model.addAttribute("yqx",yqx);
        model.addAttribute("dfh",dfh);
        model.addAttribute("dsh",dsh);
        model.addAttribute("ysh",ysh);

        return "itemOrder/my";
    }

    //取消訂單(用戶端)
    @RequestMapping("/qx")
    public String cancel(int id){
        ItemOrder itemOrder = itemOrderService.getById(id);
        itemOrder.setStatus(1);
        itemOrder.setIsDelete(1);

        //在order_details將商品狀態更改為已退貨
        List<OrderDetail> orderDetails = orderDetailService.listBySqlReturnEntity("select * from order_detail where order_id=" + id);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setStatus(1);
            orderDetailService.updateById(orderDetail);
        }

        itemOrderService.updateById(itemOrder);

        return "redirect:/itemOrder/my.action";
    }

    //去收貨(用戶端)
    @RequestMapping("/sh")
    public String receive(int id){
        ItemOrder itemOrder = itemOrderService.getById(id);
        itemOrder.setStatus(3);

        itemOrderService.updateById(itemOrder);
        return "redirect:/itemOrder/my.action";
    }

    //去評價(用戶端)
    @RequestMapping("/pj")
    public String comment(int id, HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("itemId",id);

        return "itemOrder/pj";
    }

    //結算訂單(用戶端)
    @RequestMapping("/exAdd")
    public String exAdd(@RequestBody List<Car> carList,HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        for (Car inputCar : carList) {
            Car car = carService.getById(inputCar.getId());

            inputCar.setItemId(car.getItemId());
            inputCar.setUserId(userId);
            inputCar.setPrice(car.getPrice());
            inputCar.setTotal(car.getTotal());
        }

        //新增數據至item_order
        ItemOrder itemOrder = new ItemOrder();
        Date date = new Date();

        itemOrder.setUserId(userId);
        itemOrder.setAddTime(date);

        //生成code
        String dateToStr = DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:SS");
        String dateToStr2 = dateToStr.substring(0, 16);
        String dateToStr3 = dateToStr2.replace("-", "");
        String dateToStr4 = dateToStr3.replace(":","");
        String dateToStr5 = dateToStr4.replace(" ","");

        String code = dateToStr5+"0001";

        itemOrder.setCode(code);

        //計算總金額
        double total=0;

        for (Car inputCar : carList) {
            double carTotal = Double.parseDouble(inputCar.getTotal());
            total = total + carTotal;
        }

        itemOrder.setTotal(String.valueOf(total));
        itemOrder.setIsDelete(0);
        itemOrder.setStatus(0);

        itemOrderService.insert(itemOrder);

        System.out.println(carList);

        //新增數據至order_detail
        ItemOrder newItemOrder = itemOrderService.getBySqlReturnEntity("select * from item_order where code='" + code + "'");

        for (Car inputCar : carList) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setItemId(inputCar.getItemId());
            orderDetail.setOrderId(newItemOrder.getId());
            orderDetail.setStatus(0);
            orderDetail.setNum(inputCar.getNum());
            orderDetail.setTotal(inputCar.getTotal());

            orderDetailService.insert(orderDetail);
        }

        //更新item內的購買數量
        for (Car inputCar : carList){
            Item item = itemService.getById(inputCar.getItemId());
            Integer gmNum = item.getGmNum();
            gmNum = gmNum + inputCar.getNum();
            item.setGmNum(gmNum);
            itemService.updateById(item);
        }

        //清空購物車
        for (Car inputCar : carList) {
            Integer id = inputCar.getId();
            carService.deleteById(id);
        }

        return "car/car";
    }

}
