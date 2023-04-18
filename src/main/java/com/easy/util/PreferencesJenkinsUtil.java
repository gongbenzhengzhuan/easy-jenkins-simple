package com.easy.util;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import com.easy.enums.EasyJenkinsEnum;

import java.util.prefs.Preferences;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class PreferencesJenkinsUtil {

    public static void put(String key, String value) {
        Preferences prefs = Preferences.userNodeForPackage(PreferencesJenkinsUtil.class);
        prefs.put(key, value);
    }

    public static String get(String key) {
        Preferences prefs = Preferences.userNodeForPackage(PreferencesJenkinsUtil.class);
        return prefs.get(key, null);
    }

    public static void delete(String key) {
        Preferences prefs = Preferences.userNodeForPackage(PreferencesJenkinsUtil.class);
        prefs.remove(key);
    }
    public static void main(String[] args) {
        PreferencesJenkinsUtil.delete(EasyJenkinsEnum.EASY_JENKINS_PATH.getParam());
        PreferencesJenkinsUtil.delete(EasyJenkinsEnum.EASY_JENKINS_FILE_ID.getParam());
    }
}
