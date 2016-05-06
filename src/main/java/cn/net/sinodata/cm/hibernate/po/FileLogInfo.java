package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cm_file_log")
public class FileLogInfo
  implements Serializable
{
  private static final long serialVersionUID = 2515484923133034420L;
  @Id
  @Column(name="logid")
  private String logId;
  @Column(name="batchid")
  private String batchId;
  @Column(name="fileid")
  private String fileId;
  @Column(name="filename")
  private String fileName;
  @Column(name="opertime")
  private Date operTime;
  @Column(name="operation")
  private String operation;
  @Column(name="userid")
  private String userId;
  @Column(name="invoice_no")
  private String invoiceNo;
  
  public String getLogId()
  {
    return this.logId;
  }
  
  public void setLogId(String logId)
  {
    this.logId = logId;
  }
  
  public String getBatchId()
  {
    return this.batchId;
  }
  
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
  
  public String getFileId()
  {
    return this.fileId;
  }
  
  public void setFileId(String fileId)
  {
    this.fileId = fileId;
  }
  
  public String getFileName()
  {
    return this.fileName;
  }
  
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  public Date getOperTime()
  {
    return this.operTime;
  }
  
  public void setOperTime(Date operTime)
  {
    this.operTime = operTime;
  }
  
  public String getOperation()
  {
    return this.operation;
  }
  
  public void setOperation(String operation)
  {
    this.operation = operation;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public String getInvoiceNo()
  {
    return this.invoiceNo;
  }
  
  public void setInvoiceNo(String invoiceNo)
  {
    this.invoiceNo = invoiceNo;
  }
  
  public static FileLogInfo fromFileInfo(FileInfo fileInfo)
  {
    FileLogInfo fileLogInfo = new FileLogInfo();
    fileLogInfo.setLogId(UUID.randomUUID().toString());
    fileLogInfo.setBatchId(fileInfo.getBatchId());
    fileLogInfo.setOperation(fileInfo.getOperation().toString());
    fileLogInfo.setOperTime(new Date());
    fileLogInfo.setUserId(fileInfo.getCreator());
    fileLogInfo.setFileId(fileInfo.getFileId());
    fileLogInfo.setFileName(fileInfo.getFileName());
    fileLogInfo.setInvoiceNo(fileInfo.getInvoiceNo());
    return fileLogInfo;
  }
}
