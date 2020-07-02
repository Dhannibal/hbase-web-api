package com.cuit.hbase.model;

import lombok.Data;

@Data
public class userPage {
    String email;
    String name;
    String sex;
    int fansNum;
    int concernedNum;
}
