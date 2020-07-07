package com.songlingadmin.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.songlingadmin.entity.CarNumber;
import com.songlingadmin.entity.MarryActivity;
import com.songlingadmin.service.MarryActivityService;
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
 * @since 2020-07-03
 */
@RestController
@RequestMapping("marry/" )
public class MarryActivityController {

    @Autowired
    private MarryActivityService marryActivityService;

    @GetMapping("/activity/list" )
    public TableDataInfo list(MarryActivity marryActivity){//String validTime //有時間查詢直接用string，涉及到轉換太麻煩
        Page<MarryActivity> page=PageUtil.getPage();
        QueryWrapper<MarryActivity> query = new QueryWrapper();
        query.like(marryActivity.getActivityName()!=null,"activity_name",marryActivity.getActivityName());
        marryActivityService.page(page,query);
        return AjaxResult.toDataTable(page);
    }

    @GetMapping("/activity/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id){
        return AjaxResult.success(marryActivityService.getById(id));
    }

    @PostMapping("/activity" )
    public AjaxResult add(@RequestBody MarryActivity marryActivity){
        return AjaxResult.success(marryActivityService.save(marryActivity));
    }

    @PutMapping("/activity" )
    public AjaxResult edit(@RequestBody MarryActivity marryActivity){
        return AjaxResult.success(marryActivityService.updateById(marryActivity));
    }

    @DeleteMapping("/activity/{ids}" )
    public AjaxResult remove(@PathVariable Long[]ids){
        return AjaxResult.success(marryActivityService.removeByIds(Arrays.asList(ids)));
    }

}