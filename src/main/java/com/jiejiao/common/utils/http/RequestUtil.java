package com.jiejiao.common.utils.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import com.jiejiao.common.utils.log.Log4jKit;

/*
 * 请求
 */
public class RequestUtil {

	/*
	 * 获取url里面的html
	 */
	public static String getHtml(String url, String encoding, int timeout, HttpActionType httpActionType,
			String postParameter) {
		try {
			URL realUrl = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(timeout);
			httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			PrintWriter printWriter = null;
			if (httpActionType == HttpActionType.GET) {
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.connect();
			} else {
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.addRequestProperty("Content-type", "application/x-www-form-urlencoded");

				if (postParameter != null) {
					printWriter = new PrintWriter(httpURLConnection.getOutputStream());
					printWriter.print(postParameter);
					printWriter.flush();
				}
			}
			BufferedReader bufferedReader = null;
			String type = httpURLConnection.getContentEncoding();
			if (type != null) {
				type = type.toLowerCase();
			}
			if ("gzip".equals(type)) {
				bufferedReader = new BufferedReader(
						new InputStreamReader(new GZIPInputStream(httpURLConnection.getInputStream()), encoding));
			} else if ("deflate".equals(type)) {
				bufferedReader = new BufferedReader(
						new InputStreamReader(new DeflaterInputStream(httpURLConnection.getInputStream()), encoding));
			} else {
				bufferedReader = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), encoding));
			}

			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			try {
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} finally {
				bufferedReader.close();
				httpURLConnection.disconnect();
				if (printWriter != null) {
					printWriter.close();
				}
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/*
	 * 获取url里面的html
	 */
	public static String getHtml(String url, String encoding, int timeout, HttpActionType httpActionType) {
		return getHtml(url, encoding, timeout, httpActionType, null);
	}

	/*
	 * 获取url里面的html
	 */
	public static String getHtml(String url, String encoding, int timeout) {
		return getHtml(url, encoding, timeout, HttpActionType.GET, null);
	}

	/*
	 * 获取url里面的html
	 */
	public static String getHtml(String url, String encoding) {
		return getHtml(url, encoding, 1000, HttpActionType.GET, null);
	}

	public static String postUrl(String url) {
		return getHtml(url, "utf-8", 10000, HttpActionType.POST);
	}

	public static String postUrl(String url, String paramStr) {
		Log4jKit.info("请求url==>" + url);
		Log4jKit.info("发送参数==>" + paramStr);
		String res = getHtml(url, "utf-8", 10000, HttpActionType.POST, paramStr);
		Log4jKit.info("返回结果==>" + res);
		return res;
	}

	public static String getUrl(String url) {
		return getHtml(url, "utf-8", 10000, HttpActionType.GET);
	}

	/**********************************************************/
	public static String httpUrlConnection(String pathUrl) {
		try {
			// 建立连接
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法
			String requestString = "客服端要以以流方式发送到服务端的数据...";

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("UTF-8");
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/octet-stream");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			// String name = URLEncoder.encode("黄武艺", "utf-8");
			// httpConn.setRequestProperty("NAME", name);

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();

			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功
				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				return sb.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	enum HttpActionType {
		GET, POST,
	}
}
