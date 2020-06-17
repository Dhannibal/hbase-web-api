package com.cuit.hbase.model;

import lombok.Data;

@Data
public class User {
    String name;
    String email;
    String pwd;
    String rowkey;
}
