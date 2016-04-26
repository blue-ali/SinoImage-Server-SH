package cn.net.sinodata.cm.content.jcr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.springframework.stereotype.Service;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import cn.net.sinodata.cm.content.BaseContent;
import cn.net.sinodata.cm.content.BaseContentService;
import cn.net.sinodata.cm.content.IContentService;
import cn.net.sinodata.cm.content.jcr.model.JcrContent;
import cn.net.sinodata.cm.content.jcr.model.JcrNodeType;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;
import cn.net.sinodata.cm.util.BeanUtil;
import cn.net.sinodata.framework.log.SinoLogger;
import cn.net.sinodata.framework.util.FileUtil;

//@Service("jcrService")
public class JcrServiceImpl extends BaseContentService {
	
	private final SinoLogger logger = SinoLogger.getLogger(this.getClass());

	@Resource
	private JcrTemplate jcrTemplate;

	/**
	 * 注册自定义属性
	 */
	public void regist(){
		jcrTemplate.execute(new JcrCallback() {
			
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				try {
					NodeTypeRegister.RegisterCustomNodeTypes(session,"/resources/jcr/custom-attribute.cnd");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}
	
	/**
	 *  /SysId/OrgId/BatchId
	 * 
	 */
	public Object ensureFolder(final String folderPath, final boolean createFolder){
		return (Node) jcrTemplate.execute(new JcrCallback() {

			public Object doInJcr(Session session) throws IOException, RepositoryException {
				return ensureFolder(session, folderPath, createFolder);
			}
		});
	}
	
	/**
	 * FileId(nt:file)/FileContent(nt:resource)
	 * @param parentNode
	 * @param jcrContent
	 */
	public void addContent(final String path, final BaseContent content){
		jcrTemplate.execute(new JcrCallback() {
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				JcrContent jcrContent = (JcrContent) content;
				Node parentNode = ensureFolder(session, path, true);
				String fileId = jcrContent.getFileId();
				Node fileNode = parentNode.addNode(fileId, JcrNodeType.TYPE_FILE);
				fileNode.addMixin("cm:suffixable");
				fileNode.setProperty(JcrContent.PROP_FILE_SUFFIX, jcrContent.getSuffix());
				fileNode.setProperty(JcrContent.PROP_FILE_NAME, jcrContent.getFileName());
				Node contentNode = fileNode.addNode(JcrContent.CONTENT_NODE_NAME, JcrNodeType.TYPE_CONTENT);
				jcrContent2Node(session, jcrContent, contentNode);
				session.save();
				return null;
			}
		});
	}
	
	public JcrContent getContent(final String path){
		return (JcrContent) jcrTemplate.execute(new JcrCallback() {
			
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				Node fileNode = ensureFolder(session, path, false);
				return node2JcrContent(fileNode);
			}
		});
	}
	
	private Node ensureFolder(Session session, String folderPath, boolean createFolder) throws RepositoryException {
		Node root = session.getRootNode();
        if(folderPath == null || folderPath.equals("")){
        	return root;
        }
        Node folder = null;
        try {
        	System.out.println("path: " + folderPath);
            folder = session.getNode(folderPath);
            logger.debug(folderPath + " exists");
        } catch (PathNotFoundException e) {
        	if(createFolder){
	        	logger.debug(folderPath + " not exists, create folder...");
	            folder = createFolder(session, root, folderPath);
        	}else{
        		throw new PathNotFoundException(e);
        	}
        }
        return folder;
	}
	
	private synchronized Node createFolder(Session session, Node root, String folderPath) throws RepositoryException{
        String[] splits = folderPath.substring(1).split("/");
        Node temp = root;
        for(String split : splits){
            try{
                temp = temp.getNode(split);
            }catch (PathNotFoundException e) {
                temp = temp.addNode(split, JcrNodeType.TYPE_FOLDER);
            }
        }
        session.save();
        return temp;
    }

	private void jcrContent2Node(Session session, JcrContent jcrContent, Node node) throws RepositoryException {

		node.setProperty(JcrContent.PROP_LAST_MODIFIED, jcrContent.getLastModified());
		node.setProperty(JcrContent.PROP_MIMETYPE, jcrContent.getMimeType());

		byte[] bytes = jcrContent.getData();
		InputStream is = new ByteArrayInputStream(bytes);
		ValueFactory valueFactory = session.getValueFactory();
		Binary binary = valueFactory.createBinary(is);
		node.setProperty(JcrContent.PROP_DATA, binary);
	}
	
	protected JcrContent node2JcrContent(Node fileNode) throws RepositoryException, IOException {
		Node contentNode = fileNode.getNode(JcrContent.CONTENT_NODE_NAME);
		PropertyIterator pi = fileNode.getProperties();
		while (pi.hasNext()) {
			Property prop = (Property) pi.next();
			if(prop.getName().equals("jcr:mixinTypes")){
				Value[] values = prop.getValues();
				for (Value value : values) {
					System.out.println(prop.getName() + ": " + value.getString());
				}
			}else{
			System.out.println(prop.getName() + ": " + prop.getString());
			}
		}

		JcrContent jcrContent = new JcrContent();
		jcrContent.setFileId(fileNode.getName());
		jcrContent.setFileName(fileNode.getProperty(JcrContent.PROP_FILE_NAME).getString());
//		jcrContent.setSuffix(fileNode.getProperty(JcrContent.PROP_FILE_SUFFIX).getString());
		Binary binary = contentNode.getProperty(JcrContent.PROP_DATA).getBinary();
		InputStream is = binary.getStream();
		byte[] bytes = FileUtil.input2byte(is);
		jcrContent.setData(bytes);
		jcrContent.setLastModified(contentNode.getProperty(JcrContent.PROP_LAST_MODIFIED).getDate());
		jcrContent.setMimeType(contentNode.getProperty(JcrContent.PROP_MIMETYPE).getString());
        return jcrContent;
    }

	public boolean isNodeExist(final String path) {
		return (Boolean) jcrTemplate.execute(new JcrCallback() {
			
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				return session.nodeExists(path);
			}
		});
	}

	@Override
	public void addContent(BatchInfo batchInfo) {
		String batchPath = buildPath(batchInfo);
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		for (FileInfo fileInfo : fileInfos) {
			JcrContent jcrContent = BeanUtil.fileInfo2JcrContent(fileInfo);
			addJcrContent(batchPath, jcrContent);
		}
	}
	
	/**
	 * JCR操作，将文件持久化到JCR中
	 * @param path
	 * @param jcrContent
	 */
	private void addJcrContent(final String path, final JcrContent jcrContent){
		jcrTemplate.execute(new JcrCallback() {
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				Node parentNode = ensureFolder(session, path, true);
				String fileId = jcrContent.getFileId();
				Node fileNode = parentNode.addNode(fileId, JcrNodeType.TYPE_FILE);
				fileNode.addMixin("cm:suffixable");
				fileNode.setProperty(JcrContent.PROP_FILE_SUFFIX, jcrContent.getSuffix());
				fileNode.setProperty(JcrContent.PROP_FILE_NAME, jcrContent.getFileName());
				Node contentNode = fileNode.addNode(JcrContent.CONTENT_NODE_NAME, JcrNodeType.TYPE_CONTENT);
				jcrContent2Node(session, jcrContent, contentNode);
				session.save();
				return null;
			}
		});
	}

	@Override
	protected List<? extends BaseContent> batchInfo2Content(BatchInfo batchInfo) {
		List<JcrContent> contents = new ArrayList<JcrContent>();
		List<FileInfo> fileInfos = batchInfo.getFileInfos();
		for (FileInfo fileInfo : fileInfos) {
			// bean转换成jcr对象
			contents.add(BeanUtil.fileInfo2JcrContent(fileInfo));
		}
		return contents;
	}

	@Override
	protected String buildPath(BatchInfo batchInfo) {
		StringBuffer sb = new StringBuffer(SEPARATOR);
		sb.append(batchInfo.getSysId());
		sb.append(SEPARATOR);
		sb.append(batchInfo.getOrgId());
		sb.append(SEPARATOR);
		sb.append(batchInfo.getBatchId());
		return sb.toString();
	}

	@Override
	public Object getContent(BatchInfo batchInfo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyContentFile(BatchInfo batchInfo, FileInfo fileInfo)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updContent(BatchInfo batchInfo, List<FileInfo> files) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delContent(BatchInfo batchInfo, List<FileInfo> delFiles) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
