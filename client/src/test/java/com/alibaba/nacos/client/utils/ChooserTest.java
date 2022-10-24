package com.alibaba.nacos.client.utils;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.nacos.client.utils.LogUtils.NAMING_LOGGER;

public class ChooserTest {

    @Test
    public void TestChooser(){
        List<Instance> hosts = getInstanceList();
        List<Pair<Instance>> hostsWithWeight = new ArrayList<>();
        for (Instance host : hosts) {
            if (host.isHealthy()) {
                hostsWithWeight.add(new Pair<Instance>(host, host.getWeight()));
            }
        }
        Chooser<String, Instance> vipChooser = new Chooser<>("www.taobao.com");
        vipChooser.refresh(hostsWithWeight);
        NAMING_LOGGER.debug("vipChooser.refresh");
        vipChooser.randomWithWeight();
    }
    
    private List<Instance> getInstanceList(){
        List<Instance> list = new ArrayList<>();
        for(int i =0; i <= 3; i++){
            Instance instance = new Instance();
            instance.setInstanceId(String.valueOf(i));
            if(i == 2){
                instance.setWeight(2);
            }else{
                instance.setWeight(3);
            }
            list.add(instance);
        }
        return list;
    }
}
