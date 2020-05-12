/**
 * Copyright by LuNanTing
 * Date:2018年12月29日上午9:20:56
 * @author : LuNanTing
 */
package com.hikvision.hikvisionmanage.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

/**
 * 发送httpClient请求
 * @author :LuNanTing
 * @since :JDK8
 * @date :2020年5月11日18:26:40
 */
public class HttpClientUtil {

	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * @提交post请求
	 * @方法名 submitPostRequest
	 * @param url
	 * @param params
	 * @return
	 * @返回类型 String
	 * @创建人 kangkai
	 * @修改人 LuNanTing,Invoker
	 * @修改时间 2018年10月13日09:58:16
	 */
	public static String submitPostRequest(String url, List<NameValuePair> params) {
		HttpPost httpPost = new HttpPost(url);
		try {
			if (params != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			}
			httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(200000).setConnectTimeout(200000).build();
			httpPost.setConfig(requestConfig);
			CloseableHttpResponse response = null;
			response = httpClient.execute(httpPost);
			LoggerUtil.info("response:" + response);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, "utf-8");
			return body;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		return "";
	}

	/**
	 * @提交map参数
	 * @方法名 submitPostRequestMap
	 * @param url
	 * @param parms
	 * @return
	 * @返回类型 String
	 * @创建人 kangkai
	 * @修改人
	 * @修改时间 2020年5月11日18:30:09
	 */
	public synchronized static String submitPostRequestMap(String url, Map<String, Object> parms) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (parms != null) {
			for (String key : parms.keySet()) {
				params.add(new BasicNameValuePair(key, parms.get(key) + ""));
			}
		}
		return submitPostRequest(url, params);
	}
}
