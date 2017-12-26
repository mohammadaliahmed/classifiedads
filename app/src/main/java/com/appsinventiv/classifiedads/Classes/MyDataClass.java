package com.appsinventiv.classifiedads.Classes;

import java.util.Map;

/**
 * Created by AliAh on 26/12/2017.
 */

public class MyDataClass {
    public MyDataClass() { }
//    public String name;
    public Map<String, Object> properties;


    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public MyDataClass(Map<String, Object> properties) {
        this.properties = properties;
    }
}
