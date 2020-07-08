package com;

import com.songlingadmin.controller.SonglingController;
import com.songlingadmin.entity.CarNumber;
import com.songlingadmin.entity.CarSubscribeCode;
import com.songlingadmin.mapper.CarNumberMapper;
import com.songlingadmin.mapper.CarSubscribeCodeMapper;
import com.songlingadmin.mapper.SonglingMapper;
import com.songlingadmin.service.CarNumberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

//import com.songlingadmin.service.SonglingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ControllerTests {
    @Resource
    SonglingController songlingController;
    @Resource
    CarSubscribeCodeMapper  mapper;
    @Resource
    CarNumberMapper carNumberMapper;

    @Resource
    CarNumberService ser;
    @Resource
    SonglingMapper  Songlingmapper;
    @Test
    public  void testConfig(){//测试可以
        ser.save(new CarNumber());
       log.info("fsdafdf");
//      CarSubscribeCode a=new CarSubscribeCode();
//        mapper.insert(a);

    // log.info(songlingController.getId(null).toString());

    }
}
