package cn.net.sinodata.cm.content.jcr;

import java.io.InputStreamReader;
import java.io.Reader;

import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeTypeManager;

import org.apache.jackrabbit.commons.cnd.CndImporter;

import cn.net.sinodata.framework.log.SinoLogger;

public class NodeTypeRegister {
	
	public static final SinoLogger logger = SinoLogger.getLogger(NodeTypeRegister.class);

	/**
	 * 通过CND文件注册自定义节点类型
	 * @param session
	 * @param cndFileName
	 * @throws Exception
	 */
	public static void RegisterCustomNodeTypes(Session session, String cndFileName) throws Exception {
		Reader in=new InputStreamReader(NodeTypeRegister.class.getClassLoader().getResourceAsStream(cndFileName),"UTF-8");
		NodeType[] nodeTypes = CndImporter.registerNodeTypes(in,session, true);
		
		NodeTypeManager manager = session.getWorkspace().getNodeTypeManager();

		for (NodeType nt : nodeTypes) {
			logger.debug("Registered: " + nt.getName());
			if(!manager.hasNodeType(nt.getName())){
				throw new Exception("注册节点类型[" + nt.getName() + "]失败");
			}
		}
		
//		NodeTypeIterator itor = manager.getMixinNodeTypes();
//		while (itor.hasNext()) {
//			NodeType type = (NodeType) itor.next();
//			System.out.println(type.getName());
//		}
	}
	
	public static void unregist(Session session, String nodeType) throws Exception{
		NodeTypeManager manager = session.getWorkspace().getNodeTypeManager();
		manager.unregisterNodeType(nodeType);
	}
}
