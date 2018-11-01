package com.zookeeper.server;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class AppServer extends Thread{
	private String clusterNode = "Locks";
	private String serverNode = "mylock";
	private String serverName;
	private long sleepTime;

	public void run() {
		try {
			connectZookeeper(serverName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectZookeeper(String address) throws Exception {
		ZooKeeper zk = new ZooKeeper("192.168.80.153:2181,192.168.80.154:2181,192.168.80.155:2181", 5000,
				new Watcher() {
					public void process(WatchedEvent event) {
					}
				});

		// 关键方法，创建包含自增长id名称的目录，这个方法支持了分布式锁的实现
		// 四个参数：
		// 1、目录名称 2、目录文本信息
		// 3、文件夹权限，Ids.OPEN_ACL_UNSAFE表示所有权限
		// 4、目录类型，CreateMode.EPHEMERAL_SEQUENTIAL表示创建临时目录，session断开连接则目录自动删除
		String createdPath = zk.create("/" + clusterNode + "/" + serverNode, address.getBytes("utf-8"),
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("create: " + createdPath);
		Thread.sleep(sleepTime);
	}

	public AppServer(String serverName, long sleepTime) {
		this.serverName = serverName;
		this.sleepTime = sleepTime;
	}
}
