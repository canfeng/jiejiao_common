package com.jiejiao.common.utils.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.ChannelSftp;
import com.jiejiao.common.utils.log.LogKit;

/**
 * 使用sftp协议上传文件到远程服务器
 * 
 * @author shizhiguo
 * @date 2017年7月28日 下午3:08:19
 */
public class SFTPFileUtil {

	public static SFTPChannel getSFTPChannel() {
		return new SFTPChannel();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		SFTPParam sftpParam = new SFTPParam();
		sftpParam.setFileName("123.jpg");
		sftpParam.setHost("101.201.48.141");
		sftpParam.setPort(22);
		sftpParam.setUsername("root");
		sftpParam.setUserpwd("L&gsKllU&p8Jpzse*sz$");
		sftpParam.setUploadDir("/data/wwwroot/cfps.jiejiaolicai.com/");
		sftpParam.setInputStream(new FileInputStream(new File("d://4ed58f96-3688-46c5-81d6-2b95843ef5c4_square.jpg")));
		sftpUploadFile(sftpParam);
	}

	public static void sftpUploadFile(SFTPParam sftpParam) {
		Map<String, String> sftpDetails = new HashMap<String, String>();
		// 设置主机ip，端口，用户名，密码
		sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, sftpParam.getHost());
		sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, sftpParam.getUsername());
		sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, sftpParam.getUserpwd());
		sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, sftpParam.getPort().toString());
		String dst = sftpParam.getUploadDir() + sftpParam.getFileName(); // 目标文件名
		SFTPChannel channel = new SFTPChannel();
		try {
			ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);
			chSftp.put(sftpParam.getInputStream(), dst, ChannelSftp.OVERWRITE); 
			chSftp.quit();
		} catch (Exception e) {
			LogKit.error("sftp上传异常==>" + e + "\tException Line==>" + e.getStackTrace()[0]);
		} finally {
			try {
				channel.closeChannel();
			} catch (Exception e) {
				LogKit.error("关闭sftp渠道失败==>" + e + "\tException Line==>" + e.getStackTrace()[0]);
			}
		}
	}

}
