package cn.net.sinodata.cm.hibernate.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import base.BaseSpringTest;
import cn.net.sinodata.cm.hibernate.po.FileInfo;

public class FileDaoTest extends BaseSpringTest{

	@Resource
	FileDao fileDao;
	
	@Test
	public void test(){
		List<FileInfo> list = fileDao.queryListByBatchId4Test("EA4418012511673");
		System.out.println(list.get(0).getFileId());
	}
}
