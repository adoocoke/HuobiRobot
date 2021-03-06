package org.warmsheep.huobi;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.warmsheep.constants.PropertiesConstants;
import org.warmsheep.util.EncryptUtil;
import org.warmsheep.util.HttpUtil;
import org.warmsheep.util.properties.ConfigReader;

@Service("baseService")
public class BaseService {

	private Logger logger = LoggerFactory.getLogger(BaseService.class);

	protected static int success = 200;
	
	public String post(Map<String, Object> map,String url) throws Exception {
		return HttpUtil.post(url, map, new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int code = response.getStatusLine().getStatusCode();
				if(success == code) {
					return EntityUtils.toString(response.getEntity(), "utf-8");
				}
				logger.info("response code {}", code);
				return null;
			}
			
		});
	}
	
	public long getTimestamp() {
		return System.currentTimeMillis()/1000;
	}
	
	public String sign(TreeMap<String, Object> map) {
		StringBuilder inputStr = new StringBuilder();
		for (Map.Entry<String, Object> me : map.entrySet()) {
            inputStr.append(me.getKey()).append("=").append(me.getValue()).append("&");
        }
        String md5Str = EncryptUtil.MD5(inputStr.substring(0, inputStr.length() - 1)).toLowerCase();
        return md5Str;
	}
}
