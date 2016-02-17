package com.example.ex_seat;

import java.io.InputStream;
import java.util.List;

public interface XML_Parser {
	 /** 
     * ���������� �õ�XML_Node���󼯺� 
     * @param is 
     * @return 
     * @throws Exception 
     */  
    public List<XML_Node> parse(InputStream is) throws Exception;  
      
    /** 
     * ���л�XML_Node���󼯺� �õ�XML��ʽ���ַ��� 
     * @param nodes 
     * @return 
     * @throws Exception 
     */  
    public String serialize(List<XML_Node> nodes) throws Exception; 
}
