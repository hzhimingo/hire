package com.hzhimingo.hire.user;

import cn.hutool.core.util.NumberUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

    private static final String[] c = new String[]{"蒋巷街社区", "胜利村", "立新村", "三洞村", "山尾村", "豫章桥头社区",
            "五丰村", "联圩村", "滁北村", "蒋巷村", "白岸村", "柏岗山村", "叶楼村", "垾上村", "玉丰村", "北望村", "洲头村", "高梧村",
            "河边村"};

    public static void main(String[] args) {
        Set<String> addrSet = new HashSet<>();

        while (addrSet.size() < 800) {
            int cIndex = NumberUtil.generateRandomNumber(0, c.length - 1, 1)[0];
            int doorNumber = NumberUtil.generateRandomNumber(1, 90, 1)[0];
            addrSet.add("江西省南昌市南昌县蒋巷镇" + c[cIndex] + doorNumber + "号");
        }

        addrSet.forEach(System.out::println);
    }
}
