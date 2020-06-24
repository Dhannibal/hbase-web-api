package com.cuit.hbase.model;

import lombok.Data;

@Data
public class userKV {
    String mail;
    String name;

    public userKV(String mail, String name) {
        this.mail = mail;
        this.name = name;
    }
}
