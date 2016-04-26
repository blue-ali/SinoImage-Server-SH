/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.cm.util.JsonUtil;
import cn.net.sinodata.framework.log.SinoLogger;
import cn.net.sinodata.framework.util.FileUtil;

/**
 * @author manan
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class DownloadFileService extends HttpServlet{
			
	private SinoLogger logger = SinoLogger.getLogger(this.getClass());
	
	@Resource
	private IContentManagerService manageService;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
 throws ServletException, IOException {

		String result = "";
		byte[] buffer = null;
		OutputStream output = new BufferedOutputStream(response.getOutputStream());
		try {
			String json = request.getHeader("batchJson");
			json = URLDecoder.decode(json, "utf8");
			logger.debug("request json: " + json);
			BatchInfo batchInfo = JsonUtil.jsonStr2BatchInfo(json);
			buffer = manageService.getContent(batchInfo);
//			FileUtil.byte2file(buffer, "f:\\tmp\\server", "111.zip");
			output.write(buffer);
			output.flush();
			output.close();
			// result = JSONObject.fromObject(batchInfo).toString();
		} catch (Exception e) {
			logger.error("下载文件失败, ", e);
			result = "{'result': '0', 'errMsg': " + "'下载文件失败,ERR: " + e.getMessage() + "'}";
//			response.getWriter().write(result);
			output.write(result.getBytes());
		} finally {
		}
	}
	
}
