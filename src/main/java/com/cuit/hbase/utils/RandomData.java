package com.cuit.hbase.utils;

import java.util.*;

public class RandomData {
    public String randomMail() {
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        String[] suf = new String[3];
        suf[0] = "@qq.com";suf[1] = "@gmail.com";suf[2] = "@163.com";
        for(int i = 1; i <= 6; i++) {
            if(random.nextBoolean())
                res.append((random.nextInt(10)));
            else res.append('a'+random.nextInt(26));
        }
        res.append(suf[random.nextInt(3)]);
        return res.toString();
    }

    public String randomName() {
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        for(int i = 1; i <= 5; i++) {
            if(random.nextBoolean())
                res.append((random.nextInt(10)));
            else res.append('a'+random.nextInt(26));
        }
        return res.toString();
    }

    public String randomSex() {
        Random random = new Random();
        return random.nextBoolean()?"男":"女";
    }

    public String randomPwd() {
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        for(int i = 1; i <= 6; i++) {
            int s = random.nextInt(3);
            if(s == 0) res.append(random.nextInt(10)+'0');
            else if(s == 1) res.append(random.nextInt(26)+'a');
            else res.append(random.nextInt(26)+'A');
        }
        return res.toString();
    }
}
