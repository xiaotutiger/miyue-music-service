package com.miyue.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * HTTP工具箱
 * 
 * @author leizhimin 2009-6-19 16:36:18
 */
public final class HttpTookit {
	private static Log log = LogFactory.getLog(HttpTookit.class);

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doGet(String url, String queryString, String charset,
			boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			if (StringUtils.isNotBlank(queryString))
				// 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(
								System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (URIException e) {
			log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
		} catch (IOException e) {
			log.error("执行HTTP Get请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doPost(String url, Map<String, String> params,
			String charset, boolean pretty, ByteArrayInputStream arrayInputStream,int length) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
//		post.addRequestHeader("Host", headerValue)
		post.addRequestHeader("Content-Length",String.valueOf(length));
		post.addRequestHeader("User-Agent", "");
		post.addRequestHeader("Accept", "*/*");
		post.addRequestHeader("Accept-Language", "zh-cn");
		post.addRequestHeader("Connection", "Keep-Alive");
		post.addRequestHeader("Proxy-Connection", "Keep-Alive");
		post.addRequestHeader("Pragma", "no-cache");
		post.addRequestHeader("Content-Type", "application/octet-stream");
		post.addRequestHeader("Sky-Content-Version", "1");
		post.setRequestBody(arrayInputStream);
//		String userName = "";
//		String pwd = "";
//		for (Map.Entry<String, String> entry : params.entrySet()) {
//			if("userName".equals(entry.getKey())){
//				userName = entry.getValue();
//			}
//			if("pwd".equals(entry.getKey())){
//				pwd = entry.getValue();
//			}
//		}
		// 设置Http Post数据
//		if (params != null) {
//			HttpMethodParams p = new HttpMethodParams();
//			for (Map.Entry<String, String> entry : params.entrySet()) {
//				p.setParameter(entry.getKey(), entry.getValue());
//			}
//			post.setParams(p);
//		}
		
		try {
			client.executeMethod(post);
			if (post.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(post.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(
								System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
		} finally {
			post.releaseConnection();
		}
		return response.toString();
	}

	public static void main(String[] args) {
		String y = doGet("http://video.sina.com.cn/life/tips.html", null,
				"GBK", true);
		System.out.println(y);
		
		for (int i = 0; i < 10; i++) {
			new Thread(new A()).run();
		}
	}
}
class A implements Runnable{
	@Override
	public void run() {
		byte tmpByte[]=new byte[1024];
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(tmpByte);
		for(int i=0;i<100;i++){
			String json = HttpTookit.doPost("http://172.16.21.38:8001/advertisement",null, "utf-8", false,arrayInputStream, 0);
			
			System.out.println(Thread.currentThread().getId() + "get from server http'post is:"+ json);
		}
	}
}