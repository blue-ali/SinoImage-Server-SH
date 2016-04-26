package cn.net.sinodata.cm.init;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import cn.net.sinodata.cm.common.Constant;
import cn.net.sinodata.cm.common.GlobalVars;
import cn.net.sinodata.cm.content.IContentService;
import cn.net.sinodata.cm.util.SpringUtil;
import cn.net.sinodata.framework.log.SinoLogger;

/**
 * 
 * 初始化配置文件服务，在应用程序启动时由InitializeService加载
 * 
 * @author manan
 * 
 */
public class InitAllConfigService implements InitServiceInteface{

	private static final SinoLogger logger = SinoLogger.getLogger(InitAllConfigService.class);
	private static final String SEPERATOR = File.separator; 
	private static final String BASE_CONFIG_PATH = "resources" + SEPERATOR + "config" + SEPERATOR;
	private static final int THREAD_SIZE = 200; // 线程池大小
	
	public boolean execute() {
		try {
			initSinoCM();
//			registJcrNodeType();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void registJcrNodeType(){
		Object obj = SpringUtil.getBean("jdbcManagedConnectionFactory");
		System.out.println(obj);
		IContentService contentService = (IContentService) SpringUtil.getBean("jcrService");
		contentService.regist();
	}
	
	private void initSinoCM() throws Exception{
		Properties prop = new Properties();
		prop.load(this.getClass().getClassLoader().getResourceAsStream(BASE_CONFIG_PATH + "SinoCM.properties"));
		GlobalVars.presist_type = prop.getProperty("presistType");
		GlobalVars.local_root_path = prop.getProperty("local_root_path");
		String osName = System.getProperty("os.name");
		if(osName.startsWith("Windows")){
			GlobalVars.osType = Constant.OS_TYPE_WINDOWS;
		}else if(osName.startsWith("Linux")){
			GlobalVars.osType = Constant.OS_TYPE_LINUX;
		}
	}
}
