package com.alibaba.nacos.client.utils;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.alibaba.nacos.client.utils.LogUtils.NAMING_LOGGER;
import static org.junit.Assert.assertTrue;

public class ChooserTest {
    
    @Test
    public void TestChooser() {
        List<Instance> hosts = getInstanceList();
        List<Pair<Instance>> hostsWithWeight = new ArrayList<>();
       
        for (Instance host : hosts) {
            if (host.isHealthy()) {
                hostsWithWeight.add(new Pair<Instance>(host, host.getWeight()));
            }
        }
        Chooser<String, Instance> vipChooser = new Chooser<>("www.taobao.com", hostsWithWeight);
        NAMING_LOGGER.debug("vipChooser.refresh");
        
    }
    
    private List<Instance> getInstanceList() {
        List<Instance> list = new ArrayList<>();
        int size = ThreadLocalRandom.current().nextInt(0,100);
        for (int i = 0; i <= size; i++) {
            Instance instance = new Instance();
            instance.setInstanceId(String.valueOf(i));
            double weight = ThreadLocalRandom.current().nextDouble(0,1000);
            list.add(instance);
        }
        return list;
    }
}
