package com.idowran.curator.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Recipes_Barrier {
	static String barrier_path = "/curator_recipes_barrier_path";
	static DistributedBarrier barrier;
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						CuratorFramework client = CuratorFrameworkFactory
								.builder()
								.connectString("127.0.0.1:2181")
								.retryPolicy(new ExponentialBackoffRetry(1000, 3))
								.build();
						client.start();
						barrier = new DistributedBarrier(client, barrier_path);
						System.out.println(Thread.currentThread().getName() + " 号 barrier设置");
						barrier.setBarrier();	// 设置
						barrier.waitOnBarrier();
						System.err.println(Thread.currentThread().getName() + " 启动");
					}catch (Exception e) {}
				}
			}).start();
		}
		Thread.sleep(2000);
		barrier.removeBarrier();
	}
}

