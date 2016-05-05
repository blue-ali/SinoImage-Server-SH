/**
 * 
 */
package cn.net.sinodata.cm.content;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.annotation.Resource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.net.sinodata.cm.common.GlobalVars;

/**
 * 代理bean
 * @author manan
 *
 */
@Service("contentService")
public class ProxyContentServiceBean implements FactoryBean<IContentService>, InvocationHandler{
	
//	@Resource
	private IContentService jcrService;
	@Resource
	private IContentService fileService;
	
	public IContentService getObject() throws Exception {
		Object proxyObj = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{IContentService.class}, this);
		return (IContentService)proxyObj;
	}

	public Class<IContentService> getObjectType() {
		return IContentService.class;
	}

	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(GlobalVars.presist_type.equals("0")){
			return method.invoke(jcrService, args);
		}else{
			return method.invoke(fileService, args);
		}
	}

}
