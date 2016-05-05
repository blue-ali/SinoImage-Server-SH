package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.jdbc.BatchingBatcher;

/**
 * @author manan
 *
 */
@Entity
@Table(name = "cm_invoice_info")
public class InvoiceInfo implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -853995059723485151L;
	/** 发票号 */
	@Id
	@Column(name = "invoice_no")
	private String invoiceNo;
	
	/** 提交时间*/
	@Column(name = "batchid")
	private String batchId;
	
	/** 文件名 */
	@Column(name = "filename")
	private String fileName;
	
	/** 提交人*/
	@Column(name = "author")
	private String author;
	
	/** 提交时间*/
	@Column(name = "createtime")
	private Date createtime;

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public static InvoiceInfo fromFileInfo(BatchInfo batchInfo, FileInfo fileInfo){
		InvoiceInfo invoiceInfo = new InvoiceInfo();
		invoiceInfo.setInvoiceNo(fileInfo.getInvoiceNo());
		invoiceInfo.setBatchId(batchInfo.getBatchId());
		invoiceInfo.setCreatetime(batchInfo.getCreateTime());
		invoiceInfo.setFileName(fileInfo.getFileName());
		invoiceInfo.setAuthor(batchInfo.getCreator());
		return invoiceInfo;
	}
}
