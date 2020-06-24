package com.cuit.hbase.ServiceImplements;

import com.cuit.hbase.Entity.UserInfo;
import com.cuit.hbase.Service.userBehavior;
import com.cuit.hbase.dao.HbaseConnector;
import com.cuit.hbase.model.userKV;
import com.cuit.hbase.utils.RandomData;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class userBehaviorImp implements userBehavior {
    @Autowired
    HbaseConnector connector;

    @Override
    public Boolean concernedTo(String k1, String k2) {
        String name1 = Bytes.toString(connector.getFamily(k1, "BasicInfo").get(Bytes.toBytes("name")));
        String name2 = Bytes.toString(connector.getFamily(k2, "BasicInfo").get(Bytes.toBytes("name")));

        connector.PutData(k1, "concernedId", k2, name2);
        connector.PutData(k2, "fansId", k1, name1);
        return true;
    }

    @Override
    public Boolean setFamilyVal(String rowKey, String Family, String con, String val) {
        connector.PutData(rowKey, Family, con, val);
        return true;
    }

    @Override
    public Boolean createInfo() {
        RandomData randomData = new RandomData();
        Map<String, Boolean> map= new HashMap<String, Boolean>();

        for(int i = 1; i <= 1000; i++) {
            String rowKey = randomData.randomMail();
            if(!map.containsKey(rowKey)) {
                setFamilyVal(rowKey, "BasicInfo", "name", randomData.randomName());
                setFamilyVal(rowKey, "BasicInfo", "sex", randomData.randomSex());
                setFamilyVal(rowKey, "BasicInfo", "pwd", randomData.randomPwd());
                map.put(rowKey, true);
            }
            else i--;
        }

        System.out.println("基本数据已生成");

        List<String> list = new ArrayList<String>(map.keySet());
        Random random = new Random();
        for(int i  = 0; i < list.size(); i++) {
            int cnt = random.nextInt(10)+2;
            for(int j = i+1; j < list.size() && cnt != 0; j++) {
                if(random.nextBoolean())
                    concernedTo(list.get(i), list.get(j));
                cnt--;
            }
        }
        return true;
    }

    @Override
    public Boolean cancelConcernedTo(String k1, String k2) {
        connector.deleteByRowKeyCell(k1, "concernedId", k2);
        connector.deleteByRowKeyCell(k2, "fansId", k1);
        return true;
    }

    @Override
    public  List<userKV> getConcerned(String k) {
        List<userKV> res = new ArrayList<userKV>();
        Map<byte[], byte[]> concerns = connector.getFamily(k, "concernedId");
        return getUserKVS(res, concerns);
    }

    @Override
    public List<userKV> getMyFans(String k) {
        List<userKV> res = new ArrayList<userKV>();
        Map<byte[], byte[]> concerns = connector.getFamily(k, "fansId");
        return getUserKVS(res, concerns);
    }

    private List<userKV> getUserKVS(List<userKV> res, Map<byte[], byte[]> concerns) {
        for(Map.Entry<byte[], byte[]> entry:concerns.entrySet()) {
            userKV userinfo = new userKV();
            userinfo.setMail(Bytes.toString(entry.getKey()));
            userinfo.setName(Bytes.toString(entry.getValue()));
            res.add(userinfo);
        }
        return res;
    }

    @Override
    public Boolean isConcerned(String k1, String k2) {
        return connector.getFamily(k1, "concernedId").containsKey(Bytes.toBytes(k2));
    }

    @Override
    public List<userKV> getNotConcerned(String k) {
        List<userKV> res = new ArrayList<>();
        List<userKV> myConcerned = getConcerned(k);
        Set<String> set = new HashSet<>();
        for(userKV user : myConcerned) {
            set.add(user.getMail());
        }
        List<UserInfo> allTable = connector.getAllTable();
        for(UserInfo userInfo : allTable) {
            if((!set.contains(userInfo.getRowKey())) && (!userInfo.getRowKey().equals(k))) {
                res.add(new userKV(userInfo.getRowKey(), userInfo.getBasicInfo().getName()));
            }
        }
        return res;
    }
}
