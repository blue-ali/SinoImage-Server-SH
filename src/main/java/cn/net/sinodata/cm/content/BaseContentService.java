package cn.net.sinodata.cm.content;

import java.io.File;
import java.util.List;

import ch.qos.logback.core.util.FileUtil;
import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.util.DateUtil;


public abstract class BaseContentService implements IContentService {

	protected final String SEPARATOR = File.separator;
	
	protected abstract List<? extends BaseContent> batchInfo2Content(BatchInfo batchInfo);
	
	/**
	 * 构造保存路径，/SysId/CreateTime/OrgId/BatchId
	 * @param batchInfo
	 * @return
	 */
	protected String buildPath(BatchInfo batchInfo){
		
		StringBuffer sb = new StringBuffer(GlobalVars.local_root_path);
		sb.append(SEPARATOR);
		String sid = batchInfo.getSysId();
		String oid = batchInfo.getOrgId();
		if(sid==null||"".equals(sid)){
			sid="1212";
		}
		if(oid==null||"".equals(oid)){
			oid="test";
		}
		sb.append(sid);
		sb.append(SEPARATOR);
		sb.append(DateUtil.format(batchInfo.getCreateTime(), GlobalVars.fs_date_format));
		sb.append(SEPARATOR);
		sb.append(oid);
		sb.append(SEPARATOR);
		sb.append(batchInfo.getBatchId());
		return sb.toString();
	}
}
