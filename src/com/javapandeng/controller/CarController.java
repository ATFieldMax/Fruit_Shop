package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Car;
import com.javapandeng.po.Item;
import com.javapandeng.service.impl.CarServiceImpl;
import com.javapandeng.service.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController extends BaseController {
    @Autowired
    private CarServiceImpl carService;

    @Autowired
    private ItemServiceImpl itemService;

    //查看購物車
    @RequestMapping("/findBySql")
    public String myCar(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        List<Car> carList = carService.listBySqlReturnEntity("select * from car where user_id=" + userId);
        for (Car car : carList) {
            Integer itemId = car.getItemId();
            Item item = itemService.getById(itemId);
            car.setItem(item);
        }

        model.addAttribute("list",carList);

        return "car/car";
    }

    //加入購物車
    @RequestMapping("/exAdd")
    public void exAdd(int itemId, int num, HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        //檢查購物車內是否有相同品項
        Car oldCar = carService.getBySqlReturnEntity("select * from car where item_id=" + itemId + " and user_id=" + userId);
        if(oldCar != null){
            //有相同品項，只修改數量
            Integer oldNum = oldCar.getNum();
            Integer newNum = oldNum+num;

            oldCar.setNum(newNum);

            carService.updateById(oldCar);

            return;
        }

        Item item = itemService.getById(itemId);
        double price = Double.parseDouble(item.getPrice()) * item.getZk() * 0.1;

        Car car=new Car();

        car.setItemId(itemId);
        car.setUserId(userId);
        car.setNum(num);
        car.setPrice(price);

        String total= String.valueOf(num * price);
        car.setTotal(total);

        carService.insert(car);
    }

    //刪除品項
    @RequestMapping("/delete")
    public void delete(int id){
        carService.deleteById(id);
    }
}
