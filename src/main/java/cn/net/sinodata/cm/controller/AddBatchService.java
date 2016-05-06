/**
 * 
 */
package cn.net.sinodata.cm.controller;

import java.io.File;
import java.io.IOException;
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

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.hibernate.po.InvoiceInfo;
import cn.net.sinodata.cm.httpurl.OtherSysUrl;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EResultStatus;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgBatchInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgFileInfo;
import cn.net.sinodata.cm.pb.bean.ResultInfo;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.cm.util.OpeMetaFileUtils;
import cn.net.sinodata.framework.log.SinoLogger;

/**
 * @author manan
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("serial")
public class AddBatchService extends BaseServletService {

	// private SinoLogger logger = SinoLogger.getLogger(this.getClass());

	// @Resource
	// private IContentManagerService manageService;

	private Map<String, BatchInfo> batchs = new HashMap<String, BatchInfo>();
	// private ResultInfo result = new ResultInfo();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
			BatchInfo batchInfo = null;
			// 解析request请求
			items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item != null) {
					// 表单内容，ignore
					if (!item.isFormField()) {
						logger.info("处理传送文件");
						batchInfo = processFromUpload(item, response);
					}
				} else {
					result.setStatus(EResultStatus.eFailed);
					result.setMsg("解析请求失败，请求中不包含任何对象");
				}

			}
			//TODO 失败处理
//			boolean res = OtherSysUrl.sendJunkangUrl(batchInfo.getBatchId(), batchInfo.getFileInfos().size());
//			if(!res){
//				result.setStatus(EResultStatus.eFailed);
//				result.setMsg("提交批次失败: 与费控系统通讯失败");
//			}
		} catch (Exception e) {
			result.setStatus(EResultStatus.eFailed);
			result.setMsg("提交批次失败: " + e.getMessage());
			logger.error(e);
		} finally {
			response.setCharacterEncoding("UTF-8");
			result.toNetMsg().writeTo(response.getOutputStream());
		}
	}

	private BatchInfo processFromUpload(FileItem item, HttpServletResponse hsr) throws Exception{
		String fname = item.getName();
		fname = fname.substring(fname.lastIndexOf("\\") + 1, fname.length());
		BatchInfo batchInfo = null;
		if (fname.endsWith(OpeMetaFileUtils.PBOPEEXT)) {
			// pb对象转换为po对象
			MsgBatchInfo mbatch = MsgBatchInfo.parseFrom(item.getInputStream());
			batchInfo = BatchInfo.fromNetMsg(mbatch);
			logger.info("获得批次元数据信息, batchId:[" + batchInfo.getBatchId() + "]");

			if (batchInfo.isFileDataComplete()) { // 一次上传所有文件
				manageService.addBatch(batchInfo);
				result.setStatus(EResultStatus.eSuccess);
			} else {
				// check invoice
				List<InvoiceInfo> invoiceInfos = manageService.checkInvoice(batchInfo);
				if (!invoiceInfos.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					for (InvoiceInfo invoiceInfo : invoiceInfos) {
						sb.append(String.format("发票号码%s，曾在%s,在批次%s由%s已经提交过\r\n", invoiceInfo.getInvoiceNo(),
								invoiceInfo.getCreatetime(), invoiceInfo.getBatchId(), invoiceInfo.getAuthor()));
					}
					result.setStatus(EResultStatus.eFailed);
					result.setMsg(sb.toString());
				} else {
					//更新文件序号，现在控件传过来的文件序号有问题
					batchInfo.updateFileNo();
					manageService.addBatchWithoutData(batchInfo); // 批次和文件分别上传
					batchs.put(batchInfo.getBatchId(), batchInfo);
					result.setStatus(EResultStatus.eSuccess);
				}
			}
		} else if (fname.endsWith(OpeMetaFileUtils.PBDataExt)) {
			MsgFileInfo mfileinfo = MsgFileInfo.parseFrom(item.getInputStream());
			if (batchs.containsKey(mfileinfo.getBatchNO13())) {
				batchInfo = batchs.get(mfileinfo.getBatchNO13());
				FileInfo fileInfo = FileInfo.FromPBMsg(mfileinfo);
				logger.info(
						"获得批次数据信息, batchId:[" + batchInfo.getBatchId() + "], fileId:[" + fileInfo.getFileId() + "]");
				batchInfo.updateFileData(fileInfo);
				manageService.addFile(batchInfo, fileInfo);
				if (batchInfo.isFileDataComplete()) {
					batchs.remove(batchInfo.getBatchId());
				}
				
			} else {
				throw new Exception("上传未知批次内容，请重新上传");
			}
			result.setStatus(EResultStatus.eSuccess);
		} else {
			// 上传数据内容不对
			throw new Exception("上传数据内容的扩展名非" + OpeMetaFileUtils.PBDataExt + "或者" + OpeMetaFileUtils.PBOPEEXT + "服务拒绝");
		}
		return batchInfo;
	}

	/*
	 * private NResultInfo ProcessBatchData(FileItem item) throws IOException {
	 * NResultInfo nresult = new NResultInfo();
	 * 
	 * TigEraLogger.Log("上传.0.获得元数据");
	 * 
	 * MsgBatchInfo msgbatch = MsgBatchInfo.parseFrom(item.getInputStream());
	 * NBatchInfo nbatch = NBatchInfo.FromNetMsg(msgbatch);
	 * 
	 * if (nbatch.IsFileDataComplete()) { TigEraLogger.Log("上传.1.批次采用单文件");
	 * 
	 * nresult = BatchOpeFactory.GetBatchOpe().ProcessBatch(nbatch);
	 * 
	 * } else { TigEraLogger.Log("上传.2.1. 批次采分文件");
	 * 
	 * /////// // 创建目录信息等。
	 * BatchOpeFactory.GetBatchOpe().ProcessBatchHead(nbatch);
	 * _batchinfocache.put(nbatch.getBatchNO(), nbatch);// 暂时先缓存下来 } return
	 * nresult; }
	 * 
	 * private NResultInfo ProcessBatchFileData(FileItem item) throws
	 * IOException {
	 * 
	 * NResultInfo nresult = new NResultInfo();
	 * TigEraLogger.Log("上传。2.2.获取文件数据");
	 * 
	 * MsgFileInfo mfileinfo = MsgFileInfo.parseFrom(item.getInputStream()); if
	 * (_batchinfocache.containsKey(mfileinfo.getBatchNO13())) { NFileInfo
	 * nfileinfo = NFileInfo.FromPBMsg(mfileinfo);
	 * 
	 * NBatchInfo nbatch = _batchinfocache.get(mfileinfo.getBatchNO13());
	 * 
	 * BatchOpeFactory.GetBatchOpe().ProcessBatchFile(nbatch, nfileinfo);
	 * 
	 * // TODO: 文件数据完整后，进行ProcessBatch if (nbatch.IsFileDataComplete()) {
	 * TigEraLogger.Log("2.3采用分文件传送批次，批次完成"); // nresult =
	 * BatchOpeFactory.GetBatchOpe().ProcessBatch(nbatch);
	 * _batchinfocache.remove(nbatch.getBatchNO());; }
	 * 
	 * } else { nresult = new NResultInfo();
	 * nresult.setStatus(EResultStatus.eFailed);
	 * nresult.setMsg("上传未知批次的内容，请重新上传整个批次"); } return nresult; }
	 */

	public static void main(String[] args) {
		// String
		// json="{'createTime':'','files':[{'fileSize':'','fileId':'0001','createTime':'','fileMd5':'','localPath':'f:\\tmp\\0005.jpg','fileName':'0005.jpg','lastModTime':'','mimeType':'image/png','version':0,'creator':''},{'fileSize':'','fileId':'0002','createTime':'','fileMd5':'','localPath':'f:\\tmp\\111.png','fileName':'111.png','lastModTime':'','mimeType':'image/gif','version':0,'creator':''}],'syncTime':'','state':'','serialNumber':'','batchId':'3ac99a22-cf15-4870-a6c5-9ec8b95f62f3','lastModTime':'','orgId':'o999','syncTotalTime':'','syncType':'','sysId':'111','syncState':'','sourceIp':'','tellerNo':'','spareOne':''}";

	}
}
