package com.cuit.hbase.Service;


import com.cuit.hbase.model.userKV;

import java.util.List;

public interface userBehavior {

    public Boolean concernedTo(String k1, String k2);

    public Boolean setFamilyVal(String rowKey, String Family, String con, String val);

    public Boolean createInfo();

    public Boolean cancelConcernedTo(String k1, String k2);

    public List<userKV> getConcerned(String k);

    public List<userKV> getMyFans(String k);

    public Boolean isConcerned(String k1, String k2);

    public List<userKV> getNotConcerned(String k);
}
