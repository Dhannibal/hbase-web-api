package com.cuit.hbase.Service;

import com.cuit.hbase.model.User;
import com.cuit.hbase.utils.Response;

public interface LoginService {
    public Response login(User user);
    public Response register(User user);
}