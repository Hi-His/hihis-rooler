package com.bsoft.rooler.zookeeper;

import com.alibaba.fastjson.JSON;
import com.bsoft.rooler.property.ZkProperty;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.zookeeper
 * @Description:
 * @date : 2021/11/27 14:11
 */
@Component
public class ZookeeperMain {


    protected ZooKeeper zk;


    @Autowired
    ZkProperty zkProperty;

    @PostConstruct
    public void connectToZK() throws IOException {

        String connectString = zkProperty.getConnectString();

        String auth = zkProperty.getAuth();

        zk = new ZooKeeper(connectString,
                Integer.parseInt("30000"),
                new MyWatcher(), false);

        byte[] bytes = auth.getBytes();

        zk.addAuthInfo("digest", bytes);

    }

    public Map<String, Object> getServcieList() {
        String basePath = "/ssdev/serverNodes";
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<String> children = zk.getChildren(basePath, false);
            for (String child : children) {
               String pathN = basePath + "/" +child;
               List<String> basePath1 = zk.getChildren(pathN, false);
                for (String name : basePath1) {

                         String realPath = pathN + "/" +name;
                        byte[] data = zk.getData(realPath, true, new Stat());
                        String s = new String(data);

                        Object parse = JSON.parse(s);
                        map.put(name,parse);

                }
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return map;
    }


    private class MyWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {

        }
    }
}
