package com.github.crawler.service;

import com.github.crawler.mapper.UserBoardFavouriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBoardFavouriteService {
    @Autowired
    UserBoardFavouriteMapper userBoardFavouriteMapper;


    public Long selectUidById(Long id){
        return userBoardFavouriteMapper.selectUidById(id);
    }

}
