package com.github.service;


import com.github.crawler.mapper.UnsplashBoardImgMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private UnsplashBoardImgMapper unsplashBoardImgMapper;


    @org.junit.Test
    public void  test(){

        Long aLong = unsplashBoardImgMapper.selectByOriginSiteImageId("wqbM8p9yQuc");
        System.out.println(aLong);

    }



}
