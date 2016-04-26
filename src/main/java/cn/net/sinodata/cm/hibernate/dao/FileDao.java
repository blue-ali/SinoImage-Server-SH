package cn.net.sinodata.cm.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.tigera.document.definition.TigEraFileTransfer.EOperType;

import cn.net.sinodata.cm.hibernate.po.FileInfo;

@Repository
public class FileDao extends GenericDao<FileInfo>{

	public FileDao() {
		super(FileInfo.class);
	}
	
	public void saveOrDel(List<FileInfo> fileInfos){
		List<FileInfo> delFiles = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			if(fileInfo.getOperation() == EOperType.eDEL){
				fileInfos.remove(fileInfo);
				delFiles.add(fileInfo);
			}
		}
		save(fileInfos);
		delete(delFiles);
	}
	
	@SuppressWarnings("unchecked")
	public List<FileInfo> queryListByBatchId(String batchId){
		String hql = "from FileInfo where batchId=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		setQueryParams(query, new String[]{batchId});
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<FileInfo> queryListByBatchId4Test(String batchId){
		String hql = "from FileInfo where batchId=?";
		Query query = sessionFactory.openSession().createQuery(hql);
		setQueryParams(query, new String[]{batchId});
		return query.list();
	}
}
