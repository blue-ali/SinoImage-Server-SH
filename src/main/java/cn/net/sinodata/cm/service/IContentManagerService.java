package cn.net.sinodata.cm.service;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;

public interface IContentManagerService {

	/**
	 * 添加批次
	 * @param batchInfo
	 * @throws Exception
	 */
	public void addBatch(BatchInfo batchInfo) throws Exception;
	
	/**
	 * 只添加批次信息，不处理文件
	 * @param batchInfo
	 * @throws Exception
	 */
	public void addBatchWithoutData(BatchInfo batchInfo) throws Exception;
	/**
	 * 添加、更新批次
	 * @param batchInfo
	 * @throws Exception
	 */
	public void upsertBatch(BatchInfo batchInfo) throws Exception;
	

	/**
	 * 删除批次
	 * @param batchInfo
	 */
	public void deleteBatch(BatchInfo batchInfo);
	
	/**
	 * 获取批次
	 * @param batchId
	 * @return
	 * @throws Exception
	 */
	public BatchInfo getBatch(String batchId) throws Exception;
	
	/**
	 * 往批次添加文件
	 * @param batchInfo
	 * @throws Exception
	 */
	public void addFile(BatchInfo batchInfo, FileInfo fileInfo) throws Exception;
	
	public byte[] getContent(BatchInfo batchInfo) throws Exception;

}
