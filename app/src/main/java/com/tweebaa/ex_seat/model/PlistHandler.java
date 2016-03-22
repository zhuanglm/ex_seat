package com.tweebaa.ex_seat.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * .plist配置文件的解析器 
 * 支持array 
 * <plist version="1.0"> 
 *   <array> 
 *     <dict> 
 *       ... 
 *     </dict> 
 *     ... 
 *   </array> 
 * </plist version="1.0"> 
 *
 * 支持Map 
 * <plist version="1.0"> 
 * <dict> 
 *   <id>key</id> 
 *   <array> 
 *     <dict> 
 *       ... 
 *     </dict> 
 *     ... 
 *   </array> 
 *   ... 
 * </dict>   
 * </plist version="1.0">
 *
 * PlistHandler handle = new PlistHandler();
 XmlUtil xml = new XmlUtil(handle);
 xml.prase(new FileInputStream(configFile));
 Map result = handle.getMapResult();
 *
 * @author chen_weihua
 *
 */
public class PlistHandler extends DefaultHandler {
    private LinkedList<Object> list = new LinkedList<Object>();

    private boolean isRootElement = false;

    private boolean keyElementBegin = false;

    private String key;

    private StringBuffer value = new StringBuffer();

    private boolean valueElementBegin = false;

    private Object root;

    @SuppressWarnings("unchecked")
    public Map getMapResult() {
        return (Map)root;
    }

    @SuppressWarnings("unchecked")
    public List getArrayResult() {
        return (List)root;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        value = new StringBuffer();
        if ("plist".equals(localName)) {
            isRootElement = true;
        }
        if ("dict".equals(localName)) {
            if (isRootElement) {
                list.addFirst(new HashMap());
                isRootElement = !isRootElement;
            } else {
                ArrayList parent = (ArrayList)list.get(0);
                list.addFirst(new HashMap());
                parent.add(list.get(0));
            }
        }

        if ("key".equals(localName)) {
            keyElementBegin = true;
        }
        if ("true".equals(localName)) {
            HashMap parent = (HashMap)list.get(0);
            parent.put(key, true);
        }
        if ("false".equals(localName)) {
            HashMap parent = (HashMap)list.get(0);
            parent.put(key, false);
        }
        if ("array".equals(localName)) {
            if (isRootElement) {
                ArrayList obj = new ArrayList();
                list.addFirst(obj);
                isRootElement = !isRootElement;
            } else {
                HashMap parent = (HashMap)list.get(0);
                ArrayList obj = new ArrayList();
                list.addFirst(obj);
                parent.put(key, obj);
            }
        }
        if ("string".equals(localName)) {
            valueElementBegin = true;
        }
    }

    //@SuppressWarnings("unchecked")  
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (length > 0) {
            value.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (keyElementBegin) {
            key = value.toString();
            //Log.d("AR native", "key:" + key);  
        }
        if (valueElementBegin) {

            if (HashMap.class.equals(list.get(0).getClass())) {
                HashMap parent = (HashMap)list.get(0);
                parent.put(key, value.toString());
            } else if (ArrayList.class.equals(list.get(0).getClass())) {
                ArrayList parent = (ArrayList)list.get(0);
                parent.add(value.toString());
            }
            //Log.d("AR native", "value:" + value);  
        }




        if ("plist".equals(localName)) {
            ;
        }
        if ("key".equals(localName)) {
            keyElementBegin = false;
        }
        if ("string".equals(localName)) {
            valueElementBegin = false;
        }
        if ("array".equals(localName)) {
            root = list.removeFirst();
        }
        if ("dict".equals(localName)) {
            root = list.removeFirst();
        }
    }
}  