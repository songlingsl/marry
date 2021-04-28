package com.songlingadmin.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;


import com.ruoyi.project.system.domain.SysUser;
import com.songlingadmin.entity.CarSubscribe;

import com.songlingadmin.entity.CarSubscribeCode;
import com.songlingadmin.mapper.CarSubscribeCodeMapper;
import com.songlingadmin.service.CarSubscribeService;
import com.songlingadmin.util.PageUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
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
    @Autowired
    private CarSubscribeCodeMapper mapper;

    @GetMapping("/carSubscribe/list" )
    public TableDataInfo list(CarSubscribe carSubscribe){//String validTime //有時間查詢直接用string，涉及到轉換太麻煩
        Page<CarSubscribe> page=PageUtil.getPage();
        carSubscribeService.page(page,getQueryWrapper(carSubscribe));
        return AjaxResult.toDataTable(page);
    }

    @GetMapping("/carSubscribe/export")
    public AjaxResult export(CarSubscribe carSubscribe)
    {
        List<CarSubscribe> list=carSubscribeService.list(getQueryWrapper(carSubscribe));
        ExcelUtil<CarSubscribe> util = new ExcelUtil(CarSubscribe.class);
        return util.exportExcel(list, "预约数据");
    }

    private QueryWrapper getQueryWrapper(CarSubscribe carSubscribe){
        QueryWrapper<CarSubscribe> query = new QueryWrapper();
        query.like(carSubscribe.getCarNumber()!=null,"car_number",carSubscribe.getCarNumber());
        query.like(carSubscribe.getSubscribeName()!=null,"subscribe_name",carSubscribe.getSubscribeName());
        query.eq(carSubscribe.getSubscribeCode()!=null,"subscribe_code",carSubscribe.getSubscribeCode());
        query.eq(carSubscribe.getSubscribeStatus()!=null,"subscribe_status",carSubscribe.getSubscribeStatus());
        query.ge(StringUtils.isNotEmpty(carSubscribe.getBeginTime()),"subscribe_time",carSubscribe.getBeginTime());
        query.le(StringUtils.isNotEmpty(carSubscribe.getEndTime()),"subscribe_time",carSubscribe.getEndTime());
        query.eq(carSubscribe.getImportFlag()!=null,"import_flag",carSubscribe.getImportFlag());
        query.like(carSubscribe.getSysNickName()!=null,"sys_nick_name",carSubscribe.getSysNickName());
        query.orderByDesc("create_time");
        return query;
    }

    @GetMapping("/carSubscribe/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id){
        return AjaxResult.success(carSubscribeService.getById(id));
    }

    @PostMapping("/carSubscribe" )
    public AjaxResult add(@RequestBody CarSubscribe carSubscribe){
        CarSubscribeCode code=new CarSubscribeCode();
        code.setCodeStatus(0);
        mapper.insert(code);

        LoginUser loginUser=SecurityUtils.getLoginUser();
        carSubscribe.setSysUserId(loginUser.getUser().getUserId());
        carSubscribe.setSubscribeCode(code.getCodeId());
        carSubscribe.setSysNickName(loginUser.getUsername());//用戶名不許修改，所以可以當做前面展示的人名
        return AjaxResult.success(carSubscribeService.save(carSubscribe));
    }

    @PutMapping("/carSubscribe" )
    @PreAuthorize("@ss.hasPermi('car:subscribe:control')")
    public AjaxResult edit(@RequestBody CarSubscribe carSubscribe){
        return AjaxResult.success(carSubscribeService.updateById(carSubscribe));
    }

    @DeleteMapping("/carSubscribe/{ids}" )
    public AjaxResult remove(@PathVariable Long[]ids){
        carSubscribeService.removeById(ids[0]);
        System.out.println(carSubscribeService.removeByIds(Arrays.asList(ids)));
        return AjaxResult.success(carSubscribeService.removeByIds(Arrays.asList(ids)));
    }

    @PostMapping("/carSubscribe/importData" )
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        LoginUser loginUser=SecurityUtils.getLoginUser();
        ExcelUtil<CarSubscribe> util = new ExcelUtil(CarSubscribe.class);
        List<CarSubscribe> list = util.importExcel(file.getInputStream());
        Iterator<CarSubscribe> it=list.iterator();
        while(it.hasNext()){
            CarSubscribe carSubscribe  = it.next();
            Long code=carSubscribe.getSubscribeCode();
            CarSubscribe query=new CarSubscribe();
            query.setSubscribeCode(code);
            QueryWrapper<CarSubscribe> wrapper = new QueryWrapper(query);
            if(carSubscribeService.list(wrapper).size()>0){
                it.remove();
            };

        }
        list.forEach(carSubscribe -> {
            if(carSubscribe.getStatus12123().equals("取消")){
                carSubscribe.setSubscribeStatus(1);
            };
            carSubscribe.setSysUserId(loginUser.getUser().getUserId());
            carSubscribe.setSysNickName(loginUser.getUsername());
            carSubscribe.setImportFlag(1);//12123导入
        });
        carSubscribeService.saveBatch(list);
        return AjaxResult.success("保存成功" + list.size() + "条记录" );
    }

    @GetMapping("/carSubscribe//importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<CarSubscribe> util = new ExcelUtil(CarSubscribe.class);
        return util.importTemplateExcel("检测预约");
    }

    @GetMapping("/carSubscribe/getTodaySubscribeSum/{time}" )
    public AjaxResult getTodaySubscribeSum(@PathVariable("time" )String time)  {
//        String today= DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        QueryWrapper<CarSubscribe> query = new QueryWrapper();
        query.eq("subscribe_time",time);
        query.eq("import_flag",0);
        int  count= carSubscribeService.count(query);
        return AjaxResult.success(count+"");
    }

}