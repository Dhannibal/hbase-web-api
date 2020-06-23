package com.cuit.hbase.Service;



public interface userBehavior {

    public Boolean concernedTo(String k1, String k2);

    public Boolean setFamilyVal(String rowKey, String Family, String con, String val);

    public Boolean createInfo();
}
