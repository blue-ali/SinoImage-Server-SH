package cn.net.sinodata.cm.hibernate.dao;

import org.springframework.stereotype.Repository;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;

@Repository
public class BatchDao extends GenericDao<BatchInfo>{

	public BatchDao() {
		super(BatchInfo.class);
	}

}
