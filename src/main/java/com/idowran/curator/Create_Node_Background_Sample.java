package com.idowran.curator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class Create_Node_Background_Sample {
	static String path = "/zk-book/c1";
	// 使用Fluent风格的API创建会话
	static CuratorFramework client = CuratorFrameworkFactory.builder()
					.connectString("127.0.0.1:2181")
					.sessionTimeoutMs(5000)
					.retryPolicy(new ExponentialBackoffRetry(1000, 3))
					.build();
	// 倒计时器
	static CountDownLatch semaphore = new CountDownLatch(2);
	// 线程池
	static ExecutorService tp = Executors.newFixedThreadPool(2);
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		client.start();
		System.out.println("Main thread: " + Thread.currentThread().getName());
		// 此处传入了自定义的Executor
		client.create()
			.creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
			.inBackground(new BackgroundCallback() {
				
				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("event[code: " + event.getResultCode() + ", type: " + event.getType() + "]");
					System.out.println("Thread of processResult: " + Thread.currentThread().getName());
					semaphore.countDown();
				}
			}, tp).forPath(path, "init".getBytes());
		
		// 此处没有传自定义的Executor
		client.create()
			.creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
			.inBackground(new BackgroundCallback() {
				
				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("event[code: " + event.getResultCode() + ", type: " + event.getType() + "]");
					System.out.println("Thread of processResult: " + Thread.currentThread().getName());
					semaphore.countDown();
				}
			}).forPath(path, "init".getBytes());
		
		semaphore.await();
		tp.shutdown();
	}

}
