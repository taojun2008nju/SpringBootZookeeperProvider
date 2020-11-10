package com.test.kafka;

import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

/**
 * @author Administrator
 * @date 2020/9/21 20:56:00
 * @description TODO
 */
public class KafkaPartitioner implements Partitioner {

    private int partitionNum = 0;

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        if (key == null) {
            Random rand = new Random();
            return rand.nextInt(numPartitions);
        }
        int floorMod = Math.floorMod(key.hashCode(), numPartitions);
        return floorMod;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
