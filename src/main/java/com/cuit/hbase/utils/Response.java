package com.cuit.hbase.utils;
import lombok.Data;

import java.io.Serializable;
@Data
public class Response  implements Serializable{
    private int code;
    private String msg;
    private Object data;

    public Response(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
