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
import org.apache.hadoop.hbase.filter.Filter;
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
		scanALL();
		scanFilter();
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
		// 最多保存5个版本,如果只需要最新的,版本设置为1即可
		hc.setMaxVersions(5);
		hc.setBlockCacheEnabled(true);
		hc.setBlocksize(1800000);
		ht.addFamily(hc);
		admin.createTable(ht);
		admin.close();
	}

	/**
	 * RowKey
	 * 
	 * @param pre
	 * @return
	 */
	public static String getRowKey() {
		int val = ra.nextInt(99999999);
		return (val % 2 == 1 ? "134" : "138") + ra.nextInt(99999999);
	}

	/**
	 * RowKey
	 * 
	 * @param pre
	 * @return
	 */
	public static String getTime() {
		int val = ra.nextInt(99999999);
		return (val % 2 == 1 ? "20180802 " : "20180801 ") + ra.nextInt(23) + ":" + ra.nextInt(59) + ":"
				+ ra.nextInt(59);
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
		me.addColumn("cf1".getBytes(), "time".getBytes(), getTime().getBytes());
		me.addColumn("cf1".getBytes(), "type".getBytes(), String.valueOf(ra.nextInt(2)).getBytes());
		list.add(me);
		for (int i = 0; i < 500; i++) {
			Put put = new Put(getRowKey().getBytes());
			put.addColumn("cf1".getBytes(), "address".getBytes(), "北京".getBytes());
			put.addColumn("cf1".getBytes(), "time".getBytes(), getTime().getBytes());
			put.addColumn("cf1".getBytes(), "type".getBytes(), String.valueOf(ra.nextInt(2)).getBytes());
			list.add(put);
		}
		table.put(list);
		table.close();
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
		for (Cell cell : result.rawCells()) {// 获取row的各个列
			System.out.println("-列族->" + Bytes.toString(CellUtil.cloneFamily(cell)));
			System.out.println("-列名->" + Bytes.toString(CellUtil.cloneQualifier(cell)));
			System.out.println("-列值->" + Bytes.toString(CellUtil.cloneValue(cell)));
		}
		table.close();
	}

	/**
	 * 获取所有
	 * 
	 * @throws IOException
	 */
	public static void scanALL() throws IOException {
		Table table = conn.getTable(test);
		Scan scan = new Scan();
		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> it = scanner.iterator();
		/*
		 * 缓存 setCacheBlocks,设置缓存到Region的Cache中 setCaching,设置每次获取的列数(按照批次)
		 */
		// scan.setCacheBlocks(cacheBlocks);
		// scan.setCaching(caching);
		System.out.println("<-----------------ScanALl------------->");
		while (it.hasNext()) {
			Result next = it.next();
			// byte[] value = next.getValue("cf1".getBytes(), "type".getBytes());
			System.out.print("ROWKEY[" + Bytes.toString(next.getRow()) + "]");
			for (Cell cell : next.rawCells()) {// 获取row的各个列
				System.out.print("\t" + Bytes.toString(CellUtil.cloneFamily(cell)));
				System.out.print("." + Bytes.toString(CellUtil.cloneQualifier(cell)));
				System.out.print(": " + Bytes.toString(CellUtil.cloneValue(cell)));
			}
			System.out.println();
		}
		table.close();
	}

	/**
	 * scanFilter
	 * 
	 * Filter对列进行过滤,但是Hbase对Rowkey查询性能高,Filter性能有的较差
	 * 
	 * @throws IOException
	 */
	public static void scanFilter() throws IOException {
		Table table = conn.getTable(test);
		Scan scan = new Scan();
		// 筛选匹配行键的前缀成功的行
		Filter filter = new PrefixFilter(Bytes.toBytes("134"));
		scan.setFilter(filter);
		ResultScanner scanner = table.getScanner(scan);
		Iterator<Result> it = scanner.iterator();
		System.out.println("<-----------------scanFilter------------->");
		while (it.hasNext()) {
			Result next = it.next();
			// byte[] value = next.getValue("cf1".getBytes(), "type".getBytes());
			System.out.print("ROWKEY[" + Bytes.toString(next.getRow()) + "]");
			for (Cell cell : next.rawCells()) {// 获取row的各个列
				System.out.print("\t" + Bytes.toString(CellUtil.cloneFamily(cell)));
				System.out.print("." + Bytes.toString(CellUtil.cloneQualifier(cell)));
				System.out.print(": " + Bytes.toString(CellUtil.cloneValue(cell)));
			}
			System.out.println();
		}
		table.close();
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
		// FilterList可以实现复杂的过滤逻辑
		FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		PrefixFilter pf = new PrefixFilter("138".getBytes());
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
