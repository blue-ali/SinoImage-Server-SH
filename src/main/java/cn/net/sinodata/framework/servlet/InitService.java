package cn.net.sinodata.framework.servlet;

import org.springframework.context.ApplicationContext;

public interface InitService {
	/** 
	  * @Title: execute 
	  * @Description: 执行服务
	  * @param @param appctx spring服务容器
	  * @return boolean    返回类型 
	  * @throws 
	  */
	public boolean execute(ApplicationContext appctx);
}
