/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.tigera.document.definition.TigEraFileTransfer.EResultStatus;
import com.tigera.document.definition.TigEraFileTransfer.MsgBatchInfo;
import com.tigera.document.definition.TigEraFileTransfer.MsgFileInfo;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.pb.bean.ResultInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.cm.util.JsonUtil;
import cn.net.sinodata.cm.util.OpeMetaFileUtils;
import cn.net.sinodata.framework.log.SinoLogger;
import cn.net.sinodata.framework.util.FileUtil;

/**
 * @author manan
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class AddBatchService extends HttpServlet{
	
	private SinoLogger logger = SinoLogger.getLogger(this.getClass());
	
	@Resource
	private IContentManagerService manageService;
	
	Map<String, BatchInfo>	batchs	= new HashMap<String, BatchInfo>();
	private ResultInfo result = new ResultInfo();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 指定在内存中缓存数据大小,单位为byte,这里设为1Mb
		factory.setSizeThreshold(1024 * 1024);
		// 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		factory.setRepository(new File("D:\\temp"));
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 指定单个上传文件的最大尺寸,单位:字节，这里设为50Mb
		upload.setFileSizeMax(50 * 1024 * 1024);
		// 指定一次上传多个文件的总尺寸,单位:字节，这里设为50Mb
		upload.setSizeMax(100 * 1024 * 1024);
		upload.setHeaderEncoding("UTF-8");

		List<FileItem> items = null;
		try {
			// 解析request请求
			items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item != null) {
					// 表单内容，ignore
					if (!item.isFormField()) {
						logger.info("处理传送文件");
						processFromUpload(item, response);
						result.setStatus(EResultStatus.eSuccess);
					}
				} else {
					result.setStatus(EResultStatus.eFailed);
					result.setMsg("解析请求失败，请求中不包含任何对象");
				}

			}
		} catch (Exception e) {
			result.setStatus(EResultStatus.eFailed);
			result.setMsg("提交批次失败: " + e.getMessage());
			logger.error(e);
		} finally {
			response.setCharacterEncoding("UTF-8");
//			response.getWriter().write(Arrays.toString(result.ToNetMsg().toByteArray()));
			result.toNetMsg().writeTo(response.getOutputStream());
		}
	}
	
	private BatchInfo parseItem(final Map<String, byte[]> fileContents, final List<FileItem> items) {
		// 解析表单项目
		BatchInfo batchInfo = null;
		Iterator<FileItem> iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (item.isFormField()) { // 如果是普通表单属性
				// 相当于input的name属性 <input type="text" name="content">
				String name = item.getFieldName();
				String value = item.getString();
				logger.debug(name + ": " + value);
				batchInfo = JsonUtil.jsonStr2BatchInfo(value);
			} else { // 如果是上传文件
				String fileId = item.getFieldName();
				byte[] date = item.get();
				fileContents.put(fileId, date);
			}
		}
		return batchInfo;
	}
	
	private void processFromUpload(FileItem item, HttpServletResponse hsr) throws Exception
	{
		String fname = item.getName();

		fname = fname.substring(fname.lastIndexOf("\\") + 1, fname.length());
		if (fname.endsWith(OpeMetaFileUtils.PBOPEEXT)) {
			System.out.println("获得批次元数据信息");
			// pb对象转换为po对象
			MsgBatchInfo mbatch = MsgBatchInfo.parseFrom(item.getInputStream());
			BatchInfo batchInfo = BatchInfo.FromNetMsg(mbatch);

			if (batchInfo.isFileDataComplete()) {
				// 校验po对象信息是否完整
				// TODO
				manageService.addBatch(batchInfo);
				result.setStatus(EResultStatus.eSuccess);
			} else {
				batchs.put(batchInfo.getBatchId(), batchInfo);
			}

		} else if (fname.endsWith(OpeMetaFileUtils.PBDataExt)) {
			System.out.println("获得批次数据信息");
			InputStream is = item.getInputStream();
			FileUtil.byte2file(FileUtil.input2byte(is), "e:", "222");
			MsgFileInfo mfileinfo = MsgFileInfo.parseFrom(item.getInputStream());
			if (batchs.containsKey(mfileinfo.getBatchNO13())) {
				BatchInfo batchInfo = batchs.get(mfileinfo.getBatchNO13());
				FileInfo nfileinfo = FileInfo.FromPBMsg(mfileinfo);
				batchInfo.updateFileData(nfileinfo);

				// TODO: 文件数据完整后，进行ProcessBatch
				if (batchInfo.isFileDataComplete()) {
					manageService.upsertBatch(batchInfo);
				}

			} else {
				throw new Exception("上传批次不完整，请重新上传");
			}

		} else {
			// 上传数据内容不对
			throw new Exception("上传数据内容的扩展名非" + OpeMetaFileUtils.PBDataExt + "或者" + OpeMetaFileUtils.PBOPEEXT + "服务拒绝");
		}
	}
	
	public static void main(String[] args) {
//		String json="{'createTime':'','files':[{'fileSize':'','fileId':'0001','createTime':'','fileMd5':'','localPath':'f:\\tmp\\0005.jpg','fileName':'0005.jpg','lastModTime':'','mimeType':'image/png','version':0,'creator':''},{'fileSize':'','fileId':'0002','createTime':'','fileMd5':'','localPath':'f:\\tmp\\111.png','fileName':'111.png','lastModTime':'','mimeType':'image/gif','version':0,'creator':''}],'syncTime':'','state':'','serialNumber':'','batchId':'3ac99a22-cf15-4870-a6c5-9ec8b95f62f3','lastModTime':'','orgId':'o999','syncTotalTime':'','syncType':'','sysId':'111','syncState':'','sourceIp':'','tellerNo':'','spareOne':''}";
		
	}
}