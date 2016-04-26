package cn.net.sinodata.framework.base.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * dao基础类
 * @author manan
 *
 */
public class SinoBaseDao extends HibernateDaoSupport {

	@Autowired  
	public void setMySessionFactory(SessionFactory sessionFactory){  
	    super.setSessionFactory(sessionFactory);  
	}  
}
