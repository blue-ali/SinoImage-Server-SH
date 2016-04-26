package jcr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import base.BaseSpringTest;
import cn.net.sinodata.cm.content.IContentService;
import cn.net.sinodata.cm.content.jcr.model.JcrContent;
import cn.net.sinodata.framework.util.FileUtil;
import sun.net.www.MimeTable;

public class JcrServiceTest extends BaseSpringTest{

	
	@Resource
	private IContentService jcrService;
	
	@Test
	public void test(){
		
	}
	@Before
	public void before(){
//		jcrService.regist();
	}
	
	/*@Test
	public void testEnsureFolder() throws RepositoryException{
		jcrService.ensureFolder("/111/o999/6530b6e9-6035-4a6b-90e7-3fb956efd91a", false);
	}
	
	@Test
	public void testAddContent(){
		
		File file = new File("F:\\tmp\\image\\222.jpg");
		JcrContent jcrContent = new JcrContent();
		try {
			jcrContent.setData(FileUtil.file2byte(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jcrContent.setFileId("0007");
		MimeTable mt = MimeTable.getDefaultTable();
		String mimeType = mt.getContentTypeFor(file.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		jcrContent.setFileName(file.getName());
		jcrContent.setMimeType(mimeType);
		jcrContent.setLastModified(Calendar.getInstance());
		jcrContent.setSuffix("jpg");
		Node parentNode = jcrService.ensureFolder("/test/N1", true);
		jcrService.addContent("/test/N1", jcrContent);
	}
	
	@Test
	public void testNodeExist(){
		boolean flag = jcrService.isNodeExist("/999/o111/");
		Assert.assertTrue(flag);
	}
	
	@Test
	public void testGetContent(){
		JcrContent jcrContent = jcrService.getContent("/test/N1/0007");
		byte[] bytes = jcrContent.getData();
		try {
			FileUtil.byte2file(bytes, "F:\\tmp\\download\\", jcrContent.getFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(jcrContent);
	}*/
	
}

