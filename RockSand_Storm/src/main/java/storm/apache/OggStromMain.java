package storm.apache;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import com.google.common.collect.ImmutableList;

import storm.apache.bolt.DeleteBolt;
import storm.apache.bolt.InsertBolt;
import storm.apache.bolt.PrinterBolt;
import storm.apache.bolt.ResultBolt;
import storm.apache.bolt.UpdateBolt;

/**
 * 样例为: Ogg获取Sql日志,将Insert,update,Delte消息发给Kafka,再由Storm订阅Kafka,将操作数据进行流处理.
 * 
 * @author Administrator
 *
 */
public class OggStromMain {

	public static void main(String[] args) {
		// 定义Broker
		BrokerHosts brokerHosts = new ZkHosts("192.168.80.145:2181,192.168.80.146:2181,192.168.80.147:2181");
		// Topic,订阅话题
		String topic = "oggtopic";
		// 此Storm进程在Kafka的工作目录
		String zkRoot = "";
		// ID-组ID
		String spoutId = "oggTest";
		SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, topic, zkRoot, spoutId);
		// kafka消息格式--String
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		// 设定Zookeeper服务
		spoutConfig.zkServers = ImmutableList.of("192.168.80.145", "192.168.80.146", "192.168.80.147");
		spoutConfig.zkPort = 2181;
		/*
		 * 需要手动设置,LatestTime表示最近的消费消息开始
		 */
		spoutConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
		// 创建Storm的拓扑
		TopologyBuilder builder = new TopologyBuilder();
		// 创建kafka数据Spout对象 --- 水龙头,一直开启
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		// 设定拓扑的spout
		builder.setSpout("spout", kafkaSpout);
		/*
		 * 1.shuffleGrouping（随机分组） 
		 * 2.fieldsGrouping（按照字段分组，在这里即是同一个单词只能发送给一个Bolt）
		 * 3.allGrouping（广播发送，即每一个Tuple，每一个Bolt都会收到） 
		 * 4.globalGrouping（全局分组，将Tuple分配到task id值最低的task里面） 
		 * 5.noneGrouping（随机分派）--> 于shuffle行为相似
		 * 6.directGrouping（直接分组，指定Tuple与Bolt的对应发送关系） 
		 * 7.Local or shuffle Grouping
		 * 8.customGrouping （自定义的Grouping）
		 */
		builder.setBolt("printBolt", new PrinterBolt()).globalGrouping("spout");
		// 第二个参数streamid可以指定Bolt,但是在运行时当设置多个work时insertBolt可能存在多个Task运行,因而随机将会体现
		builder.setBolt("insertBolt", new InsertBolt()).shuffleGrouping ("printBolt", "insert");
		builder.setBolt("updateBolt", new UpdateBolt()).shuffleGrouping ("printBolt", "update");
		builder.setBolt("DeleteBolt", new DeleteBolt()).shuffleGrouping ("printBolt", "delete");
		// 汇聚
		builder.setBolt("resultBolt", new ResultBolt()).globalGrouping("insertBolt").globalGrouping("updateBolt")
				.globalGrouping("DeleteBolt");
		Config config = new Config();
		config.setDebug(true);
		if (args != null && args.length > 0) {
			// 远程执行
			config.setNumWorkers(2);
			try {
				StormSubmitter.submitTopologyWithProgressBar(OggStromMain.class.getSimpleName(), config,
						builder.createTopology());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 本地测试
			LocalCluster local = new LocalCluster();
			local.submitTopology("counter", config, builder.createTopology());
			try {
				Thread.sleep(6000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			local.shutdown();
		}
	}
}
