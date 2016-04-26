package cn.net.sinodata.cm.hibernate.po;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tigera.document.definition.TigEraFileTransfer.EOperType;
import com.tigera.document.definition.TigEraFileTransfer.MsgBatchInfo;
import com.tigera.document.definition.TigEraFileTransfer.MsgFileInfo;

import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.util.DateUtil;

@Entity
@Table(name = "cm_batch_info")
public class BatchInfo implements Serializable {

	private static final long serialVersionUID = -4489094278284547383L;
	/** 批次号 */
	@Id
	@Column(name = "batchid")
	private String batchId;
	/** 系统编号 */
	@Column(name = "sysid")
	private String sysId;
	/** 机构号 */
	@Column(name = "orgid")
	private String orgId;
	/** 创建时间 */
	@Column(name = "createtime")
	private Date createTime;
	@Column(name = "version")
	private String version;
	/** 最后修改时间 */
	@Column(name = "lastmodified")
	private Date lastModified;
	/** 包含的文件，用于文件索引持久化 */
	@Column(name = "files")
	private String fileIds;
	/** 创建人 */
	@Column(name = "creator")
	private String creator;
	/** 来源IP */
	@Transient
	private String sourceIp;
	/** 批次状态 */
	@Transient
	private String state;
	/** 同步状态 */
	@Transient
	private String syncState;
	/** 同步类型 */
	@Transient
	private String syncType;
	/** 同步时间 **/
	@Transient
	private String syncTime;
	/** 包含的文件，用于JCR持久化 */
	@Transient
	private List<FileInfo> fileInfos = new ArrayList<FileInfo>();
	@Transient
	private EOperType operation = EOperType.eFROM_SERVER_NOTCHANGE;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getFileIds() {
		return fileIds;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSyncState() {
		return syncState;
	}

	public void setSyncState(String syncState) {
		this.syncState = syncState;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public String getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}

	public List<FileInfo> getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(List<FileInfo> fileInfos) {
		this.fileInfos = fileInfos;
	}

	public EOperType getOperation() {
		return operation;
	}

	public void setOperation(EOperType operation) {
		this.operation = operation;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static BatchInfo FromNetMsg(MsgBatchInfo input) throws ParseException {
		BatchInfo batchInfo = new BatchInfo();
		// ret.setAuthor(input.getAuthor());
		batchInfo.setBatchId(input.getBatchNO6());
		//TODO parse date
		batchInfo.setCreateTime(new Date());
		batchInfo.setCreator(input.getAuthor1());
		// batchInfo.setFileIds(input.getfileid);
//		batchInfo.setLastModified(DateUtil.parse(input.get, GlobalVars.client_date_format));
		batchInfo.setOrgId(input.getOrgID10());
		batchInfo.setSourceIp(input.getSourceIP14());
		batchInfo.setSysId(input.getBusiSysId11());
		batchInfo.setOperation(input.getOperation8());
		batchInfo.setVersion(String.valueOf(input.getVersion2()));
		List<MsgFileInfo> mFileInfos = input.getFileinfos9List();
		if (mFileInfos != null) {
			for (MsgFileInfo mInfo : mFileInfos) {
				FileInfo fileInfo = FileInfo.FromPBMsg(mInfo);
				batchInfo.getFileInfos().add(fileInfo);
			}
		}

		return batchInfo;
	}

	public static BatchInfo fromPBFile(String fname) throws Exception {
		FileInputStream input = new FileInputStream(new File(fname));
		MsgBatchInfo msg = MsgBatchInfo.parseFrom(input);
		BatchInfo batchInfo = BatchInfo.FromNetMsg(msg);
		return batchInfo;
	}

	public MsgBatchInfo toNetMsg() {
		MsgBatchInfo.Builder mBuilder = MsgBatchInfo.newBuilder();
		mBuilder.setBatchNO6(this.getBatchId());
		mBuilder.setCreateDate3(this.getCreateTime().getDate());
		mBuilder.setAuthor1(this.getCreator());
//		mBuilder.setLastModified(DateUtil.format(this.getLastModified(), GlobalVars.client_date_format));
		mBuilder.setOrgID10(this.getOrgId());
		mBuilder.setBusiSysId11(this.getSysId());
		mBuilder.setVersion2(Integer.valueOf(this.getVersion()));
		mBuilder.setOperation8(EOperType.eFROM_SERVER_NOTCHANGE);
		// mBuilder.setOperation(this.getOperation());
		List<FileInfo> fileInfos = this.getFileInfos();
		if (fileInfos != null) {
			for (FileInfo info : this.fileInfos) {
				// ret.addFileinfos(info.ToPBMsg());
				mBuilder.addFileinfos9(info.ToPBMsg());
			}
			// mBuilder.setFileCount(fileInfos.size());
		} else {
			// ret.setFileCount(0); // 注：这个字段，或者应该清除，或者应该保留，保留的话的作用是作为校验作用
		}
		// ret.setBranch(this.getbranch());
		// ret.setTorgId(this.getTorgID());
		// ret.setBusiSysId(this.getBusiSysID());
		// ret.setBusiTypeId(this.getBusiSysID());
		// ret.setTellerNO(this.getTellNO());
		// ret.setBarCode(this.getBarCode());
		// ret.setSourceIP(this.getSourceIP());
		// ret.setMachineID(this.getMachineID());
		// ret.setPassword(this.getPassword());
		// if (this.getResultInfo() != null)
		// {
		// ret.setResultInfo(this.getResultInfo().ToNetMsg());
		// }
		return mBuilder.build();
	}

	public Boolean isFileDataComplete() {
		for (FileInfo fileinfo : this.getFileInfos()) {
			if (fileinfo.getOperation() == EOperType.eADD || fileinfo.getOperation() == EOperType.eUPD) {
				if (fileinfo.getData() == null) {
					return false;
				}
				return true;
			}
		}
		return true;
	}

	public Boolean updateFileData(FileInfo info) {
		if (info.getBatchId() != this.getBatchId()) {
			return false;
		}
		for (FileInfo fileinfo : this.getFileInfos()) {
			if (fileinfo.getFileName() == info.getFileName()) {
				// TODO,应该减少服务器内存使用，直接把数据落地为文件

				fileinfo.setData(info.getData());
				return true;
			}
		}
		return false;
	}

	public void addFileInfo(FileInfo fileInfo) {
		fileInfos.add(fileInfo);
	}
}
