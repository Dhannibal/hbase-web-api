package com.cuit.hbase.ServiceImplements;

import com.cuit.hbase.Entity.UserInfo;
import com.cuit.hbase.Service.userBehavior;
import com.cuit.hbase.dao.HbaseConnector;
import com.cuit.hbase.model.User;
import com.cuit.hbase.model.userKV;
import com.cuit.hbase.model.userPage;
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

        for(int i = 1; i <= 50; i++) {
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
            for(int j = i+1; j < list.size(); j++) {
                if(random.nextBoolean())
                    concernedTo(list.get(i), list.get(j));
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
    public  List<userPage> getConcerned(String k) {
        List<userPage> res = new ArrayList<userPage>();
        Map<byte[], byte[]> concerns = connector.getFamily(k, "concernedId");
        return getUserPages(res, concerns);
    }

    @Override
    public List<userPage> getMyFans(String k) {
        List<userPage> res = new ArrayList<userPage>();
        Map<byte[], byte[]> concerns = connector.getFamily(k, "fansId");
        return getUserPages(res, concerns);
    }

    private List<userPage> getUserPages(List<userPage> res, Map<byte[], byte[]> concerns) {
        for(Map.Entry<byte[], byte[]> entry:concerns.entrySet()) {
            userPage userinfo = new userPage();
            userinfo.setEmail(Bytes.toString(entry.getKey()));
            userinfo.setName(Bytes.toString(entry.getValue()));
            userinfo.setSex(Bytes.toString(connector.getFamily(userinfo.getEmail(), "BasicInfo").get(Bytes.toBytes("sex"))));
            userinfo.setConcernedNum(connector.getFamilyCount(userinfo.getEmail(), "concernedId"));
            userinfo.setFansNum(connector.getFamilyCount(userinfo.getEmail(), "fansId"));
            res.add(userinfo);
        }
        return res;
    }


    @Override
    public Boolean isConcerned(String k1, String k2) {
        Map<byte[], byte[]> concernedId = connector.getFamily(k1, "concernedId");
        if(concernedId == null) return false;
        return concernedId.containsKey(Bytes.toBytes(k2));
    }

    @Override
    public List<userPage> getNotConcerned(String k) {
        List<userPage> res = new ArrayList<>();
        List<userPage> myConcerned = getConcerned(k);
        Set<String> set = new HashSet<>();
        for(userPage user : myConcerned) {
            set.add(user.getEmail());
        }
        List<UserInfo> allTable = connector.getAllTable();
        for(UserInfo userInfo : allTable) {
            if((!set.contains(userInfo.getRowKey())) && (!userInfo.getRowKey().equals(k))) {
                userPage user = new userPage();
                user.setEmail(userInfo.getRowKey());
                user.setName(userInfo.getBasicInfo().getName());
                user.setSex(userInfo.getBasicInfo().getSex());
                user.setConcernedNum(connector.getFamilyCount(userInfo.getRowKey(), "concernedId"));
                user.setFansNum(connector.getFamilyCount(userInfo.getRowKey(), "fansId"));
                res.add(user);
            }
        }
        return res;
    }
}
