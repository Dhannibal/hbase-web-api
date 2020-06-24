package com.cuit.hbase.ServiceImplements;

import com.cuit.hbase.Service.LoginService;
import com.cuit.hbase.dao.HbaseConnector;
import com.cuit.hbase.model.User;
import com.cuit.hbase.utils.Response;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    HbaseConnector hbaseConnector;

    @Override
    public Response login(User user) {
        String rowKey = user.getEmail();
        String pwd = user.getPwd();
        Map<byte[], byte[]> basicInfo = hbaseConnector.getFamily(rowKey, "BasicInfo");
        if(basicInfo == null) return  new Response(201, "没有该用户", "fail");
        String vaild  = Bytes.toString(basicInfo.get(Bytes.toBytes("pwd")));
        if(vaild.equals(pwd)) {
            return new Response(200, "登录成功", "success");
        }
        return new Response(201, "登录失败", "fail");
    }

    @Override
    public Response register(User user) {
        hbaseConnector.PutData(user.getEmail(), "BasicInfo", "pwd", user.getPwd());
        hbaseConnector.PutData(user.getEmail(), "BasicInfo", "name", user.getName());
        hbaseConnector.PutData(user.getEmail(), "BasicInfo", "sex", user.getSex());
        return new Response(200, "注册成功", "success");
    }
}
