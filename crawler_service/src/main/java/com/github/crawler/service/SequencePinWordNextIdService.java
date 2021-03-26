package com.github.crawler.service;

import com.github.crawler.entity.SequencePinNextId;
import com.github.crawler.mapper.SequencePinWordNextIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 获取自增ID
 * @author huangkai
 *
 */
@Service
public class SequencePinWordNextIdService {
	@Autowired
	private SequencePinWordNextIdMapper sequencePinWordNextIdMapper;

	public long nextId() {
		SequencePinNextId sni = new SequencePinNextId();
		sequencePinWordNextIdMapper.insertSql(sni);
		return sni.getId();
	}
}
