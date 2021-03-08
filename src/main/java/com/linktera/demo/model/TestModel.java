package com.linktera.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TestModel implements Serializable {

    private static final long serialVersionUID = -5924028033325088262L;


    private String name;
}
