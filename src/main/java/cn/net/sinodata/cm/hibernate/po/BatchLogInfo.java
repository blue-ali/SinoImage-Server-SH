package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cm_batch_log")
public class BatchLogInfo
  implements Serializable
{
  private static final long serialVersionUID = 7023038900836754920L;
  @Id
  @Column(name="logid")
  private String logId;
  @Column(name="batchid")
  private String batchId;
  @Column(name="createtime")
  private Date createTime;
  @Column(name="opertime")
  private Date operTime;
  @Column(name="operation")
  private String operation;
  @Column(name="userid")
  private String userId;
  @Column(name="orgid")
  private String orgId;
  
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
  
  public Date getCreateTime()
  {
    return this.createTime;
  }
  
  public void setCreateTime(Date createTime)
  {
    this.createTime = createTime;
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
  
  public String getOrgId()
  {
    return this.orgId;
  }
  
  public void setOrgId(String orgId)
  {
    this.orgId = orgId;
  }
  
  public static BatchLogInfo fromBatchInfo(BatchInfo batchInfo)
  {
    BatchLogInfo batchLogInfo = new BatchLogInfo();
    batchLogInfo.setLogId(UUID.randomUUID().toString());
    batchLogInfo.setBatchId(batchInfo.getBatchId());
    batchLogInfo.setCreateTime(batchInfo.getCreateTime());
    batchLogInfo.setOperation(batchInfo.getOperation().toString());
    batchLogInfo.setOperTime(new Date());
    batchLogInfo.setOrgId(batchInfo.getOrgId());
    batchLogInfo.setUserId(batchInfo.getCreator());
    return batchLogInfo;
  }
}
