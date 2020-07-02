package com.cuit.hbase.Service;


import com.cuit.hbase.model.User;
import com.cuit.hbase.model.userKV;
import com.cuit.hbase.model.userPage;

import java.util.List;

public interface userBehavior {

    public Boolean concernedTo(String k1, String k2);

    public Boolean setFamilyVal(String rowKey, String Family, String con, String val);

    public Boolean createInfo();

    public Boolean cancelConcernedTo(String k1, String k2);

    public List<userPage> getConcerned(String k);

    public List<userPage> getMyFans(String k);

    public Boolean isConcerned(String k1, String k2);

    public List<userPage> getNotConcerned(String k);
}
