package com.songlingadmin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.songlingadmin.entity.CarNumber;
import com.songlingadmin.service.CarNumberService;
import com.songlingadmin.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author songling
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/car" )
public class CarNumberController {

    @Autowired
    private CarNumberService carNumberService;

    @GetMapping("/number/list" )
    public TableDataInfo list( CarNumber carNumber,String validTime) {//有時間查詢直接用string，涉及到轉換太麻煩
        Page<CarNumber> page = PageUtil.getPage();
        QueryWrapper<CarNumber> query = new QueryWrapper();
        query.like(carNumber.getCarNumber()!=null,"car_number",carNumber.getCarNumber());
        query.le(validTime!=null,"valid_time",validTime);
        query.orderByDesc("car_number");
        carNumberService.page(page,query);
        return AjaxResult.toDataTable(page);
    }

    @GetMapping("/number/{carId}" )
    public AjaxResult getInfo(@PathVariable("carId" ) Long carId) {
        return AjaxResult.success(carNumberService.getById(carId));

    }

    @PostMapping("/number" )
    public AjaxResult add(@RequestBody CarNumber carNumber) {
        return AjaxResult.success(carNumberService.save(carNumber));

    }

    @PutMapping("/number" )
    public AjaxResult edit(@RequestBody CarNumber carNumber) {
        return AjaxResult.success(carNumberService.updateById(carNumber));
    }

    @DeleteMapping("/number/{carIds}" )
    public AjaxResult remove(@PathVariable Long[] carIds) {
        return AjaxResult.success(carNumberService.removeByIds(Arrays.asList(carIds)));
    }

    @PostMapping("/number/importData" )
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<CarNumber> util = new ExcelUtil<CarNumber>(CarNumber.class);
        List<CarNumber> CarNumberList = util.importExcel(file.getInputStream());
        carNumberService.saveBatch(CarNumberList);
        return AjaxResult.success("保存成功" + CarNumberList.size() + "条记录" );
    }
}
