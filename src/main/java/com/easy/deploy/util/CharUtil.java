package com.easy.deploy.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class CharUtil {

    public static Set<String> getProcessNumber(String result, Set<String> resultAll) {
        try {
            String[] resultArray = result.split("\n");
            for (String s : resultArray) {
                List<String> collect = Arrays.stream(s.split(" ")).collect(Collectors.toList());
                collect.removeIf(a -> a.equals("") || a.equals(" "));
                resultAll.add(collect.get(1));
            }
            return resultAll;
        } catch (Exception e) {
            return resultAll;
        }
    }


}
