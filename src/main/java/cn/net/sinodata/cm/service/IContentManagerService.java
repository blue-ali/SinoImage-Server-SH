package cn.net.sinodata.cm.service;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;

public interface IContentManagerService {

	/**
	 * 添加批次
	 * @param batchInfo
	 * @throws Exception
	 */
	public void addBatch(BatchInfo batchInfo) throws Exception;
	
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
	
	public byte[] getContent(BatchInfo batchInfo) throws Exception;

}
