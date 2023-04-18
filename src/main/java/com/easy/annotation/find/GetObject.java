package com.easy.annotation.find;

import com.easy.annotation.tag;
import com.easy.util.ClassUtil;

import java.util.Iterator;
import java.util.Set;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class GetObject {

    public GetObject() {
    }

    public static <E> E getObject(String name, String packageName) {
        Object t = null;

        try {
            Set<Class<?>> classes = ClassUtil.getClasses(packageName);
            Iterator var5 = classes.iterator();

            while (var5.hasNext()) {
                Class c = (Class) var5.next();
                if (c.isAnnotationPresent(tag.class) && classNameAndObject(c).equals(name)) {
                    t = Class.forName(c.getName()).getDeclaredConstructor().newInstance();
                }
            }
        } catch (Exception var7) {
            System.out.println("没有找到该类");
        }

        return (E) t;
    }

    public static <E> E getEnumObject(String name, String packageName) {
        Object t = null;

        try {
            Set<Class<?>> classes = ClassUtil.getClasses(packageName);
            Iterator var5 = classes.iterator();

            while (var5.hasNext()) {
                Class c = (Class) var5.next();
                if (c.isAnnotationPresent(tag.class) && classNameAndObject(c).equals(name)) {
                    Object[] enumConstants = c.getEnumConstants();
                    t = enumConstants[0];
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return (E) t;
    }

    private static tag objectPattern(Class c) {
        return (tag) c.getAnnotation(tag.class);
    }

    private static String className(Class c) {
        String str = c.getName();
        str = str.substring(str.lastIndexOf(".") + 1);
        return str;
    }

    private static String classNameAndObject(Class c) {
        String name = objectPattern(c).name();
        return !"".equals(name) ? name : className(c);
    }

}
