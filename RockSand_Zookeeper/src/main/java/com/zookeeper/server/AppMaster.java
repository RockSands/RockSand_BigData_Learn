package com.zookeeper.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class AppMaster implements Watcher{
	private String clusterNode = "Locks";
	private ZooKeeper zk;
	private volatile List<String> serverList;

	public void connectZookeeper() throws Exception {
		// 注册全局默认watcher
		zk = new ZooKeeper("192.168.80.153:2181,192.168.80.154:2181,192.168.80.155:2181", 5000, this);
		updateServerList();
	}

	private void updateServerList() throws Exception {
		List<String> newServerList = new ArrayList<String>();

		// watcher注册后，只能监听事件一次，参数true表示继续使用默认watcher监听事件
		List<String> subList = zk.getChildren("/" + clusterNode, true);
		for (String subNode : subList) {
			// 获取节点数据
			byte[] data = zk.getData("/" + clusterNode + "/" + subNode, false, null);
			newServerList.add(new String(data, "utf-8"));
		}

		serverList = newServerList;
		System.out.println("server list updated: " + serverList);
	}

	public static void main(String[] args) throws Exception {
		AppMaster ac = new AppMaster();
		ac.connectZookeeper();
		Thread.sleep(Long.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			zk.exists("/" + clusterNode, true);
		} catch (KeeperException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (event.getType() == EventType.NodeChildrenChanged && ("/" + clusterNode).equals(event.getPath())) {
			try {
				updateServerList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
