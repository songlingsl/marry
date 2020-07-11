package com.songlingadmin.controller;



import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysRole;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import com.songlingadmin.entity.CarRecorder;
import com.songlingadmin.entity.CarSubscribe;
import com.songlingadmin.entity.CarSubscribeCode;
import com.songlingadmin.mapper.CarRecorderMapper;
import com.songlingadmin.mapper.CarSubscribeCodeMapper;
import com.songlingadmin.service.CarSubscribeService;
import com.songlingadmin.util.baidu.AccessToken;
import com.songlingadmin.util.baidu.Base64Util;
import com.songlingadmin.util.baidu.HttpUtil;
import com.songlingadmin.vo.WxUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/carcheck" )
public class AppController {

    @Autowired
    private CarSubscribeService carSubscribeService;
    @Autowired
    CarRecorderMapper  carRecorderMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private CarSubscribeCodeMapper mapper;

    @PostMapping("/carnumber/uploadPhoto" )
    public AjaxResult importData(MultipartFile file)  {
        try {
            String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
            String imgStr = Base64Util.encode(file.getBytes());
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AccessToken.get();
            String result = HttpUtil.post(url, accessToken, param);
            log.info("识别结果" + result);
            String number = "";
            JSONObject jsonObject = JSONObject.parseObject(result);
            jsonObject = jsonObject.getJSONObject("words_result");
            if (jsonObject != null) {
                number = jsonObject.getString("number");
                CarSubscribe carSubscribe=getCarSubscribeByNumber(number);
                if(carSubscribe==null){
                    return AjaxResult.error(number+" 未预约",null);
                }
                if(carSubscribe.getSubscribeStatus()==1){
                    return AjaxResult.error(number+"取消了预约(预约人："+carSubscribe.getSubscribeName()+"；预约时间："+ org.apache.commons.lang.time.DateFormatUtils.format(carSubscribe.getSubscribeTime(),"yyyy-MM-dd") +")",null);
                }
                LocalDateTime preTime=getPreTime(carSubscribe.getSubscribeId());
                carSubscribe.setPreTime(preTime);//上次入场时间
                return AjaxResult.success("",carSubscribe);
            }
        }catch(Exception e){
            return  AjaxResult.error(e.getMessage(),null);

        }

        return AjaxResult.error("未识别出该车牌,请手工查询",null);
    }

    private CarSubscribe getCarSubscribeByNumber(String number){
        String preTime=DateFormatUtils.format(DateUtils.addDays(new Date(),-185),"yyyy-MM-dd");//提前预约时间半年来检查
        String afterTime=DateFormatUtils.format(DateUtils.addDays(new Date(),185),"yyyy-MM-dd");//预约时间后半年来检查
        log.info("预约时间在两个时间之间的允许"+preTime+"   "+afterTime);
        QueryWrapper<CarSubscribe> query = new QueryWrapper();
        query.eq("car_number",number);
        query.ge("subscribe_time",preTime);
        query.le("subscribe_time",afterTime);
        query.orderByDesc("create_time");
        CarSubscribe carSubscribe=carSubscribeService.getOne(query,false);
      return carSubscribe;

    }

    private LocalDateTime getPreTime(Long subscribeId){
        QueryWrapper<CarRecorder> query = new QueryWrapper();
        query.eq("subscribe_id",subscribeId);
        query.orderByDesc("recorder_time");
        List<CarRecorder>  list=carRecorderMapper.selectList(query);
        if(!list.isEmpty()){
            return list.get(0).getRecorderTime();
        }
         return  null;
    }
    @GetMapping("/carnumber/recordEnter" )
    public AjaxResult agree(Long subscribeId,Long inputUserId)  {
        log.info("subscribeId:"+subscribeId+"inputUserId:"+inputUserId);
        CarRecorder recoder=new CarRecorder();
        recoder.setSubscribeId(subscribeId);
        recoder.setRecorderUserId(inputUserId);
        carRecorderMapper.insert(recoder);
        return AjaxResult.success();
    }

    @GetMapping("/carnumber/query" )
    public AjaxResult agree(String number)  {
        CarSubscribe carSubscribe=getCarSubscribeByNumber(number);
        if(carSubscribe==null){
            return AjaxResult.error(number+" 未预约",null);
        }
        if(carSubscribe.getSubscribeStatus()==1){
            return AjaxResult.error(number+"取消了预约\n(预约人："+carSubscribe.getSubscribeName()+"；预约时间："+ org.apache.commons.lang.time.DateFormatUtils.format(carSubscribe.getSubscribeTime(),"yyyy-MM-dd") +")",null);
        }
        LocalDateTime preTime=getPreTime(carSubscribe.getSubscribeId());
        carSubscribe.setPreTime(preTime);//上次入场时间
        return AjaxResult.success("",carSubscribe);
    }


    @GetMapping("/carnumber/login" )
    public AjaxResult login(String userName,String password)  {
        log.info(userName+"  "+password);
        SysUser user=userService.selectUserByUserName(userName);
        if(user==null){
            return AjaxResult.error("没有该用户");
        }
        if(!SecurityUtils.matchesPassword(password,user.getPassword())){
            return AjaxResult.error("密码错误");
        }
        String wxrole="";
        for(SysRole sysRole:user.getRoles()){
            if(sysRole.getRoleKey().equals("phonesubscribe")){
                wxrole="phonesubscribe";
                break;
            }
            if(sysRole.getRoleKey().equals("incheck")){
                wxrole="incheck";
            }
        }
        if (StringUtils.isEmpty(wxrole)){
            return AjaxResult.error("您没有权限操作该小程序");
        }
        WxUserVO vo=new WxUserVO();
        vo.setUserId(user.getUserId());
        vo.setUserName(user.getUserName());
        vo.setUserNickName(user.getNickName());
        vo.setDeptName(user.getDept().getDeptName());
        vo.setRoleKey(wxrole);
        log.info(vo.toString());
        return AjaxResult.success(vo);
    }


    @PostMapping("/carnumber/inputSubscribe" )
    public AjaxResult inputSubscribe(@RequestBody CarSubscribe carSubscribe)  {
        log.info("小程序提交的预约表"+carSubscribe.toString());
        CarSubscribeCode code=new CarSubscribeCode();
        code.setCodeStatus(0);
        mapper.insert(code);
        carSubscribe.setSubscribeCode(code.getCodeId());
        carSubscribeService.save(carSubscribe);
        return AjaxResult.success(code);
    }


    @GetMapping("/carnumber/queryCode" )
    public AjaxResult queryCode(String code)  {
        String preTime=DateFormatUtils.format(DateUtils.addDays(new Date(),-185),"yyyy-MM-dd");//提前预约时间半年来检查
        String afterTime=DateFormatUtils.format(DateUtils.addDays(new Date(),185),"yyyy-MM-dd");//预约时间后半年来检查
        QueryWrapper<CarSubscribe> query = new QueryWrapper();
        query.eq("subscribe_code",code);
        query.ge("subscribe_time",preTime);
        query.le("subscribe_time",afterTime);

        CarSubscribe carSubscribe=carSubscribeService.getOne(query,false);
        if(carSubscribe==null){
            return AjaxResult.error(code+" 未预约",null);
        }
        if(carSubscribe.getSubscribeStatus()==1){
            return AjaxResult.error(code+"取消了预约\n(预约人："+carSubscribe.getSubscribeName()+"；预约时间："+ org.apache.commons.lang.time.DateFormatUtils.format(carSubscribe.getSubscribeTime(),"yyyy-MM-dd") +")",null);
        }
        LocalDateTime historyTime=getPreTime(carSubscribe.getSubscribeId());
        carSubscribe.setPreTime(historyTime);//上次入场时间
        return AjaxResult.success("",carSubscribe);
    }


}
