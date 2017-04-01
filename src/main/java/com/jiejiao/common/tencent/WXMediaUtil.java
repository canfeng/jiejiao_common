package com.jiejiao.common.tencent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.jiejiao.common.utils.http.RequestUtil;
import com.jiejiao.common.utils.log.Log4jKit;

/**
 * 微信多媒体的工具类
 * 
 * @author wan
 */
public class WXMediaUtil {
	/**
	 * 上传媒体文件到微信服务器需要请求的地址
	 */
	public static String UPLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	/**
	 * 获取临时素材的请求地址
	 */
	public static String GET_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

	/**
	 * 上传多媒体数据到微信服务器
	 * 
	 * @param mediaUrl
	 *            来自网络上面的媒体文件地址
	 * @param type
	 * 			       媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @return
	 */
	public static String uploadMedia(String type, String mediaFileUrl) {
		return uploadMedia(WXUtil.getBaseAccessToken(),type, mediaFileUrl);
	}
	
	/**
	 * 上传多媒体数据到微信服务器
	 * 
	 * @param accessToken
	 *            从微信获取到的access_token
	 * @param mediaUrl
	 *            来自网络上面的媒体文件地址
	 * @return
	 */
	public static String uploadMedia(String accessToken, String type, String mediaFileUrl) {
		StringBuffer resultStr = null;
		// 拼装url地址
		String mediaStr = UPLOAD_MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		System.out.println("mediaStr:" + mediaStr);
		URL mediaUrl;
		try {
			String boundary = "----WebKitFormBoundaryOYXo8heIv9pgpGjT";
			URL url = new URL(mediaStr);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			// 让输入输出流开启
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			// 使用post方式请求的时候必须关闭缓存
			urlConn.setUseCaches(false);
			// 设置请求头的Content-Type属性
			urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			urlConn.setRequestMethod("POST");
			// 获取输出流，使用输出流拼接请求体
			OutputStream out = urlConn.getOutputStream();

			// 读取文件的数据,构建一个GET请求，然后读取指定地址中的数据
			mediaUrl = new URL(mediaFileUrl);
			HttpURLConnection mediaConn = (HttpURLConnection) mediaUrl.openConnection();
			// 设置请求方式
			mediaConn.setRequestMethod("GET");
			// 设置可以打开输入流
			mediaConn.setDoInput(true);
			// 获取传输的数据类型
			String contentType = mediaConn.getHeaderField("Content-Type");
			// 将获取大到的类型转换成扩展名
			String fileExt = judgeType(contentType);
			// 获取输入流，从mediaURL里面读取数据
			InputStream in = mediaConn.getInputStream();
			BufferedInputStream bufferedIn = new BufferedInputStream(in);
			// 数据读取到这个数组里面
			byte[] bytes = new byte[1024];
			int size = 0;
			// 使用outputStream流输出信息到请求体当中去
			out.write(("--" + boundary + "\r\n").getBytes());
			out.write(("Content-Disposition: form-data; name=\"media\";\r\n" + "filename=\"" + (new Date().getTime())
					+ fileExt + "\"\r\n" + "Content-Type: " + contentType + "\r\n\r\n").getBytes());
			while ((size = bufferedIn.read(bytes)) != -1) {
				out.write(bytes, 0, size);
			}
			// 切记，这里的换行符不能少，否则将会报41005错误
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());

			bufferedIn.close();
			in.close();
			mediaConn.disconnect();

			InputStream resultIn = urlConn.getInputStream();
			InputStreamReader reader = new InputStreamReader(resultIn);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String tempStr = null;
			resultStr = new StringBuffer();
			while ((tempStr = bufferedReader.readLine()) != null) {
				resultStr.append(tempStr);
			}
			bufferedReader.close();
			reader.close();
			resultIn.close();
			urlConn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultStr.toString();
	}

	/**
	 * 获取临时素材
	 * 
	 * @author shizhiguo
	 * @date 2017年4月1日 下午2:05:38
	 * @param accessToken
	 * @param media_id
	 * @return
	 */
	public static String getMedia(String accessToken,String media_id) {
		String resultStr = null;
		// 拼装url地址
		String mediaStr = GET_MEDIA_URL.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", media_id);
		System.out.println("mediaStr:" + mediaStr);
		try {
			resultStr=RequestUtil.getUrl(mediaStr);
			
		} catch (Exception e) {
			// 打印异常行数
			Log4jKit.error(e + "\tException Line==>" + e.getStackTrace()[0].getLineNumber());
		}
		return resultStr;
	}

	/**
	 * 通过传过来的contentType判断是哪一种类型
	 * 
	 * @param contentType
	 *            获取来自连接的contentType
	 * @return
	 */
	public static String judgeType(String contentType) {
		String fileExt = "";
		if ("image/jpeg".equals(contentType)) {
			fileExt = ".jpg";
		} else if ("audio/mpeg".equals(contentType)) {
			fileExt = ".mp3";
		} else if ("audio/amr".equals(contentType)) {
			fileExt = ".amr";
		} else if ("video/mp4".equals(contentType)) {
			fileExt = ".mp4";
		} else if ("video/mpeg4".equals(contentType)) {
			fileExt = ".mp4";
		}
		return fileExt;
	}

}