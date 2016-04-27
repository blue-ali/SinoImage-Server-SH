package cn.net.sinodata.cm.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;

public class BeanUtil {
	
	public static BatchInfo batchInfo2BatchPO(BatchInfo batchInfo){
		BatchInfo batchPO = new BatchInfo();
		batchPO.setBatchId(batchInfo.getBatchId());
		batchPO.setSysId(batchInfo.getSysId());
		batchPO.setOrgId(batchInfo.getOrgId());
		batchPO.setCreateTime(batchInfo.getCreateTime());
		batchPO.setLastModified(batchInfo.getLastModified());
		List<FileInfo> fileList = batchInfo.getFileInfos();
		StringBuilder sb = new StringBuilder();
		Iterator<FileInfo> iterator = fileList.iterator();
		while (iterator.hasNext()) {
			FileInfo fileInfo = (FileInfo) iterator.next();
			sb.append(fileInfo.getFileId());
			if(iterator.hasNext()){
				sb.append(",");
			}
		}
		batchPO.setFileIds(sb.toString());
		return batchPO;
	}
	
	/*public static BatchInfo batchModel2BatchInfo(MBatch batchModel, List<MFile> fileModels){
		BatchInfo batchInfo = new BatchInfo();
		batchInfo.setBatchId(batchModel.getBatchId());
		
		batchInfo.setCreateTime(batchModel.getCreateTime());
		batchInfo.setLastModTime(batchModel.getLastModified());
		batchInfo.setOrgId(batchModel.getOrgId());
		batchInfo.setSysId(batchModel.getSysId());
		for (MFile fileMode : fileModels) {
			batchInfo.addFile(fileModel2FileInfo(fileMode));
		}
		return batchInfo;
	}*/
	
	public static FileInfo fileInfo2FilePO(final FileInfo fileInfo, final String batchId){
		FileInfo filePO = new FileInfo();
		filePO.setFileId(fileInfo.getFileId());
		filePO.setBatchId(batchId);
		filePO.setFileName(fileInfo.getFileName());
		return filePO;
	}
	/*
	public static JcrContent fileInfo2JcrContent(FileInfo fileInfo, String batchId, Map<String, byte[]> fileContents){
		JcrContent jcrContent = new JcrContent();
		jcrContent.setFileId(fileInfo.getFileId());
		jcrContent.setFileName(fileInfo.getFileName());
//		jcrContent.setSuffix(fileInfo.ge);
		jcrContent.setLastModified(Calendar.getInstance());
		jcrContent.setData(fileContents.get(fileInfo.getFileId()));
		jcrContent.setMimeType(fileInfo.getMimeType());
		return jcrContent;
	}
	*/
/*	public static JcrContent fileInfo2JcrContent(FileInfo fileInfo){
		JcrContent jcrContent = new JcrContent();
		jcrContent.setFileId(fileInfo.getFileId());
		jcrContent.setFileName(fileInfo.getFileName());
//		jcrContent.setSuffix(fileInfo.ge);
		jcrContent.setLastModified(Calendar.getInstance());
		jcrContent.setData(fileInfo.getData());
		jcrContent.setMimeType(fileInfo.getMimeType());
		return jcrContent;
	}
	
	public static FileInfo jcrContent2FileInfo(JcrContent jcrContent){
		FileInfo fileInfo = new FileInfo();
//		fileInfo.setCreateTime(jcrContent.get);
		fileInfo.setFileId(jcrContent.getFileId());
		fileInfo.setFileName(jcrContent.getFileName());
		fileInfo.setLastModTime(jcrContent.getLastModified().getTime().toString());
		fileInfo.setMimeType(jcrContent.getMimeType());
		return fileInfo;
	}*/

}
