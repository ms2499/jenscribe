package com.jenscribe.demo.Model;

import lombok.Data;

@Data
public class EnsFileAttr<T> {
    String type;
    String name;
    T value;
    String head;
}
