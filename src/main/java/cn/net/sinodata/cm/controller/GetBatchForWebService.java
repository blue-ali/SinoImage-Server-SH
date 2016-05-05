package cn.net.sinodata.cm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
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
public class GetBatchForWebService extends HttpServlet {

	private SinoLogger logger = SinoLogger.getLogger(this.getClass());

	@Resource
	private IContentManagerService manageService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetBatchForWebService() {
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
		response.setContentType("text/plain;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String batchId = request.getParameter("batchNo");
		JSONObject result;
		try {
			result = jsonQuery(batchId);
			PrintWriter out = response.getWriter();
			// String data = result1.toString();
			out.println(result);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		/*
		 * try { BatchInfo batchInfo = manageService.getBatch(batchId); if
		 * (batchInfo != null) {
		 * batchInfo.setOperation(EOperType.eFROM_SERVER_NOTCHANGE);
		 * batchInfo.toNetMsg().writeTo(response.getOutputStream()); } } catch
		 * (Exception e) { e.printStackTrace(); // TODO 异常返回异常信息 }
		 */

		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
	}

	public JSONObject jsonQuery(String batchId) throws Exception {
		JSONObject jsonObj = null;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		BatchInfo batchInfo = manageService.getBatch(batchId);
		List<FileInfo> fileInfoList = batchInfo.getFileInfos();
		jsonMap.put("rows", fileInfoList);
		jsonObj = JSONObject.fromObject(jsonMap);
		return jsonObj;

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
