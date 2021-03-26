package com.github.crawler.mapper;

import com.github.crawler.entity.SequencePinNextId;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;


@Mapper
public interface SequencePinWordNextIdMapper {

	@Insert("insert into sequence_pin_word_next_id VALUES (0);")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insertSql(SequencePinNextId entity);
}
