package com.example.ex_seat;

import java.io.InputStream;
import java.util.List;

public interface XML_Parser {
	 /** 
     * 解析输入流 得到XML_Node对象集合 
     * @param is 
     * @return 
     * @throws Exception 
     */  
    public List<XML_Node> parse(InputStream is) throws Exception;  
      
    /** 
     * 序列化XML_Node对象集合 得到XML形式的字符串 
     * @param nodes 
     * @return 
     * @throws Exception 
     */  
    public String serialize(List<XML_Node> nodes) throws Exception; 
}
