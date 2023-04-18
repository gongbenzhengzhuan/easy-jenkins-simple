package com.easy.annotation.find;

import com.easy.annotation.IfMapper;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class ReturnObject {

    private static String packageName;

    public ReturnObject() {
    }

    public static <E> E getObj(String pack, String name) {
        packageName = pack;
        return (E) object(name);
    }

    public static <E> E getObj(Class<?> cls, String name) {
        IfMapper annotation = cls.getAnnotation(IfMapper.class);
        packageName = annotation.pack();
        return (E) object(name);
    }

    public static <E> E object(String name) {
        return GetObject.getObject(name, packageName);
    }

    public static <E> E enumObject(String name) {
        return GetObject.getEnumObject(name, packageName);
    }

}
