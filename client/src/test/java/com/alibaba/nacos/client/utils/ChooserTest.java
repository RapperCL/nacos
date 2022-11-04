package com.alibaba.nacos.client.utils;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import org.checkerframework.checker.signature.qual.DotSeparatedIdentifiersOrPrimitiveType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;

public class ChooserTest {
    
    //Test the correctness of Chooser, the weight of the final selected instance must be greater than 0
    @Test
    public void TestChooser() {
        List<Instance> hosts = getInstanceList();
        Instance target = getRandomInstance(hosts);
        assertTrue(hosts.contains(target) && target.getWeight() > 0);
    }
  
    private List<Instance> getInstanceList() {
        List<Instance> list = new ArrayList<>();
        int size = ThreadLocalRandom.current().nextInt(0, 1000);
        for (int i = 0; i < size; i++) {
            Instance instance = new Instance();
            instance.setInstanceId(String.valueOf(i));
            instance.setWeight(i);
            list.add(instance);
        }
        return list;
    }
    
    // If there is only one instance whose weight is not zero, it will be selected
    @Test
    public void TestOnlyOneInstanceWeightIsNotZero() {
        List<Instance> hosts = getOneInstanceNotZeroList();
        
        Instance target = getRandomInstance(hosts);
        assertTrue(target.getWeight() > 0);
    }
    
    private List<Instance> getOneInstanceNotZeroList() {
        List<Instance> list = new ArrayList<>();
        int size = ThreadLocalRandom.current().nextInt(0, 1000);
        int notZeroIndex = ThreadLocalRandom.current().nextInt(0, size - 1);
       
        for (int i = 0; i < size; i++) {
            Instance instance = new Instance();
            instance.setInstanceId(String.valueOf(i));
            if (i == notZeroIndex) {
                instance.setWeight(notZeroIndex + 1);
            } else {
                instance.setWeight(0);
            }
            list.add(instance);
        }
        return list;
    }
    
    @Test
    public void testInstanceWeightAllZero(){
       List<Instance> hosts = getInstanceWeightAllZero();
       Instance target = getRandomInstance(hosts);
       
       assertTrue(target.getWeight() == 0);
    }
    
    private List<Instance> getInstanceWeightAllZero(){
        List<Instance> list = new ArrayList<>();
        int size = ThreadLocalRandom.current().nextInt(0, 1000);
    
        for (int i = 0; i < size; i++) {
            Instance instance = new Instance();
            instance.setInstanceId(String.valueOf(i));
            instance.setWeight(0);
            list.add(instance);
        }
        return list;
    }
    
    private Instance getRandomInstance(List<Instance> hosts){
        List<Pair<Instance>> hostsWithWeight = new ArrayList<>();
        for (Instance host : hosts) {
            if (host.isHealthy()) {
                hostsWithWeight.add(new Pair<>(host, host.getWeight()));
            }
        }
        Chooser<String, Instance> vipChooser = new Chooser<>("www.taobao.com", hostsWithWeight);
        
        return vipChooser.randomWithWeight();
    }
    
}
