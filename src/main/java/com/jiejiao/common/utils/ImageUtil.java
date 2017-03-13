package com.jiejiao.common.utils;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
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
	 * @param ext 扩展名
	 * @return
	 */
	public static InputStream getImageStream(BufferedImage image,String ext){ 
        InputStream is = null; 
         
        ByteArrayOutputStream bs = new ByteArrayOutputStream();  
         
        ImageOutputStream imOut; 
        try { 
            imOut = ImageIO.createImageOutputStream(bs); 
             
            ImageIO.write(image,ext,imOut); 
             
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
	
	
	/* 
     * 根据尺寸图片居中裁剪 
     */  
     public static BufferedImage cutImage(InputStream inputStream,int x,int y,int w,int h,String ext) throws IOException{   
        ImageInputStream iis = null;  
        try {  
            /** 
             *  
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 
             *  
             * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 . 
             *  
             * (例如 "jpeg" 或 "tiff")等 。 
             */  
            Iterator<ImageReader> it = ImageIO  
                    .getImageReadersByFormatName(ext);  
  
            ImageReader reader = it.next();  
  
            // 获取图片流  
            iis = ImageIO.createImageInputStream(inputStream);  
  
            /** 
             *  
             * <p> 
             * iis:读取源。true:只向前搜索 
             * </p> 
             * .将它标记为 ‘只向前搜索’。 
             *  
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 
             *  
             * 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。 
             */  
            reader.setInput(iis, true);  
  
            /** 
             *  
             * <p> 
             * 描述如何对流进行解码的类 
             * <p> 
             * .用于指定如何在输入时从 Java Image I/O 
             *  
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 
             *  
             * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回 
             *  
             * ImageReadParam 的实例。 
             */  
            ImageReadParam param = reader.getDefaultReadParam();  
  
            /** 
             *  
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象 
             *  
             * 的左上顶点的坐标(x，y)、宽度和高度可以定义这个区域。 
             */  
            Rectangle rect = new Rectangle(x, y, w, h);  
  
            // 提供一个 BufferedImage，将其用作解码像素数据的目标。  
            param.setSourceRegion(rect);  
  
            /** 
             *  
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 
             *  
             * 它作为一个完整的 BufferedImage 返回。 
             */  
            BufferedImage bi = reader.read(0, param);  
            return bi;
        } finally {  
            if (inputStream != null)  
            	inputStream.close();  
            if (iis != null)  
                iis.close();  
        }  
     }  


     
}
