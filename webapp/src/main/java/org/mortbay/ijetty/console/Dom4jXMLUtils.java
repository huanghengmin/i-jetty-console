package org.mortbay.ijetty.console;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Dom4jXMLUtils {

    private final String charset = "UTF-8";

    /**
     *
     * @param file
     * @return
     */
	public Document getDocument(File file) {
		try {
			if(file.exists()){
                SAXReader saxReader = new SAXReader();
                saxReader.setEncoding(charset);
                Document  document = saxReader.read(file);
                return document;
            }
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        return null;
	}

    public Document getNewDocument(){
        Document document = DocumentHelper.createDocument();
        return document;
    }

    /**
     *
     * @param document
     * @return
     */
    public Element getRootElement(Document document) {
        return document.getRootElement();
    }

    /**
     *
     * @param key
     * @param isAttribute
     * @param document
     * @return
     */
	public String get(String key, Boolean isAttribute,Document document) {
		try {
			List list = document.selectNodes(key);
			if (list != null && list.size() > 0) {
				if (isAttribute) {
					return ((Attribute) list.iterator().next()).getText();
				} else {
					return ((Element) list.iterator().next()).getText();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     *
     * @param key
     * @param isAttribute
     * @param document
     * @return
     */
	public List getList(String key, Boolean isAttribute,Document document) {
		try {
			if (!isAttribute) {
				List list = document.selectNodes(key);
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     *
     * @param key
     * @param value
     * @param isAttribute
     * @param document
     */
	public void set(String key, String value, Boolean isAttribute,Document document) {
		try {
			if (isAttribute) {
				List list = document.selectNodes(key);
				((Attribute) list.iterator().next()).setValue(value);
			} else {
				List list = document.selectNodes(key);
				((Element) list.iterator().next()).setText(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     *
     * @param key
     * @param value
     * @param isAttribute
     * @param document
     */
	public void delete(String key, String value, Boolean isAttribute,Document document) {
		try {
			List list = document.selectNodes(key);
			Element element = (Element) list.iterator().next();
			if (isAttribute) {
				List attributeList = document.selectNodes(key + "/@" + value);
				Attribute attribute = (Attribute) attributeList.iterator().next();
				element.remove(attribute);
			} else {
				List childList = document.selectNodes(key + "/" + value);
				Element childElement = (Element) childList.iterator().next();
				element.remove(childElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     *
     * @param key
     * @param stringArray
     * @param isAttribute
     * @param document
     * @return
     */
	public Element insert(String key, String[] stringArray, Boolean isAttribute,Document document) {
		Element element = null;
		try {
			List list = document.selectNodes(key);
			element = (Element) list.iterator().next();
			if (isAttribute) {
				element = element.addAttribute(stringArray[0], stringArray[1]);
			} else {
				element = element.addElement(stringArray[0]);
				element.setText(stringArray[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element;
	}

    /**
     *
     * @param document
     * @param path
     * @throws java.io.IOException
     */
	public void writeXML(Document document,String path) throws IOException {
		try {
            document.setXMLEncoding(charset);
            OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(path), charset);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(charset);
            XMLWriter writer = new XMLWriter(outWriter, format);
            writer.write(document);
            writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

    /**
     *
     * @param file
     * @param document
     * @param encoding
     */
	public void writeXML(String file, Document document, String encoding) {
		try {
            if(encoding!=null) {
                document.setXMLEncoding(encoding);
                OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(file), encoding);
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding(encoding == null ? format.getEncoding() : encoding);
                XMLWriter writer = new XMLWriter(outWriter, format);
                writer.write(document);
                writer.close();
            }else {
                document.setXMLEncoding(charset);
                OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(file), charset);
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding(charset);
                XMLWriter writer = new XMLWriter(outWriter, format);
                writer.write(document);
                writer.close();
            }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}