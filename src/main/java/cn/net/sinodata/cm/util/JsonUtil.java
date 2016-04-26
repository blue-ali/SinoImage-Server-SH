/**
 * 
 */
package cn.net.sinodata.cm.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import cn.net.sinodata.cm.hibernate.po.BatchInfo;
import cn.net.sinodata.cm.hibernate.po.FileInfo;

/**
 * @author manan
 *
 */
public class JsonUtil {

	public static BatchInfo jsonStr2BatchInfo(String json){
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("fileInfos", FileInfo.class);
		return (BatchInfo) JSONObject.toBean(JSONObject.fromObject(json), BatchInfo.class, classMap);
	}
	
}