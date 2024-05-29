package com.jenscribe.demo.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenscribe.demo.CharsetTool;
import com.jenscribe.demo.Dao.EnscribeDao;
import com.jenscribe.demo.EnsXml;
import com.jenscribe.demo.Model.XmlElement;
import com.tandem.ext.enscribe.EnscribeFile;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
public class EnscribeService {
    @Autowired
    EnscribeDao ensDao;

    CharsetTool charsetTool = new CharsetTool();

    public List<String> getData(String fileName, int rows){
        List<String> result = new ArrayList<>();

        EnscribeFile ensFile = ensDao.openFile(fileName);
        try {
            //讀檔完用反射取得class中所有欄位名稱跟值
            System.out.println("EnscribeService : start handle record");
            for (int rownum = 0; rownum < rows; rownum++) {
                Object fileRec = ensDao.getData(ensFile);
                if (fileRec == null)
                    break;
                String objClassName = fileRec.getClass().getName();
                System.out.println("EnscribeService : ClassName = " + objClassName);
                HashMap<String, Object> map = new HashMap<>();
                Field[] recFields = fileRec.getClass().getDeclaredFields();
                for (Field recField : recFields) {
                    if (!recField.toString().contains("public"))
                        continue;
                    String propertyName = recField.toString().substring(recField.toString().indexOf(objClassName) + objClassName.length() + 1);
                    propertyName = propertyName.replaceAll("_", "-").toUpperCase();
                    if (recField.toString().contains("[]")) {
                        String arrType = recField.get(fileRec).getClass().getName().substring(0, 2);
                        switch (arrType) {
                            case "[J":
                                long[] larr = (long[]) recField.get(fileRec);
                                for (int i = 0; i < larr.length; i++) {
                                    map.put(propertyName + "(" + i + ")", larr[i]);
                                    //System.out.println(propertyName + "[" + i + "]" + " Value = " + larr[i]);
                                }
                                break;
                            case "[I":
                                int[] iarr = (int[]) recField.get(fileRec);
                                for (int i = 0; i < iarr.length; i++) {
                                    map.put(propertyName + "(" + i + ")", iarr[i]);
                                    //System.out.println(propertyName + "[" + i + "]" + " Value = " + iarr[i]);
                                }
                                break;
                            case "[L":
                                if (recField.toString().contains("BigDecimal")) {
                                    BigDecimal[] bds = (BigDecimal[]) recField.get(fileRec);
                                    for (int i = 0; i < bds.length; i++) {
                                        map.put(propertyName + "(" + i + ")", bds[i].toString());
                                        //System.out.println(propertyName + "[" + i + "]" + " Value = " + bds[i].toString());
                                    }
                                } else { //其他自訂物件的array
                                    Object[] inObjs = (Object[]) recField.get(fileRec);
                                    for (int i = 0; i < inObjs.length; i++) {
                                        //System.out.println("--- " + i + " --------------------------------------------");
                                        Field[] objFs = inObjs[i].getClass().getFields();
                                        for (Field objf : objFs) {
                                            //System.out.print("|");
                                            String inObjName = inObjs[i].getClass().getName();
                                            String inPropertyName = objf.toString().substring(objf.toString().indexOf(inObjName) +
                                                    inObjName.length() + 1);
                                            map.put(inPropertyName, objf.get(inObjs[i]));
                                            //System.out.println(inPropertyName + " Value = " + objf.get(inObjs[i]).toString());
                                        }
                                        //System.out.println("--- " + i + " --------------------------------------------");
                                    }
                                }
                                break;
                            default:
                                System.out.println("Unknown type, name = " + arrType);
                                break;
                        }
                    } else {
                        //System.out.println(propertyName + " Value = " + recField.get(fileRec));
                        if (recField.toString().contains("String")) {
                            String str = (String) recField.get(fileRec);
                            byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
                            map.put(propertyName, new String(bytes, "BIG5"));
                        } else
                            map.put(propertyName, recField.get(fileRec));
                    }
                }
                ObjectMapper mapper = new ObjectMapper();
                result.add(mapper.writeValueAsString(map));
            }
            ensFile.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        ensFile.close();
        return null;
    }

    public List<XmlElement> getXml(String fileName){
        String result = "";
        String file = fileName.toUpperCase();
        List<XmlElement> xmlElements = new ArrayList<>();
        try {
            Document doc = EnsXml.doc;
            System.out.println(doc.getName());
            Element root = doc.getRootElement();
            Element fileRoot = root.element(file + "-R");

            getElement(fileRoot.elements(), xmlElements);

//            for(XmlElement xmlElement : xmlElements) {
//                System.out.println("ColName=" + xmlElement.getColName() + " head=" + charsetTool.utf82iso(xmlElement.getHead()));
//            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return xmlElements;
    }

    public void getElement(List<Element> sonElementList, List<XmlElement> xmlElements) {
        for (Element sonElement : sonElementList) {
            XmlElement xmlElement = new XmlElement();
            xmlElement.setColName(sonElement.getName());
            xmlElement.setHead(sonElement.attributeValue("head"));
            xmlElement.setParentName(sonElement.getParent().getName());
            if (!sonElement.elements().isEmpty()) {
                xmlElement.setIsFather(1);
                xmlElements.add(xmlElement);
                getElement(sonElement.elements(), xmlElements);
            }else{
                xmlElements.add(xmlElement);
            }
        }
    }
}
