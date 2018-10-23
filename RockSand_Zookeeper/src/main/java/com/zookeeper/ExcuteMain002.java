package com.zookeeper;

import java.nio.charset.Charset;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ExcuteMain002 {
	private static String ZK_SERVER_CONNECT = "192.168.80.153:2181,192.168.80.154:2181,192.168.80.155:2181";

	private static int ZK_SERVER_CONN_TIMEOUT = 1000;

	private static final Charset CHARSET = Charset.forName("UTF-8");

	private static ZooKeeper zk;

	/**
	 * 判断
	 * 
	 * @param path
	 * @param watcher
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static boolean exists1(String path, Watcher watcher) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, watcher);
		if (stat == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 判断
	 * 
	 * @param path
	 * @param watcher
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static boolean exists2(String path) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, true);
		if (stat == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 创建
	 * 
	 * @param path
	 * @param value
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void create(String path, String value) throws KeeperException, InterruptedException {
		zk.create(path, value.getBytes(CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	/**
	 * 写
	 * 
	 * @param path
	 * @param value
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void write(String path, String value) throws KeeperException, InterruptedException {
		zk.setData(path, value.getBytes(CHARSET), -1);
	}

	/**
	 * 读
	 * 
	 * @param path
	 * @param watcher
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static String read1(String path, Watcher watcher) throws KeeperException, InterruptedException {
		byte[] data = zk.getData(path, watcher, null);
		return new String(data, CHARSET);

	}

	public static String read2(String path) throws KeeperException, InterruptedException {
		byte[] data = zk.getData(path, true, null);
		return new String(data, CHARSET);

	}

	public static void main(String[] args) throws Exception {
		/*
		 * 该watcher,在连接成功后执行一次
		 */
		zk = new ZooKeeper(ZK_SERVER_CONNECT, ZK_SERVER_CONN_TIMEOUT, new Watcher() {
			// 监控所有被触发的事件
			public void process(WatchedEvent event) {
				System.out.println("ExcuteMain002已经触发了" + event.getType() + "事件！");
			}
		});
		/*
		 * exists, 可以新创建watch或者使用原Watcher
		 */
//		Boolean isExist = exists1("/testRootPath", new Watcher() {
//			// 监控所有被触发的事件
//			public void process(WatchedEvent event) {
//				System.out.println("ExistWatcher:[" + event.getType() + "]事件！");
//			}
//		});
		Boolean isExist = exists2("/testRootPath");
		System.out.println("==Exist==>" + isExist);
		System.out.println("-----------------------------");
		/*
		 * write: 触发watcher
		 */
		if (!isExist) {
			create("/testRootPath","0000000");
		} else {
			write("/testRootPath","0000000");
		}
		System.out.println("-----------------------------");
		/*
		 * read1:不触发watcher
		 */
		String val = read1("/testRootPath", new Watcher() {
			// 监控所有被触发的事件
			public void process(WatchedEvent event) {
				System.out.println("ReadWatcher:[" + event.getType() + "]事件！");
			}
		});
		System.out.println("-----------------------------");
		/*
		 * write
		 */
		write("/testRootPath", "1111111");
		System.out.println("-----------------------------");
		/*
		 * read2
		 */
		val = read2("/testRootPath");
		System.out.println("====>" + val);
		/*
		 * delete:触发ExcuteMain002Watcher
		 */
		zk.delete("/testRootPath", -1);
		System.out.println("-----------------------------");
		zk.close();
	}
}
