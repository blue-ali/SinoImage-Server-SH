package cn.net.sinodata.cm.httpurl;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.net.sinodata.cm.common.GlobalVars;

/**
 * @author dongzj
 *
 */
public class OtherSysUrl 
{	
	public static boolean sendJunkangUrl(String batchNo,int imgAmount) throws IOException
	{
		String otherSysUrl = GlobalVars.otherSysUrl;
		String url = otherSysUrl + "?batchNo" + batchNo + "&imgAmount=" + imgAmount;
		boolean yesOrNo = httpGet(url);
		return yesOrNo;
	}
	
	private static  boolean httpGet(String url) throws IOException{
		boolean auswer = false;
		HttpGet request = new HttpGet(url);
		HttpResponse response = HttpClients.createDefault().execute(request);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			String urlres = EntityUtils.toString(response.getEntity());		
			auswer = true;
			url = URLDecoder.decode(url, "UTF-8");
		} else {
			auswer = false;
		}
		return auswer;
	}
}
