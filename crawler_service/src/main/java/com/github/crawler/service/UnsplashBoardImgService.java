package com.github.crawler.service;

import com.github.crawler.entity.UnsplashBoardImg;
import com.github.crawler.mapper.UnsplashBoardImgMapper;
import com.github.crawler.mapper.provider.bean.Where;
import com.github.crawler.mapper.provider.bean.WhereOrderLimit;
import com.github.crawler.mapper.provider.bean.Wheres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnsplashBoardImgService extends BaseService {

	@Autowired
	UnsplashBoardImgMapper unsplashBoardImgMapper;

	public int insertEntity(UnsplashBoardImg unsplashBoardImg){
		return unsplashBoardImgMapper.insertEntity(unsplashBoardImg);
	}

	public List<UnsplashBoardImg> getImgByOriginsiteImageId(String originsiteImgeId) {

		WhereOrderLimit wol =new WhereOrderLimit();

		wol.addWhere(Where.equal("unsplash_image_id", originsiteImgeId));

		return unsplashBoardImgMapper.selectSqlWOL(wol, UnsplashBoardImg.class);

	}

	public Long countUnsplashImageId(String UnsplashImageId) {
	    Wheres wheres = new Wheres();
        wheres.add(Where.equal("unsplash_image_id",UnsplashImageId));
        
    	return unsplashBoardImgMapper.countSql(wheres, UnsplashBoardImg.class);
    }

}
