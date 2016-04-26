package cn.net.sinodata.cm.hibernate.po;

import java.io.Serializable;

public class FileInfoKey implements Serializable {

	private static final long serialVersionUID = -8256348112071196808L;

	private String batchId;

	private String fileId;

	public FileInfoKey() {
	}

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

	 /** 
     * 覆盖hashCode方法，必须要有 
     */ 
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (batchId == null ? 0 : batchId.hashCode());
		result = PRIME * result + (fileId == null ? 0 : fileId.hashCode());
		return result;
	}

	/**
	 * 覆盖equals方法，必须要有
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FileInfoKey))
			return false;
		FileInfoKey objKey = (FileInfoKey) obj;
		if (batchId.equalsIgnoreCase(objKey.batchId)
				&& fileId.equalsIgnoreCase(objKey.fileId)) {
			return true;
		}
		return false;
	}
}
