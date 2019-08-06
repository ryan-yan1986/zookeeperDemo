package com.idowran.curator.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Recipes_MasterSelect {
	// master选举的根节点
	static String master_path = "/curator_recipes_master_path";
	// 使用Fluent风格的API创建会话
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString("127.0.0.1:2181")
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		client.start();
		
		// Master选举
		LeaderSelector selector = new LeaderSelector(client, master_path, new LeaderSelectorListenerAdapter() {
			
			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("成为Master角色");
				Thread.sleep(3000);
				System.out.println("完成Master操作，释放Master权利");
			}
		});
		
		selector.autoRequeue();
		selector.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
