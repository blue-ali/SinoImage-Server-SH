package cn.net.sinodata.cm.util;

import java.io.FileInputStream;
import java.io.IOException;

import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;
import cn.net.sinodata.cm.pb.ProtoBufInfo.MsgBatchInfo;

public class OpeMetaFileUtils
{
	public static String	PBOPEEXT	= ".pbope";	// proto buffer operation
	public static String	PBDataExt	= ".pbdata";
										
	// 解析从客户端上传上来的MetaFile
	public static MsgBatchInfo ParseFile(String fname) throws IOException
	{
		FileInputStream input = new FileInputStream(fname);
		MsgBatchInfo info = MsgBatchInfo.parseFrom(input);
		return info;
	}
	
	// 服务器存储整个操作的最新版本的快照
	public static BatchInfo MergeOpe(BatchInfo lastsnapshot, BatchInfo newope)
	{
		BatchInfo ret = new BatchInfo();
		if (newope.getOperation() == EOperType.eDEL)
		{
			//
			return newope;
		}
		if (newope.getOperation() == EOperType.eUPD)
		{
			for (FileInfo fileinfo : newope.getFileInfos())
			{
				
//				for (NNoteInfo noteinfo : fileinfo.getNotesList())
//				{
//				
//				}
			}
		}
		if (newope.getOperation() == EOperType.eADD)
		{
			newope.setOperation(EOperType.eFROM_SERVER_NOTCHANGE);
			for (FileInfo item : newope.getFileInfos())
			{
			
			}
			return newope;
		}
		return null;
	}
	
}
