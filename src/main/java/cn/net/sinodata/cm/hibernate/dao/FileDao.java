package cn.net.sinodata.cm.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.hibernate.po.FileLogInfo;
import cn.net.sinodata.cm.pb.ProtoBufInfo.EOperType;

@Repository
public class FileDao extends GenericDao<FileInfo> {

	public FileDao() {
		super(FileInfo.class);
	}

	public void saveOrDel(List<FileInfo> fileInfos) {
		List<FileInfo> delFiles = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() == EOperType.eDEL) {
				fileInfos.remove(fileInfo);
				delFiles.add(fileInfo);
			}
		}
		save(fileInfos);
		delete(delFiles);
	}

	public void save(List<FileInfo> fileInfos, boolean withLog) {
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() != EOperType.eFROM_SERVER_NOTCHANGE) {
				sessionFactory.getCurrentSession().saveOrUpdate(fileInfo);
				sessionFactory.getCurrentSession().save(FileLogInfo.fromFileInfo(fileInfo));
			}
		}
	}

	public void delete(List<FileInfo> fileInfos, boolean withLog) {
		for (FileInfo fileInfo : fileInfos) {
			if (fileInfo.getOperation() != EOperType.eFROM_SERVER_NOTCHANGE) {
				sessionFactory.getCurrentSession().delete(fileInfo);
				sessionFactory.getCurrentSession().save(FileLogInfo.fromFileInfo(fileInfo));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<FileInfo> queryListByBatchId(String batchId) {
		String hql = "from FileInfo where batchId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, new String[] { batchId });
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<FileInfo> queryListByBatchId4Test(String batchId) {
		String hql = "from FileInfo where batchId=?";
		Query query = sessionFactory.openSession().createQuery(hql);
		setQueryParams(query, new String[] { batchId });
		return query.list();
	}
}
