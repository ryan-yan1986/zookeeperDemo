package com.idowran.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Create_Session_Sample {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 创建一个重试策略，参数(初始sleep时间, 最大重试次数)
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		
		// 创建会话
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 5000, 3000, retryPolicy);
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
