package com.jiejiao.common.utils.security;

import java.io.*;

/*
 * 十六进制处理
 */
public class HexUtil {
	/*
	 * 二进制字节数组转换为十六进制字节数组
	 * 
	 * @param byte[] datas，数据
	 * 
	 * @return byte[]，转换的结果
	 */
	public static byte[] toHex(byte[] datas) {
		Character[] hexChars = new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
				'E', 'F' };
		ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
		for (int i = 0; i < datas.length; i++) {
			byte data = datas[i];
			byte[] hexByte = new byte[2];
			hexByte[0] = (byte) ((int) hexChars[(data & 0xF0) >> 4]);
			hexByte[1] = (byte) ((int) hexChars[data & 0x0F]);
			memoryStream.write(hexByte, 0, 2);
		}
		return memoryStream.toByteArray();
	}

	/*
	 * 同上
	 * 
	 * @param String input，待转换的字符串
	 * 
	 * @param String encoding，编码方式
	 * 
	 * @return String，转换的结果
	 */
	public static String toHex(String input, String encoding) {
		try {
			byte[] data = input.getBytes(encoding);
			byte[] hexData = toHex(data);
			return new String(hexData, encoding);
		} catch (Exception e) {
		}
		return "";
	}

	/*
	 * 转换为十六进制
	 * 
	 * @param String input，待转换的字符串
	 * 
	 * @param String encoding，编码方式
	 * 
	 * @return String，转换的结果
	 */
	public static String toHex(String input) {
		return toHex(input, "utf8");
	}

	/*
	 * 由十六进制数据转换为二进制字节数组
	 * 
	 * @param byte[] hexData，十六进制数据
	 * 
	 * @param byte[]，原形式
	 */
	public static byte[] fromHex(byte[] hexData) {
		ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
		for (int i = 0; i < hexData.length; i += 2) {
			byte[] hexPairInDecimal = new byte[2];
			for (int h = 0; h < 2; h++) {
				Character temp = Character.toChars((int) hexData[i + h])[0];
				if (temp == '0') {
					hexPairInDecimal[h] = 0;
				} else if (temp == '1') {
					hexPairInDecimal[h] = 1;
				} else if (temp == '2') {
					hexPairInDecimal[h] = 2;
				} else if (temp == '3') {
					hexPairInDecimal[h] = 3;
				} else if (temp == '4') {
					hexPairInDecimal[h] = 4;
				} else if (temp == '5') {
					hexPairInDecimal[h] = 5;
				} else if (temp == '6') {
					hexPairInDecimal[h] = 6;
				} else if (temp == '7') {
					hexPairInDecimal[h] = 7;
				} else if (temp == '8') {
					hexPairInDecimal[h] = 8;
				} else if (temp == '9') {
					hexPairInDecimal[h] = 9;
				} else if (temp == 'A' || temp == 'a') {
					hexPairInDecimal[h] = 10;
				} else if (temp == 'B' || temp == 'b') {
					hexPairInDecimal[h] = 11;
				} else if (temp == 'C' || temp == 'c') {
					hexPairInDecimal[h] = 12;
				} else if (temp == 'D' || temp == 'd') {
					hexPairInDecimal[h] = 13;
				} else if (temp == 'E' || temp == 'e') {
					hexPairInDecimal[h] = 14;
				} else if (temp == 'F' || temp == 'f') {
					hexPairInDecimal[h] = 15;
				}
			}
			memoryStream.write((byte) ((hexPairInDecimal[0] << 4) | hexPairInDecimal[1]));
		}
		return memoryStream.toByteArray();
	}

	/*
	 * 转换回原形式
	 * 
	 * @param String input，待转换的字符串
	 * 
	 * @param String encoding，编码方式
	 * 
	 * @return String，原形式
	 */
	public static String fromHex(String input, String encoding) {
		try {
			byte[] hexData = input.getBytes(encoding);
			byte[] data = fromHex(hexData);
			return new String(data, encoding);
		} catch (Exception e) {
		}
		return "";
	}

	/*
	 * 转换回原形式
	 * 
	 * @param String input，待转换的字符串
	 * 
	 * @param String encoding，编码方式
	 * 
	 * @return String，原形式
	 */
	public static String fromHex(String input) {
		return fromHex(input, "utf8");
	}

	
	private static String hexStr = "0123456789ABCDEF";
	/**
    *
    * @param bytes
    * @return 将二进制转换为十六进制字符输出
    */
    public static String BinaryToHexString(byte[] bytes){

	    String result = "";
	
	    String hex = "";
	
	    for(int i=0;i<bytes.length;i++){
	    	
	    	//字节高4位
	    	hex=String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
		
		    //字节低4位
		
		    hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
		
		    result +=hex;
	
	    }

	    return result;

    }
    
    /**
    *
    * @param hexString
    * @return 将十六进制转换为字节数组
    */
    public static byte[] HexStringToBinary(String hexString){

	    //hexString的长度对2取整，作为bytes的长度
	
	    int len = hexString.length()/2;
	
	    byte[] bytes = new byte[len];
	
	    byte high = 0;//字节高四位
	
	    byte low = 0;//字节低四位
	
	    for(int i=0;i<len;i++){
	
		    //右移四位得到高位
		
		    high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
		
		    low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
		
		    bytes[i] = (byte) (high|low);//高地位做或运算
	
	    }
	
	    return bytes;

    }
    
    public static void main(String[] args) {
		String hStr = BinaryToHexString("indata".getBytes());
		System.out.println(hStr);
		
		byte[] bin = HexStringToBinary(hStr);
		
		System.out.println(new String(bin));
	}


}