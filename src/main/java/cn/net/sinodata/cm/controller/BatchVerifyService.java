package cn.net.sinodata.cm.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.framework.log.SinoLogger;
import net.sf.json.JSONObject;

/**
 * @author dongzj
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class BatchVerifyService extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static SinoLogger logger = SinoLogger.getLogger(BatchVerifyService.class);

	@Resource
	protected IContentManagerService manageService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BatchVerifyService() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		// JSONObject json=JSONObject.fromObject(request.getParameter("data"));
		// String batchid = request.getParameter("txtBatchNo");
		String batchVerify = request.getParameter("dealBatchNo");
		String fileVerify = request.getParameter("dealImgList");
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			String[] batchVerifyInfo = batchVerify.split("\\|");
			
			String[] fileVerifyInfos = fileVerify.split(",");
			
			
			manageService.updateBatchVerifyState(batchVerifyInfo, fileVerifyInfos);
//			for (int i = 0; i < filelist.length; i++) {
//				String[] fileremark = filelist[i].split("-");
//				String fileid = fileremark[0];
//				String fileaddone = fileremark[1];
//				manageService.addFileRemark(batchid, fileid, fileaddone);
//			}
//			jsonMap.put("total", filelist.length);
			jsonMap.put("result", "true");
		} catch (Exception e) {
			logger.error(e);
			jsonMap.put("resultdsc", "提交失败");
			jsonMap.put("result", "false");
		}
//		result1 = JSONObject.fromObject(jsonMap);
		String jsonStr = replaceNull2EmptyString(JSONObject.fromObject(jsonMap).toString());
	}

	public static String replaceNull2EmptyString(String jsonStr) {
		if (jsonStr == null) {
			return null;
		}
		return jsonStr.replace(":null", ":\"\"");
	}

}
