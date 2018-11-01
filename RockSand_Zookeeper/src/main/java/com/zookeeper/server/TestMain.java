package com.zookeeper.server;

public class TestMain {

	public static void main(String[] args) {
		AppServer server1 = new AppServer("Server1", 5000);
		server1.start();
		AppServer server2 = new AppServer("Server2", 10000);
		server2.start();
	}

}
