package com.jiejiao.common.utils.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Hex;

public class RSAUtil {

	public static void main(String[] args) {
		jdkRSA("this my rsa encode content");
	}
	
	/**
	 * jdk rsa加解密
	 * @author shizhiguo
	 * @date 2017年2月13日 上午9:57:37
	 */
	public static void jdkRSA(String src){
		
		try {
			//获取密钥
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			
			//执行签名
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
			KeyFactory keyFactory=KeyFactory.getInstance("RSA");
			PrivateKey privateKey=keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Signature signature=Signature.getInstance("MD5withRSA");
			signature.initSign(privateKey);
			byte[] result = signature.sign();
			System.out.println("jdk rsa sign:"+ new String(Hex.encodeHex(result)));
			
			//签名验证
			X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			keyFactory=KeyFactory.getInstance("RSA");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
