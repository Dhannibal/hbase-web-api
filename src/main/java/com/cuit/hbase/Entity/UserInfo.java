package com.cuit.hbase.Entity;

import com.cuit.hbase.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    String rowKey;
    User BasicInfo;
    List<String> concernedId;
    List<String> fansId;
}
