package cn.net.sinodata.cm.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.net.sinodata.cm.hibernate.po.InvoiceInfo;

@Repository
public class InvoiceDao extends GenericDao<InvoiceInfo>{

	public InvoiceDao() {
		super(InvoiceInfo.class);
	}

	@SuppressWarnings("unchecked")
	public List<InvoiceInfo> queryListByIds(List<String> invoiceIds){
		String hql = "from InvoiceInfo where invoice_no in (:invoiceIds)";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameterList("invoiceIds", invoiceIds);
		return query.list();
	}
	
	public List<InvoiceInfo> queryListByBatchId(String batchId){
		String hql = "from InvoiceInfo where batchid=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, new String[]{batchId});
		return query.list();
	}
}
