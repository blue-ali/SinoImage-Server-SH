package cn.net.sinodata.cm.content.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.net.sinodata.cm.common.Constants;
import cn.net.sinodata.cm.content.BaseContent;
import cn.net.sinodata.cm.content.BaseContentService;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.framework.util.FileUtil;

@Service("fsService")
public class FileSystemServiceImpl extends BaseContentService{

	@Override
	public Object ensureFolder(String path, boolean createFolder) {
		File file = new File(path);
		
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}

	@Override
	public void regist() {
		
	}

	@Override
	public void addContent(BatchInfo batchInfo) throws Exception {
		String path = buildPath(batchInfo);
		ensureFolder(path, true);
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		for (FileInfo fileInfo : fileInfos) {
			FileUtil.byte2file(fileInfo.getData(), path, fileInfo.getFileName());
		}
	}
	

	@Override
	public Object getContent(BatchInfo batchInfo) throws Exception {
		String path = buildPath(batchInfo);
//		path = "f://tmp//download";
		File dir = new File(path);
		if(!dir.exists()){
			throw new Exception("路径不存在：" + path);
		}else{
			
			List<File> files = Arrays.asList(dir.listFiles());
			List<FileInfo> fileInfos = batchInfo.getFileInfos();
			file2FileInfo(fileInfos, files);
			return batchInfo;
		}
	}

	@Override
	protected List<? extends BaseContent> batchInfo2Content(BatchInfo batchInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	private void file2FileInfo(List<FileInfo> fileInfos, List<File> files) throws IOException{
		Map<String, FileInfo> map = new HashMap<String, FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			map.put(fileInfo.getFileName(), fileInfo);
		}
		for (File file : files) {
			FileInfo fileInfo = map.get(file.getName());
			if(fileInfo != null)
				fileInfo.setData(FileUtil.file2byte(file));;
		}
	}
	
	public static void main(String[] args) throws Exception {
//		String path = "F:\\tmp\\download";
//		File dir = new File(path);
//		if(!dir.exists()){
//			throw new Exception("·�������ڣ�" + path);
//		}else{
//			File[] files = dir.listFiles();
//			List<File> fileList = Arrays.asList(files);
//			
//		}
		BatchInfo batchInfo = new BatchInfo();
		FileInfo fileInfo = new FileInfo();
		fileInfo.setData(FileUtil.file2byte("F:\\tmp\\download\\001"));
		fileInfo.setFileId("001");
		
		FileInfo fileInfo1 = new FileInfo();
		fileInfo1.setData(FileUtil.file2byte("F:\\tmp\\download\\002"));
		fileInfo1.setFileId("002");
		
		batchInfo.addFileInfo(fileInfo);
		batchInfo.addFileInfo(fileInfo1);
		
		new FileSystemServiceImpl().getContent(batchInfo);
	}

	@Override
	public void modifyContentFile(final BatchInfo batchInfo,
			final FileInfo fileInfo) throws Exception {
		String path = buildPath(batchInfo);
		EOperType fileOper = fileInfo.getOperation();
		String fileName = fileInfo.getFileId();
		byte[] fileData = fileInfo.getData();
		if (EOperType.eUPD.equals(fileOper) || EOperType.eADD.equals(fileOper)) {
			FileUtil.byte2file(fileData, path, fileName);
		} else if (EOperType.eDEL.equals(fileOper)) {
			FileUtil.deleteFile(path + SEPARATOR + fileName);
		} else {
			throw new Exception("Unsupported file operation [" + fileOper + "]");
		}
	}

	@Override
	public void updContent(BatchInfo batchInfo, List<FileInfo> files) throws Exception {
		String path = buildPath(batchInfo);
		ensureFolder(path, true);
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		for (FileInfo fileInfo : fileInfos) {
			if(fileInfo.getOperation() == EOperType.eADD || fileInfo.getOperation() == EOperType.eUPDATEFILE)
			FileUtil.byte2file(fileInfo.getData(), path, fileInfo.getFileName());
		}
		
	}
	
	@Override
	public void updContent(BatchInfo batchInfo, FileInfo fileInfo) throws Exception {
		String path = buildPath(batchInfo);
		ensureFolder(path, true);
		if(fileInfo.getOperation() == EOperType.eDEL){
			FileUtil.deleteFile(path + File.separator + fileInfo.getFileName());
		}
		FileUtil.byte2file(fileInfo.getData(), path, fileInfo.getFileName());
	}

	@Override
	public void delContent(BatchInfo batchInfo, List<FileInfo> delFiles) throws Exception {
		String path = buildPath(batchInfo);
		ensureFolder(path, true);
//		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		for (FileInfo fileInfo : delFiles) {
			File file = new File(path + File.separator + fileInfo.getFileName());
			if(file.exists())
				file.delete();
		}
	}

	@Override
	public void saveContent(BatchInfo batchInfo) throws Exception {
		String fileName = getBatchNameNextVersion(batchInfo);
		// info.RemoveFilesData();
		byte[] data = batchInfo.toNetMsg().toByteArray();
		FileUtil.byte2file(data, fileName);
	}
}
