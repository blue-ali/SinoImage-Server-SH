package cn.net.sinodata.cm.service;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import base.BaseSpringTest;
import cn.net.sinodata.cm.hibernate.dao.BatchDao;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;

@Transactional(rollbackFor=Exception.class)
@Repository
public class HibernateTest extends BaseSpringTest{

	@Resource
	private BatchDao dao;
	
	@Test
	public void test() {
		BatchInfo batchInfo = new BatchInfo();
		batchInfo.setBatchId("12345657");
//		batchInfo.setLastModified(new Date());
		dao.save(batchInfo);
	}

}
