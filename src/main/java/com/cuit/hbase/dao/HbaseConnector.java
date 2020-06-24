package com.cuit.hbase.dao;

import com.cuit.hbase.Entity.UserInfo;
import com.cuit.hbase.model.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class HbaseConnector {
    private Connection connection;

    public HbaseConnector() {
        BasicConfigurator.configure();
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","39.99.156.133");
        conf.set("hbase.zookeeper.property.clientPort","2181");

        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddInfo(String tableName, Put p) {
        try {
            Table table = GetTable(tableName);
            table.put(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PutData(String rowKey, String family, String data, String info) {
        Put put = new Put(rowKey.getBytes()); //指定rowKey
        put.addColumn(family.getBytes(), data.getBytes(), info.getBytes());
        try {
            AddInfo("UserInfo", put);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Table GetTable(String tableName) {
        try {
            return connection.getTable(TableName.valueOf(tableName));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public List<UserInfo> getAllTable() {
        List<UserInfo> table = new ArrayList<UserInfo>();

        ResultScanner results = scanData("UserInfo");
        for(Result result : results) {
            String rowKey = new String(result.getRow());
            UserInfo userInfo = new UserInfo();
            userInfo.setRowKey(rowKey);

            //基本信息
            User user = new User();
            Map<byte[], byte[]> basicInfo = result.getFamilyMap(Bytes.toBytes("BasicInfo"));
            user.setEmail(rowKey);
            user.setName(Bytes.toString(basicInfo.get(Bytes.toBytes("name"))));
            user.setPwd(Bytes.toString(basicInfo.get(Bytes.toBytes("pwd"))));
            user.setSex(Bytes.toString(basicInfo.get(Bytes.toBytes("sex"))));
            userInfo.setBasicInfo(user);

            //关注列表
            Map<byte[], byte[]> concernedId = result.getFamilyMap(Bytes.toBytes("concernedId"));
            List<String> userConcerned = new ArrayList<String>();
            for(Map.Entry<byte[], byte[]> entry:concernedId.entrySet()) {
                userConcerned.add(Bytes.toString(entry.getValue()));
            }

            userInfo.setConcernedId(userConcerned);

            //粉丝列表
            concernedId = result.getFamilyMap(Bytes.toBytes("fansId"));
            List<String> userFans = new ArrayList<String>();
            for(Map.Entry<byte[], byte[]> entry:concernedId.entrySet()) {
                userFans.add(Bytes.toString(entry.getValue()));
            }
            userInfo.setFansId(userFans);

            table.add(userInfo);
        }

        return table;
    }

    public ResultScanner scanData(String tableName) {
        try {
            Scan scan = new Scan();
            Table table = GetTable(tableName);
            return table.getScanner(scan);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void deleteRowKey(String rowKey) {
        try {
            Table table = GetTable("UserInfo");
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<byte[], byte[]> getFamily(String rowKey, String family)  {
        try {
            Get get = new Get(Bytes.toBytes(rowKey));
            Table table = GetTable("UserInfo");
            Result result = table.get(get);
            return result.getFamilyMap(Bytes.toBytes(family));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public int getFamilyCount( String rowKey, String family)  {
        return getFamily(rowKey, family).size();
    }

    //删除指定cell数据
    public void deleteByRowKeyCell(String rowKey, String family, String con)  {
        try {
            Table table = GetTable("UserInfo");
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumns(Bytes.toBytes(family), Bytes.toBytes(con));
            table.delete(delete);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
