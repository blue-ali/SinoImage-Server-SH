package cn.net.sinodata.cm.hibernate.dao;

import org.springframework.stereotype.Repository;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.BatchLogInfo;

@Repository
public class BatchDao extends GenericDao<BatchInfo>{

	public BatchDao() {
		super(BatchInfo.class);
	}
	
	
	public void save(BatchInfo batchInfo, boolean withLog){
		sessionFactory.getCurrentSession().saveOrUpdate(batchInfo);
		if(withLog){
			sessionFactory.getCurrentSession().saveOrUpdate(BatchLogInfo.fromBatchInfo(batchInfo));
		}
	}
	
	public void delete(BatchInfo batchInfo, boolean withLog){
		sessionFactory.getCurrentSession().delete(batchInfo);
		if(withLog){
			sessionFactory.getCurrentSession().save(BatchLogInfo.fromBatchInfo(batchInfo));
		}
	}
}
