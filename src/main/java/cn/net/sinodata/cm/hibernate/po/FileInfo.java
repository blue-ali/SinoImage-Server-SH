package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.protobuf.ByteString;
import com.tigera.document.definition.TigEraFileTransfer.EOperType;
import com.tigera.document.definition.TigEraFileTransfer.MsgFileInfo;

import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.util.DateUtil;


@Entity
@Table(name = "cm_file_info")
@IdClass(FileInfoKey.class)
public class FileInfo implements Serializable{

	private static final long serialVersionUID = -4456286447538350463L;
	/** 批次号 */
	@Id
	@Column(name = "batchid")
	private String batchId;
	/** 影像ID */
	@Id
	@Column(name = "fileid")
	private String fileId;
	/** 影像名称 */
	@Column(name = "filename")
	private String fileName;
    /** MD5码 */
	@Column(name = "MD5")
    private String fileMd5;
	/** 提交时间 */
	@Column(name="createtime")
    private Date createTime;
	@Column(name="creator")
    /** 创建者 */
    private String creator;
	@Transient
    /** 最后修改时间*/
    private String lastModTime;
	@Transient
    /** 版本信息， 用于下载，只能get */
    private String version;
	@Transient
    /** 文件类型 */
    private String mimeType;
	@Transient
    /** 影像大小 */
    private int fileSize;
	@Transient
	/** 文件内容 */
	private byte[] data;
	
	@Transient
	private EOperType operation;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(String lastModTime) {
		this.lastModTime = lastModTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public EOperType getOperation() {
		return operation;
	}

	public void setOperation(EOperType operation) {
		this.operation = operation;
	}

	public MsgFileInfo ToPBMsg()
	{
		MsgFileInfo.Builder mBuilder = MsgFileInfo.newBuilder();
		mBuilder.setBatchNO13(this.getBatchId());
		mBuilder.setCreateTime4(this.getCreateTime().getHours());
		mBuilder.setCreateDate3(this.getCreateTime().getDate());
		mBuilder.setAuthor1(this.getCreator());
		mBuilder.setFileMD59(this.getFileMd5());
		mBuilder.setFileName6(this.getFileName());
		mBuilder.setFileNO8(this.getFileId());
		mBuilder.setFileSize10(this.getFileSize());
//		mBuilder.setFileURLBytes(this.getfile)
//		mBuilder.setVersion(this.getVersion());
		
		if (this.getData() != null)	{
			mBuilder.setData11(ByteString.copyFrom(this.getData()));
		}
		mBuilder.setOperation12(EOperType.eFROM_SERVER_NOTCHANGE);
		
//		ret.setBatchNOLink(this.getBatchNOLink());
		
		// ret.getNotesList().clear();
//		for (NNoteInfo info : this.getNotesList())
//		{
//			ret.addNotes(info.ToPBMsg());
//		}
		
		return mBuilder.build();
	}
	
	public static FileInfo FromPBMsg(MsgFileInfo input) throws ParseException
	{
		FileInfo fileInfo = new FileInfo();
		fileInfo.setBatchId(input.getBatchNO13());
		//TODO parse date
		fileInfo.setCreateTime(new Date());
		fileInfo.setCreator(input.getAuthor1());
		fileInfo.setFileId(input.getFileNO8());
		fileInfo.setFileMd5(input.getFileMD59());
		fileInfo.setFileName(input.getFileName6());
		fileInfo.setFileSize(input.getFileSize10());
//		fileInfo.setLastModTime(input.get);
		fileInfo.setOperation(input.getOperation12());
//		fileInfo.setVersion(input.getVersion());
		
		if (input.getData11() != null)
		{
			fileInfo.setData(input.getData11().toByteArray());
		}
		/*ret.getNotesList().clear();
		for (MsgNoteInfo info : input.getNotesList())
		{
			ret.getNotesList().add(NNoteInfo.FromPBMsg(info));
		}*/
		
		return fileInfo;
	}
	
}