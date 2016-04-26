/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.pb.LogosFileTransfer.EOperType;
import cn.net.sinodata.cm.pb.LogosFileTransfer.MsgBatchInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.framework.log.SinoLogger;

/**
 * @author manan
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class ServerGetServerInfo extends HttpServlet {

	private SinoLogger logger = SinoLogger.getLogger(this.getClass());

	@Resource
	private IContentManagerService manageService;

	/*
	 * public void doPost(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException {
	 * 
	 * String result = ""; try { BufferedReader br = new BufferedReader(new
	 * InputStreamReader( request.getInputStream())); String line = null;
	 * StringBuilder sb = new StringBuilder(); while ((line = br.readLine()) !=
	 * null) { sb.append(line); } String json = sb.toString(); json =
	 * URLDecoder.decode(json, "utf8"); logger.debug("request json: " + json);
	 * // BatchInfo batchInfo = JsonUtil.jsonStr2BatchInfo(json); // batchInfo =
	 * manageService.getBatch(batchInfo); BatchInfo batchInfo =
	 * JsonUtil.jsonStr2BatchInfo(json); batchInfo =
	 * manageService.getBatch(batchInfo); result =
	 * JSONObject.fromObject(batchInfo).toString(); } catch (Exception e) {
	 * logger.error("获取批次信息失败, " + e); result = "{'result': '0', 'errMsg': " +
	 * "'获取批次信息失败,ERR: " + e.getMessage() + "'}"; } finally {
	 * response.getWriter().write(result); } // InputStream is = new
	 * BufferedInputStream(new FileInputStream(file)); // is.read(buffer); //
	 * is.close(); // // OutputStream os = new
	 * BufferedOutputStream(response.getOutputStream()); // os.write(buffer); //
	 * os.flush(); // os.close(); }
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String batchId = request.getParameter("batchno");
		// MsgBatchInfo mBatchInfo =
		// MsgBatchInfo.parseFrom(request.getInputStream());
		// BatchInfo querybatchinfo = BatchInfo.FromNetMsg(mBatchInfo);
		try {
			BatchInfo batchInfo = manageService.getBatch(batchId);
			if (batchInfo != null) {
				batchInfo.setOperation(EOperType.FROM_SERVER_NOTCHANGE);
			}
			batchInfo.toNetMsg().writeTo(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 异常返回异常信息
		}

	}

}
