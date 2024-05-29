package com.jenscribe.demo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class EnsXml {
    public static Document doc = null;

    @PostConstruct
    public static void initXml() {
        try {
            SAXReader reader = new SAXReader();
            doc = reader.read("EnsFilesXml.xml");
        }catch (DocumentException de) {
            System.out.println(new String("XML讀檔失敗".getBytes(), StandardCharsets.ISO_8859_1));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
