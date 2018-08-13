package hbase.com.sample;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

public class HbaseMapperReduce {
	public static Connection conn = null;
	public static Configuration conf = null;
	public static TableName tableName = TableName.valueOf("t_phone");
	public static String targetTable = "t_phone_count";

	public static void main(String[] args) throws IOException {
		connect();
		close();
	}

	/**
	 * 连接 注意： 1.需要修改本机的hosts，将HbaseSlaves对应的Host添加 2.Hbase中修改/etc/hosts,
	 * 将local和127.0.0.1注释掉 ----目前不需要
	 * 
	 * @throws IOException
	 */
	public static void connect() throws IOException {
		// 具体参数查看HConstants
		System.getProperties().setProperty("HADOOP_USER_NAME", "hadoop");
		System.getProperties().setProperty("HADOOP_HOME", "E:\\Java\\Hadoop\\hadoop-2.7.6");
		conf = HBaseConfiguration.create();
		// rootdir可以使用IP,但是一旦IP所属机器失效,则无法访问,这里应该使用hadoop定义的nameSpace
		conf.set("hbase.rootdir", "hdfs://192.168.80.152:8020/hbase");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		// 核心HBase是zookeeper保存regionServer以及region信息,所以只需要zookeeper就能访问到信息
		conf.set("hbase.zookeeper.quorum", "hadoop-3,hadoop-4,hadoop-5");
		conf.setInt("hbase.rpc.timeout", 20000);
		conf.setInt("hbase.client.operation.timeout", 30000);
		conf.setInt("hbase.client.scanner.timeout.period", 20000);
		conn = ConnectionFactory.createConnection(conf);
	}

	/**
	 * 关闭
	 * 
	 * @throws IOException
	 */
	public static void close() throws IOException {
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * 使用MapperReduce读取Hbase信息,使用Mapper进行操作
	 * 
	 * @throws Exception
	 */
	public static void mapperReduceReader() throws Exception {
		Job job = Job.getInstance(conf, "readerTest");
		job.setJarByClass(HbaseMapperReduce.class);
		/*
		 * 查询
		 */
		Scan scan = new Scan();
		// 设定Hbase每次抓取记录数,考虑MapperReduce效率不能默认为1
		scan.setCaching(500);
		// MapperReduce无法使用缓存
		scan.setCacheBlocks(false);
		// Mapper编写
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		// 表名,Mapper,任务
		TableMapReduceUtil.initTableMapperJob(tableName, // 读取的Hbase表名
				scan, // 查询条件
				MyMapper.class, // 自定义Mapper
				null, // mapper output key
				null, // mapper output value
				job);
		TableMapReduceUtil.initTableReducerJob(targetTable, // output table
				MyTableReducer.class, // reducer class
				job);
		job.setNumReduceTasks(1); // at least one, adjust as required
		job.setNumReduceTasks(0);
		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}

	public static class MyMapper extends TableMapper<Text, Text> {
		public void map(ImmutableBytesWritable row, Result result, Context context)
				throws InterruptedException, IOException {
			String rowKey = Bytes.toString(row.get());// 为rowKey
			Cell timeCell = result.getColumnLatestCell("cf1".getBytes(), "time".getBytes());
			// 获取时间yyyyMMdd
			String time = Bytes.toString(CellUtil.cloneValue(timeCell)).substring(0, 8);
			context.write(new Text(time), new Text(rowKey));
		}
	}

	public static class MyTableReducer extends TableReducer<Text, Text, ImmutableBytesWritable> {
		public static final byte[] CF = "cf".getBytes();
		public static final byte[] COUNT = "count".getBytes();
		public static final byte[] PHONE = "phone".getBytes();

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			int i = 0;
			StringBuffer phones = new StringBuffer();
			for (Text val : values) {
				i += 1;
				phones.append(val.toString()).append(",");
			}
			Put put = new Put(Bytes.toBytes(key.toString()));
			put.addColumn(CF, COUNT, Bytes.toBytes(i));
			put.addColumn(CF, PHONE, Bytes.toBytes(phones.toString()));
			context.write(null, put);
		}
	}
}
