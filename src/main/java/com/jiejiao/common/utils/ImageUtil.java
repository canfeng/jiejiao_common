package com.jiejiao.common.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;


public class ImageUtil {
	
	/**
	 * 图片缩放
	 * @author shizhiguo
	 * @date 2016年11月11日 下午3:24:55
	 * @param imagepath
	 * @param newpath
	 * @return
	 */
	public static BufferedImage scale(InputStream is,Double size) {
		// 返回一个 BufferedImage，作为使用从当前已注册 ImageReader 中自动选择的 ImageReader 解码所提供
		// File 的结果

		BufferedImage image = null;
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			System.out.println("读取图片文件出错！" + e.getMessage());
			return null;
		}

		// Image Itemp = image.getScaledInstance(300, 300, image.SCALE_SMOOTH);
		double Ratio = 1.0;

		if ((image.getHeight() > size) || (image.getWidth() > size)) {
			if (image.getHeight() > image.getWidth())
				// 图片要缩放的比例
				Ratio = size / image.getHeight();
			else
				Ratio = size / image.getWidth();
		}
		// 根据仿射转换和插值类型构造一个 AffineTransformOp。
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), null);
		// 转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
		image = op.filter(image, null);
		// image.getScaledInstance(300,300,image.SCALE_SMOOTH);

		return image;
	}
	
	
	/**
	 * 将BufferedImage转换成InputStream
	 * @author shizhiguo
	 * @date 2016年10月18日 下午7:03:36
	 * @param image
	 * @return
	 */
	public static InputStream getImageStream(BufferedImage image){ 
        InputStream is = null; 
         
        ByteArrayOutputStream bs = new ByteArrayOutputStream();  
         
        ImageOutputStream imOut; 
        try { 
            imOut = ImageIO.createImageOutputStream(bs); 
             
            ImageIO.write(image,"png",imOut); 
             
            is= new ByteArrayInputStream(bs.toByteArray()); 
             
        } catch (IOException e) { 
            e.printStackTrace(); 
        }  
        return is; 
    }

	
	/**
	 * 图片缩放
	 * @author shizhiguo
	 * @date 2016年11月11日 下午3:24:55
	 * @param imagepath
	 * @param newpath
	 * @return
	 */
	public static boolean scale(String imagepath, String newpath) {
		// 返回一个 BufferedImage，作为使用从当前已注册 ImageReader 中自动选择的 ImageReader 解码所提供
		// File 的结果

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imagepath));
		} catch (IOException e) {
			System.out.println("读取图片文件出错！" + e.getMessage());
			return false;
		}

		// Image Itemp = image.getScaledInstance(300, 300, image.SCALE_SMOOTH);
		double Ratio = 0.0;

		if ((image.getHeight() > 300) || (image.getWidth() > 300)) {
			if (image.getHeight() > image.getWidth())
				// 图片要缩放的比例
				Ratio = 300.0 / image.getHeight();
			else
				Ratio = 300.0 / image.getWidth();
		}
		// 根据仿射转换和插值类型构造一个 AffineTransformOp。
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(Ratio, Ratio), null);
		// 转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
		image = op.filter(image, null);
		// image.getScaledInstance(300,300,image.SCALE_SMOOTH);

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(newpath);
			ImageIO.write((BufferedImage) image, "bmp", out);
			out.close();
		} catch (Exception e) {
			System.out.println("写图片文件出错!!" + e.getMessage());
			return false;
		}
		return true;
	}
}
