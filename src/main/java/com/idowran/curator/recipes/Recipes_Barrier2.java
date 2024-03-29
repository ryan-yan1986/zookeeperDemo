package com.idowran.curator.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Recipes_Barrier2 {
	static String barrier_path = "/curator_recipes_barrier_path";
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
						DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrier_path, 5);
						Thread.sleep(Math.round(Math.random() * 3000));
						
						System.out.println(Thread.currentThread().getName() + " 号 进入barrier");
						barrier.enter();	// 进入
						System.out.println(Thread.currentThread().getName() + " 启动...");
						Thread.sleep(Math.round(Math.random() * 3000));
						barrier.leave();	// 离开
						System.err.println(Thread.currentThread().getName() + " 退出...");
					}catch (Exception e) {}
				}
			}).start();
		}
	}
}

