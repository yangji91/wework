package com.robot.util;

import java.util.ArrayList;
import java.util.List;

public class SpliteUtil {

    public static List<List > splistList(List list, int splitSize) {
        if (null == list) {
            return null;
        }
        int listSize = list.size();
        List<List > newList = new ArrayList<List >();
        if (listSize < splitSize) {
            newList.add(list);
            return newList;
        }
        int addLength = splitSize;
        int times = listSize / splitSize;
        if (listSize % splitSize != 0) {
            times += 1;
        }
        int start = 0;
        int end = 0;
        int last = times - 1;
        for (int i = 0; i < times; i++) {
            start = i * splitSize;
            if (i < last) {
                end = start + addLength;
            } else {
                end = listSize;
            }
            newList.add(list.subList(start, end));
        }
        return newList;
    }
}
