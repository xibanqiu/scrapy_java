package com.github.crawler.mapper;

import com.github.crawler.entity.UserBoardFavourite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserBoardFavouriteMapper extends BaseMapper<UserBoardFavourite> {
	
	@Select("select uid from user_board_favourite where id =#{id}")
	Long selectUidById(Long id);
}
