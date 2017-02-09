package com.jiejiao.common.tencent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * User: rizenguo
 * Date: 2014/11/1
 * Time: 14:06
 */
public class XMLParser {


    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  Util.getStringStream(xmlString);
        Document document = builder.parse(is);

        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;

    }
    
    
    /**
	 * 将data类型的对象转换为xml字符串
	 * @param map
	 * @return
	 */
	public static String getXMLFromMap(Map<String,Object> map) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>");  
        mapToXML(map, sb);  
        sb.append("</xml>");  
        try {  
            return sb.toString();//.getBytes();  
        } catch (Exception e) {  
        	System.out.println(e);  
        }  
        return null;  
    }  
	
	 private static void mapToXML(Map map, StringBuffer sb) {  
	        Set set = map.keySet();  
	        for (Iterator it = set.iterator(); it.hasNext();) {  
	            String key = (String) it.next();  
	            Object value = map.get(key);  
	            if (null == value)  
	                value = "";  
	            if (value.getClass().getName().equals("java.util.ArrayList")) {  
	                ArrayList list = (ArrayList) map.get(key);  
	                sb.append("<" + key + ">");  
	                for (int i = 0; i < list.size(); i++) {  
	                    HashMap hm = (HashMap) list.get(i);  
	                    mapToXML(hm, sb);  
	                }  
	                sb.append("</" + key + ">");  
	  
	            } else {  
	                if (value instanceof HashMap) {  
	                    sb.append("<" + key + ">");  
	                    mapToXML((HashMap) value, sb);  
	                    sb.append("</" + key + ">");  
	                } else {  
	                    sb.append("<" + key + ">" + value + "</" + key + ">");  
	                }  
	  
	            }  
	  
	        }  
	    } 

	 
}
