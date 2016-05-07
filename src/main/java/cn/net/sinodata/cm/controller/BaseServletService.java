/**
 * 
 */
package cn.net.sinodata.cm.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;

import cn.net.sinodata.cm.pb.bean.ResultInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.framework.log.SinoLogger;

/**
 * servlet 基类
 * @author manan
 *
 */
@SuppressWarnings("serial")
public class BaseServletService extends HttpServlet {

	protected SinoLogger logger = SinoLogger.getLogger(this.getClass());

	@Resource
	protected IContentManagerService manageService;

	private ThreadLocal<ResultInfo> result = new ThreadLocal<ResultInfo>();
	
	protected ResultInfo getResult() {
		ResultInfo rs = result.get();
		if (rs == null) {
			rs = new ResultInfo();
			result.set(rs);
		}
		return rs;
	}
}
