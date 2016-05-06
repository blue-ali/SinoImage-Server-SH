/**
 * 
 */
package cn.net.sinodata.cm.service.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.hibernate.po.InvoiceInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.service.BaseService;
import cn.net.sinodata.cm.service.IContentManagerService;
import cn.net.sinodata.cm.util.DateUtil;
import cn.net.sinodata.cm.util.ZipUtil;
import cn.net.sinodata.framework.exception.SinoException;

/**
 * @author manan
 * 
 */

@Service("manageService")
@Transactional(rollbackFor = Exception.class)
public class ContentManageServiceImpl extends BaseService implements IContentManagerService {
	protected final String SEPARATOR = File.separator;

	// @Override
	public BatchInfo _getBatch(BatchInfo batchInfo) throws Exception {
		String batchId = batchInfo.getBatchId();
		batchInfo = batchDao.queryById(batchId);
		if (batchInfo == null) {
			throw new SinoException("待获取批次" + batchId + "不存在！");
		}
		List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
		batchInfo.setFileInfos(fileInfos);
		return batchInfo;
	}

	/*
	 * @Override public BatchInfo getBatch(String batchId) throws Exception { //
	 * String batchId = batchInfo.getBatchId(); BatchInfo batchInfo =
	 * batchDao.queryById(batchId); if (batchInfo == null) { throw new
	 * SinoException("待获取批次" + batchId + "不存在！"); } List<FileInfo> fileInfos =
	 * fileDao.queryListByBatchId(batchId); batchInfo.setFileInfos(fileInfos);
	 * HBaseDao hbaseDao = new HBaseDao(); // String path =
	 * buildPath(batchInfo); for (FileInfo fileInfo : fileInfos) { //
	 * Hbase处理，把二进制数据放到fileInfo中 返回到控件 HQuery hquery = new HQuery();
	 * hquery.setColumFamily("F"); hquery.setColumnName("content");
	 * hquery.setRowkey(fileInfo.getFileMd5());
	 * hquery.setTableName("tb_image1"); HResult result =
	 * hbaseDao.queryByRowkey(hquery); // String fileId = fileInfo.getFileId();
	 * // FileUtil.byte2file(result.getValue(),path , fileId);
	 * fileInfo.setData(result.getValue());
	 * 
	 * }
	 * 
	 * // contentService.getContent(batchInfo); return batchInfo; }
	 */

	@Override
	public BatchInfo getBatch(String batchId) throws Exception {
		BatchInfo batchInfo = batchDao.queryById(batchId);
		List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
		batchInfo.setFileInfos(fileInfos);
		contentService.getContent(batchInfo);
		for (FileInfo fileInfo : fileInfos) {
			fileInfo.setFileUrl(GlobalVars.download_url + buildRelaPath(batchInfo) + fileInfo.getFileName());
		}
		return batchInfo;
	}

	@Override
	public byte[] getContent(BatchInfo batchInfo) throws Exception {
		String batchId = batchInfo.getBatchId();
		String orgId = batchInfo.getOrgId();
		String sysId = batchInfo.getSysId();
		List<FileInfo> fileList = batchInfo.getFileInfos();
		Map<String, byte[]> fileBytes = new HashMap<String, byte[]>();
		for (FileInfo fileInfo : fileList) {
			String fileId = fileInfo.getFileId();
			String fileName = fileInfo.getFileName();
			String jcrPath = separator + sysId + separator + orgId + separator + batchId + separator + fileId;
			// JcrContent jcrContent = jcrService.getContent(jcrPath);
			// fileBytes.put(fileName, jcrContent.getData());
		}
		return ZipUtil.filesByte2Zip(fileBytes);
	}

	@Override
	public void deleteBatch(BatchInfo batch) {
		// TODO 删除批次
	}

	/**
	 * 上传批次+文件
	 */
	@Override
	public void addBatch(BatchInfo batchInfo) throws Exception {
		List<FileInfo> fileInfos = batchInfo.getFileInfos();

		List<FileInfo> delFiles = new ArrayList<FileInfo>();
		List<FileInfo> notChangedFiles = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() == EOperType.eDEL) {
				delFiles.add(fileInfo);
			} else if (fileInfo.getOperation() == EOperType.eFROM_SERVER_NOTCHANGE) {
				notChangedFiles.add(fileInfo);
			}
		}
		fileInfos.removeAll(delFiles);
		fileInfos.removeAll(notChangedFiles);

		// ---
		// 此处为了不更新createtime字段，hibernate注解updatable=false不好用，虽然update不包含该列但是值还是改变了，带研究
		BatchInfo originalBatchInfo = batchDao.queryById(batchInfo.getBatchId());
		if (originalBatchInfo != null) {
			batchInfoWithoutCreateTime(originalBatchInfo, batchInfo);
		} else {
			originalBatchInfo = batchInfo;
		}
		// ---

		batchDao.save(originalBatchInfo, true);
		fileDao.save(fileInfos, true);
		fileDao.delete(delFiles, true);
		// TODO 判断操作类型 updatebasic不保存
		contentService.updContent(batchInfo, fileInfos);
		contentService.delContent(batchInfo, delFiles);
	}

	/**
	 * 只保存批次和文件信息，不保存文件内容
	 */
	@Override
	public void addBatchWithoutData(BatchInfo batchInfo) throws Exception {
		// 重置审核记过
		resetVerifyState(batchInfo);
		// persist
		List<InvoiceInfo> addInvoiceInfos = batchInfo.getAddInvoiceInfos();
		List<InvoiceInfo> delInvoiceInfos = batchInfo.getDelInvoiceInfos();

		// ---
		// 此处为了不更新createtime字段，hibernate注解updatable=false不好用，虽然update不包含该列但是值还是改变了，带研究
		BatchInfo originalBatchInfo = batchDao.queryById(batchInfo.getBatchId());
		if (originalBatchInfo != null) {
			batchInfoWithoutCreateTime(originalBatchInfo, batchInfo);
		} else {
			originalBatchInfo = batchInfo;
		}
		// ---
		batchDao.save(originalBatchInfo, true);
		fileDao.save(batchInfo.getFileInfos(), true);
		invoiceDao.save(addInvoiceInfos);
		invoiceDao.delete(delInvoiceInfos);
		contentService.saveContent(batchInfo);

		// TODO save invoice and notify
	}

	@Deprecated
	@Override
	public void upsertBatch(BatchInfo batchInfo) throws Exception {
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		// 将需要更新和删除的文件分开
		List<FileInfo> delFiles = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() == EOperType.eDEL) {
				delFiles.add(fileInfo);
			}
		}
		fileInfos.removeAll(delFiles);

		batchDao.save(batchInfo);
		if (fileInfos.size() > 0) {
			fileDao.save(fileInfos, true);
			contentService.updContent(batchInfo, fileInfos);// Hbase处理，把二进制数据放到Hbase中
		}
		if (delFiles.size() > 0) {
			fileDao.delete(delFiles, true);
			contentService.delContent(batchInfo, delFiles);
		}
	}

	private String buildRelaPath(BatchInfo batchInfo) {
		StringBuffer sb = new StringBuffer(SEPARATOR);
		String sid = batchInfo.getSysId();
		String oid = batchInfo.getOrgId();
		if (sid == null || "".equals(sid)) {
			sid = "1212";
		}
		if (oid == null || "".equals(oid)) {
			oid = "test";
		}
		sb.append(sid);
		sb.append(SEPARATOR);
		sb.append(DateUtil.format(batchInfo.getCreateTime(), GlobalVars.fs_date_format));
		sb.append(SEPARATOR);
		sb.append(oid);
		sb.append(SEPARATOR);
		sb.append(batchInfo.getBatchId());
		sb.append(SEPARATOR);
		return sb.toString();
	}

	@Override
	public void addFile(BatchInfo batchInfo, FileInfo fileInfo) throws Exception {
		// fileDao.save(fileInfo);
		contentService.updContent(batchInfo, fileInfo);
		batchInfo.updateFileState(fileInfo);
	}

	@Override
	public List<InvoiceInfo> checkInvoice(BatchInfo batchInfo) throws Exception {
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		List<String> invoiceIds = new ArrayList<String>();
		for (FileInfo fileInfo : fileInfos) {
			if (EOperType.eDEL != fileInfo.getOperation() && EOperType.eFROM_SERVER_NOTCHANGE != fileInfo.getOperation()) {
				invoiceIds.add(fileInfo.getInvoiceNo());
			}
		}
		return invoiceDao.queryListByIds(invoiceIds);
	}

	private void resetVerifyState(BatchInfo batchInfo) {
		batchInfo.setVerifyResult(99);
		batchInfo.setVerifyRemark("");
		for (FileInfo fileInfo : batchInfo.getFileInfos()) {
			fileInfo.setVerifyResult(99);
			fileInfo.setVerifyRemark("");
		}
	}

	@Override
	public void updateBatchVerifyState(String[] batchVerifyInfo, String[] fileVerifyInfos) {
		String batchId = batchVerifyInfo[0];
		String batchState = batchVerifyInfo[1];

		BatchInfo batchInfo = batchDao.queryById(batchId);
		batchInfo.setVerifyResult(Integer.valueOf(batchState));
		// TODO 记录审批日志，修改BatchInfo的operation字段类型为string
		batchDao.save(batchInfo);

		if (!(fileVerifyInfos.length == 1 && fileVerifyInfos[0].equals(""))) {
			Map<String, Integer> fileVerifyMap = new HashMap<String, Integer>();
			for (String fileVerifyInfo : fileVerifyInfos) {
				String[] fileVerify = fileVerifyInfo.split("\\|");
				fileVerifyMap.put(fileVerify[0], Integer.valueOf(fileVerify[1]));
			}

			List<FileInfo> fileInfos = fileDao.queryListByBatchId(batchId);
			for (FileInfo fileInfo : fileInfos) {
				String fileId = fileInfo.getFileId();
				if (fileVerifyMap.keySet().contains(fileId)) {
					fileInfo.setVerifyResult(fileVerifyMap.get(fileInfo.getFileId()));
				}
			}
			// TODO 记录审批日志，修改FileInfo的operation字段类型为string
			fileDao.save(fileInfos);
		}
	}

	private void batchInfoWithoutCreateTime(BatchInfo originalBatchInfo, BatchInfo batchInfo)
			throws IllegalAccessException, InvocationTargetException {
		Date date = originalBatchInfo.getCreateTime();
		BeanUtils.copyProperties(originalBatchInfo, batchInfo);
		originalBatchInfo.setCreateTime(date);
	}
}
