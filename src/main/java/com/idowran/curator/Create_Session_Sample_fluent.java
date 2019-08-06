package com.idowran.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Create_Session_Sample_fluent {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 创建一个重试策略，参数(初始sleep时间, 最大重试次数)
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		// 使用Fluent风格的API创建会话
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000)
				.retryPolicy(retryPolicy)
				.namespace("base")	// 含隔离命名空间的会话
				.build();
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
