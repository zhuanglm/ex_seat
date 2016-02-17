package com.example.ex_seat;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XML_SampleParser implements XML_Parser {

	@Override
	public List<XML_Node> parse(InputStream is) throws Exception {
		List<XML_Node> items = null;  
		XML_Node item = null;  
	         
		XmlPullParser parser = Xml.newPullParser(); //��android.util.Xml����һ��XmlPullParserʵ��  
		parser.setInput(is, "UTF-8");               //���������� ��ָ�����뷽ʽ  

		int eventType = parser.getEventType();  
        while (eventType != XmlPullParser.END_DOCUMENT) {  
            switch (eventType) {  
            case XmlPullParser.START_DOCUMENT:  
            	items = new ArrayList<XML_Node>();  
                break;  
            case XmlPullParser.START_TAG:  
                if (parser.getName().equals("Published")) {  
                	item = new XML_Node();  
                } else if (parser.getName().equals("Total")) {  
                    eventType = parser.next();  
                    item.Total = Integer.parseInt(parser.getText());  
                } 
                break;  
            case XmlPullParser.END_TAG:  
                if (parser.getName().equals("Published")) {  
                	items.add(item);  
                	item = null;      
                }  
                break;  
            }  
            eventType = parser.next();  
        }  
        return items;  
	}

	@Override
	public String serialize(List<XML_Node> nodes) throws Exception {
		XmlSerializer serializer = Xml.newSerializer(); //��android.util.Xml����һ��XmlSerializerʵ��  
        StringWriter writer = new StringWriter();  
        serializer.setOutput(writer);   //�����������Ϊwriter  
        serializer.startDocument("UTF-8", true);  
        serializer.startTag("", "Published");  
        for (XML_Node item : nodes) {  
            serializer.startTag("", "Total");  
            serializer.text(Integer.toString(item.Total));  
            serializer.endTag("", "Total");  
        }  
        serializer.endTag("", "Published");  
        serializer.endDocument();  
          
        return writer.toString(); 
	}

}
