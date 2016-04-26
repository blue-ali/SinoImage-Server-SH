package cn.net.sinodata.cm.content.fs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import cn.net.sinodata.cm.content.BaseContent;
import cn.net.sinodata.cm.content.BaseContentService;
import cn.net.sinodata.cm.hadoop.hbase.HBaseDao;
import cn.net.sinodata.cm.hadoop.hbase.HData;
import cn.net.sinodata.cm.hadoop.hbase.HQuery;
import cn.net.sinodata.cm.hadoop.hbase.HResult;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.pb.LogosFileTransfer.EOperType;
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
			map.put(fileInfo.getFileId(), fileInfo);
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
		if (EOperType.UPD.equals(fileOper) || EOperType.ADD.equals(fileOper)) {
			FileUtil.byte2file(fileData, path, fileName);
		} else if (EOperType.DEL.equals(fileOper)) {
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
//		HBaseDao  dao = new HBaseDao();
//		List<HData> hdatas = new ArrayList<HData>();
		for (FileInfo fileInfo : fileInfos) {
			FileUtil.byte2file(fileInfo.getData(), path, fileInfo.getFileId());
			
			//Hbase处理，把二进制数据放到Hbase中
//			HData hdata = new HData();
//			hdata.setColumnFamily("F");
//			hdata.setRowkey(String.valueOf(fileInfo.getFileMd5()));//用MD5做主键
//			hdata.addKV("fileName", Bytes.toBytes(fileInfo.getFileId()));
//			hdata.addKV("content", fileInfo.getData());
//			hdatas.add(hdata);
		}
//		dao.upsert("tb_image1", hdatas);
		
		
	}

	@Override
	public void delContent(BatchInfo batchInfo, List<FileInfo> delFiles) throws Exception {
		String path = buildPath(batchInfo);
		ensureFolder(path, true);
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		for (FileInfo fileInfo : fileInfos) {
			File file = new File(path + fileInfo.getFileId());
			if(file.exists())
				file.delete();
		}
	}
}
