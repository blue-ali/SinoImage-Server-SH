package cn.net.sinodata.cm.service;

import java.util.List;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.hibernate.po.InvoiceInfo;

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
	
	/**
	 * 检查发票是否提交过
	 * @param batchInfo
	 * @throws Exception
	 */
	public List<InvoiceInfo> checkInvoice(BatchInfo batchInfo) throws Exception;
	
	public byte[] getContent(BatchInfo batchInfo) throws Exception;
	
	/**
	 * 更新批次审核状态
	 * @param batchVerifyInfo
	 * @param fileVerifyInfo
	 */
	public void updateBatchVerifyState(String[] batchVerifyInfo, String[] fileVerifyInfo);

}
