package com.jenscribe.demo.Model;

import lombok.Data;

@Data
public class XmlElement {
    String colName;
    String head;
    int isFather = 0;
    String parentName;
}
