package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Item;
import com.javapandeng.po.OrderDetail;
import com.javapandeng.service.impl.ItemServiceImpl;
import com.javapandeng.service.impl.OrderDetailServiceImpl;
import com.javapandeng.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController extends BaseController {
    @Autowired
    private OrderDetailServiceImpl orderDetailService;
    @Autowired
    private ItemServiceImpl itemService;

    @RequestMapping("/ulist")
    public String uList(OrderDetail orderDetail, Model model){
        //分頁查詢
        String sql="select * from order_detail where order_id="+orderDetail.getOrderId();

        List<OrderDetail> orderDetails = orderDetailService.listBySqlReturnEntity(sql);
        for (OrderDetail detail : orderDetails) {
            Integer itemId = detail.getItemId();
            Item item = itemService.getById(itemId);

            detail.setItem(item);
        }

        Pager<OrderDetail> pager = orderDetailService.createPagerEntity();
        pager.setDatas(orderDetails);

        model.addAttribute("pagers",pager);
        model.addAttribute("obj",orderDetail);

        return "orderDetail/ulist";
    }
}
