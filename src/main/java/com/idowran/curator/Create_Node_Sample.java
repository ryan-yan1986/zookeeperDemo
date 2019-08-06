package com.idowran.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class Create_Node_Sample {
	static String path = "/zk-book/c1";
	// 使用Fluent风格的API创建会话
	static CuratorFramework client = CuratorFrameworkFactory.builder()
					.connectString("127.0.0.1:2181")
					.sessionTimeoutMs(5000)
					.retryPolicy(new ExponentialBackoffRetry(1000, 3))
					.build();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		client.start();
		
		client.create()
			.creatingParentsIfNeeded()			// 如果需要，递归创建父节点
			.withMode(CreateMode.EPHEMERAL)		// 创建临时节点
			.forPath(path, "init".getBytes());	// 附带初始化内容
	}

}
