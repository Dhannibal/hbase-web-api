package com.cuit.hbase.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class HbaseConnector {
    private Connection connection;
    private Admin admin;

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

        try {
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void ShowAllTables()throws  IOException {
        System.out.println("show all tables");
        TableName[] tableNames = admin.listTableNames();
        for (TableName ts : tableNames) {
            System.out.println(ts.toString());
        }
    }

    public boolean NamespaceExist(String name) {
        try{
            NamespaceDescriptor[] namespaceDescriptors =  admin.listNamespaceDescriptors();
            for(NamespaceDescriptor NamespaceName :  namespaceDescriptors) {
                if(NamespaceName.getName().equals(name)) return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createNamespace(String name) {
        if(NamespaceExist(name)) return false;
        try {
            NamespaceDescriptor namespace = NamespaceDescriptor.create(name).build();
            admin.createNamespace(namespace);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean DeleteNamespace(String name) {
        if(!NamespaceExist(name)) return false;
        try {
            admin.deleteNamespace(name);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tableExists(String tableName) {
        boolean exists = false;
        try {
            exists =  admin.tableExists(TableName.valueOf(tableName));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return exists;
    }

    public  void DeleteTableByName(String tableName) {
        if(!tableExists(tableName)) {
            return;
        }
        try {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName, String... families) {

        if(tableExists(tableName)) {

            System.out.println(tableName + " already exist!");
            return ;
        }

        HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
        try{
            for(String family:families) {
                tableDescriptor.addFamily(new HColumnDescriptor(family));
            }
            admin.createTable(tableDescriptor);
        }catch (IOException e){
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

    public void PutData(String tableName, String rowKey, String family, String data, String info) {
        Put put = new Put(rowKey.getBytes()); //指定rowKey
        put.addColumn(family.getBytes(), data.getBytes(), info.getBytes());
        try {
            AddInfo(tableName, put);
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

    public void deleteRowKey(String tableName, String rowKey) {
        try {
            Table table = GetTable(tableName);
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<byte[], byte[]> getFamily(String tableName, String rowKey, String family)  {
        try {
            Get get = new Get(Bytes.toBytes(rowKey));
            Table table = GetTable(tableName);
            Result result = table.get(get);
            return result.getFamilyMap(Bytes.toBytes(family));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public int getFamilyCount(String tableName, String rowKey, String family)  {
        return getFamily(tableName, rowKey, family).size();
    }

    //删除指定cell数据
    public void deleteByRowKeyCell(String tableName, String rowKey, String family, String con) throws IOException {

        try {
            Table table = GetTable(tableName);
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumns(Bytes.toBytes(family), Bytes.toBytes(con));
            table.delete(delete);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
