package com.songlingapp;


import com.songlingapp.controller.SonglingController;
import com.songlingapp.entity.CarSubscribeCode;
import com.songlingapp.entity.Songling;
import com.songlingapp.mapper.CarSubscribeCodeMapper;
import com.songlingapp.service.SonglingService;
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
    CarSubscribeCodeMapper mapper;
    @Resource
    SonglingService songlingService;
    @Test
    public  void testConfig(){//测试可以
        songlingService.save(new Songling().setName("dddd"));
//        mapper.insert(new CarSubscribeCode());
//        log.info("fsdafdf");
        ;
    // log.info(songlingController.getId(null).toString());

    }
}
