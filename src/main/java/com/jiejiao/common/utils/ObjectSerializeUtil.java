package com.jiejiao.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * java 对象 序列化 工具类
 * @author SZG
 *
 */
public class ObjectSerializeUtil {

	
	/**
	 * 将对象转换为字节数组
	 * @param obj
	 * @return
	 */
	public static byte[] objectToByteArray(Object obj){
		ObjectOutputStream oos=null;
		ByteArrayOutputStream baos=null;
		try {
			baos=new ByteArrayOutputStream();
			oos=new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	/**
	 * 字节数组转换为对象
	 * @param bytes
	 * @return
	 */
	public static Object byteArrayToObject(byte[] bytes){
		ObjectInputStream ois=null;
		ByteArrayInputStream bais=null;
		try {
			bais=new ByteArrayInputStream(bytes);
			ois=new ObjectInputStream(bais);
			Object obj = ois.readObject();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return  null;
	}
}

