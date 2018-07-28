package hbase.com.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseMain {

	public static Connection conn = null;
	public static TableName test = TableName.valueOf("t_phone");
	public static Random ra = new Random();

	public static void main(String[] args) throws IOException {
		connect();
		create();
		insert();
		get();
		// find();
		// find1();
		// search();
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
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.rootdir", "hdfs://192.168.80.152:8020/hbase");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
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
	 * 建表
	 * 
	 * @throws IOException
	 */
	public static void create() throws IOException {
		Admin admin = conn.getAdmin();
		if (admin.tableExists(test)) {
			// 删除表需要先disable表
			admin.disableTable(test);
			admin.deleteTable(test);
		}
		// 表名
		HTableDescriptor ht = new HTableDescriptor(test);
		// 列族
		HColumnDescriptor hc = new HColumnDescriptor("cf1".getBytes());
		// 最多保存5个版本
		hc.setMaxVersions(5);
		hc.setBlockCacheEnabled(true);
		hc.setBlocksize(1800000);
		ht.addFamily(hc);
		admin.createTable(ht);
	}

	/**
	 * RowKey
	 * 
	 * @param pre
	 * @return
	 */
	public static String getRowKey(String pre) {
		return pre + ra.nextInt(99999999) + "_2016" + ra.nextInt(12) + ra.nextInt(30) + ra.nextInt(24) + ra.nextInt(60)
				+ ra.nextInt(60);
	}

	/**
	 * 插入
	 * 
	 * @throws IOException
	 */
	public static void insert() throws IOException {
		Table table = conn.getTable(test);
		List<Put> list = new ArrayList<Put>();
		Put me = new Put("13478229868".getBytes());
		me.addColumn("cf1".getBytes(), "address".getBytes(), "沈阳".getBytes());
		me.addColumn("cf1".getBytes(), "type".getBytes(), String.valueOf(ra.nextInt(2)).getBytes());
		list.add(me);
		for (int i = 0; i < 1000; i++) {
			Put put = new Put(getRowKey("138").getBytes());
			put.addColumn("cf1".getBytes(), "address".getBytes(), "北京".getBytes());
			put.addColumn("cf1".getBytes(), "type".getBytes(), String.valueOf(ra.nextInt(2)).getBytes());
			list.add(put);
		}
		table.put(list);
	}

	/**
	 * Get
	 * 
	 * @throws IOException
	 */
	public static void get() throws IOException {
		Table table = conn.getTable(test);
		Get get = new Get(Bytes.toBytes("13478229868"));
		Result result = table.get(get);
		for (Cell cell : result.rawCells()) {//获取row的各个列
			System.out.println("-列族->" + Bytes.toString(CellUtil.cloneFamily(cell)));
			System.out.println("-列名->" + Bytes.toString(CellUtil.cloneQualifier(cell)));
			System.out.println("-列值->" + Bytes.toString(CellUtil.cloneValue(cell)));
		}
	}

	/**
	 * 查询
	 * 
	 * @throws IOException
	 */
	public static void find() throws IOException {
		Table table = conn.getTable(test);
		Scan scan = new Scan("13899459154_2016106133221".getBytes(), "13899950496_20167720852".getBytes());
		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> it = scanner.iterator();
		while (it.hasNext()) {
			Result next = it.next();
			byte[] value = next.getValue("cf1".getBytes(), "type".getBytes());
			System.out.println("fine--->" + new String(value, "GBK"));
		}
	}

	/**
	 * 某个手机号，某段时间，主叫电话 查询首字母1389，type=1
	 * 
	 * @throws IOException
	 */
	public static void find1() throws IOException {
		Table table = conn.getTable(test);
		Scan scan = new Scan();
		FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		PrefixFilter pf = new PrefixFilter("1389".getBytes());
		SingleColumnValueFilter sf = new SingleColumnValueFilter("cf1".getBytes(), "type".getBytes(),
				CompareFilter.CompareOp.EQUAL, "1".getBytes());
		// 过滤器的顺序影响效率
		fl.addFilter(pf);
		fl.addFilter(sf);
		scan.setFilter(fl);
		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> it = scanner.iterator();
		while (it.hasNext()) {
			Result next = it.next();
			byte[] value = next.getValue("cf1".getBytes(), "address".getBytes());
			System.out.println("find1--->" + new String(value, "utf8"));
		}
	}

	/**
	 * 搜索
	 * 
	 * @throws IOException
	 */
	public static void search() throws IOException {
		Table table = conn.getTable(test);
		Get get = new Get("RK123".getBytes());
		// get.addColumn("cf1".getBytes(),"name".getBytes());
		Result result = table.get(get);
		Cell cell = result.getColumnLatestCell("cf1".getBytes(), "age".getBytes());
		System.out.printf(new String(cell.getValueArray()));
	}
}