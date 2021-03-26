package com.github.crawler.mapper;

import com.github.crawler.entity.UnsplashBoardImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UnsplashBoardImgMapper extends BaseMapper<UnsplashBoardImg>{
	
	@Select("select id from unsplash_board_img where unsplash_image_id=#{unsplash_image_id}")
	Long selectByOriginSiteImageId(@Param("unsplash_image_id") String unsplash_image_id);

}
