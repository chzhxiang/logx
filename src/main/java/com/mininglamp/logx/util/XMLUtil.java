package com.mininglamp.logx.util;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * xml工具
 * @author sjfx012
 *
 */
public abstract class XMLUtil {
	private XMLUtil(){}
	/**
	 * 获取唯一子节点
	 * @param node
	 * @param tagName
	 * @return
	 */
	public static Element getChildByTagName(Node node, String tagName){
		//检查节点正确性
		if(node.getNodeType() != Node.ELEMENT_NODE){
			throw new RuntimeException(tagName + "节点格式不正确，非Element类型");
		}
		Element el = (Element)node;
		//获取此节点的名为tagName的子，并返回第一个
		NodeList nodeList = el.getElementsByTagName(tagName);
		//如果不存在子，则返回Null
		if(nodeList == null || nodeList.getLength() == 0){
			return null;
		}
		return (Element)nodeList.item(0);
	}
	public static NodeList getAllChildByTagName(Node node, String tagName){
		//检查节点正确性
		if(node.getNodeType() != Node.ELEMENT_NODE){
			throw new RuntimeException(tagName + "节点格式不正确，非Element类型");
		}
		Element el = (Element)node;
		//获取此节点的名为tagName的子，并返回第一个
		NodeList nodeList = el.getElementsByTagName(tagName);
		//如果不存在子，则返回Null
		if(nodeList == null || nodeList.getLength() == 0){
			return null;
		}
		return nodeList;
	}
	/**
	 * 将输入流转为xml
	 * @param is
	 * @return
	 */
	public static Document parseToXml(final InputStream is){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.parse(is);
			return document;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("load xmlerror", e);
		}
	}
	/**
	 * 读取某个节点的唯一子节点
	 * @param doc
	 * @param tagName
	 * @return
	 */
	public static Element getChildByTagName(Document doc, String tagName){
		NodeList nodeList = doc.getElementsByTagName(tagName);
		//如果不存在子，则返回Null
		if(nodeList == null || nodeList.getLength() == 0){
			return null;
		}
		return (Element)nodeList.item(0);
	}
	
	/**
	 * 读取某个节点的子节点
	 * @param doc
	 * @param tagName
	 * @return
	 */
	public static NodeList getAllChildByTagName(Document doc, String tagName){
		NodeList nodeList = doc.getElementsByTagName(tagName);
		//如果不存在子，则返回Null
		if(nodeList == null || nodeList.getLength() == 0){
			return null;
		}
		return nodeList;
	}
}
