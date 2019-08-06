package com.idowran.curator.test;

import java.io.File;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

public class TestingServer_Sample {

	static String path = "/zookeeper";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 打开一个测试服务器
		TestingServer server = new TestingServer(2181, new File("D:\\Middle\\zookeeper_data"));
		
		// 创建一个客户端，连接测试服务器
		CuratorFramework client = CuratorFrameworkFactory
				.builder()
				.connectString(server.getConnectString())
				.sessionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3))
				.build();
		
		client.start();
		System.out.println(client.getChildren().forPath(path));
		server.close();
	}

}
