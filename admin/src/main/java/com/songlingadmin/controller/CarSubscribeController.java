package com.songlingadmin.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.songlingadmin.entity.CarNumber;
import com.songlingadmin.entity.CarSubscribe;
import com.songlingadmin.service.CarSubscribeService;
import com.songlingadmin.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author songling
 * @since 2020-07-07
 */
@RestController
@RequestMapping("car/" )
public class CarSubscribeController {

    @Autowired
    private CarSubscribeService carSubscribeService;

    @GetMapping("/carSubscribe/list" )
    public TableDataInfo list(CarSubscribe carSubscribe){//String validTime //有時間查詢直接用string，涉及到轉換太麻煩
        Page<CarSubscribe> page=PageUtil.getPage();
        QueryWrapper<CarSubscribe> query = new QueryWrapper();
        query.like(carSubscribe.getCarNumber()!=null,"car_number",carSubscribe.getCarNumber());
        query.like(carSubscribe.getSubscribeName()!=null,"subscribe_name",carSubscribe.getSubscribeName());
        query.eq(carSubscribe.getSubscribeCode()!=null,"subscribe_code",carSubscribe.getSubscribeCode());
        query.eq(carSubscribe.getSubscribeStatus()!=null,"subscribe_status",carSubscribe.getSubscribeStatus());
        query.ge(StringUtils.isNotEmpty(carSubscribe.getBeginTime()),"subscribe_time",carSubscribe.getBeginTime());
        query.le(StringUtils.isNotEmpty(carSubscribe.getEndTime()),"subscribe_time",carSubscribe.getEndTime());
        query.orderByDesc("create_time");
        carSubscribeService.page(page,query);
        return AjaxResult.toDataTable(page);
    }

    @GetMapping("/carSubscribe/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id){
        return AjaxResult.success(carSubscribeService.getById(id));
    }

    @PostMapping("/carSubscribe" )
    public AjaxResult add(@RequestBody CarSubscribe carSubscribe){
        return AjaxResult.success(carSubscribeService.save(carSubscribe));
    }

    @PutMapping("/carSubscribe" )
    public AjaxResult edit(@RequestBody CarSubscribe carSubscribe){
        return AjaxResult.success(carSubscribeService.updateById(carSubscribe));
    }

    @DeleteMapping("/carSubscribe/{ids}" )
    public AjaxResult remove(@PathVariable Long[]ids){
        System.out.println("什么"+ids[0]);
        carSubscribeService.removeById(ids[0]);
        System.out.println(carSubscribeService.removeByIds(Arrays.asList(ids)));
        return AjaxResult.success(carSubscribeService.removeByIds(Arrays.asList(ids)));
    }

}