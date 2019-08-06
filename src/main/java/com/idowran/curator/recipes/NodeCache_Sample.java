package com.idowran.curator.recipes;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class NodeCache_Sample {
	
	static String path = "/zk-book/nodecache";
	// 使用Fluent风格的API创建会话
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString("127.0.0.1:2181")
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		client.start();
		client.create()
			.creatingParentsIfNeeded()
			.withMode(CreateMode.EPHEMERAL)
			.forPath(path, "init".getBytes());
		
		// 用于监听指定zk数据节点本身的变化，第三个参数，是否进行数据压缩
		final NodeCache cache = new NodeCache(client, path, false);
		// 参数默认为false，表示第一次启动的时候，不会立刻从zk上读取对应节点的数据内容
		cache.start(true);
		cache.getListenable().addListener(new NodeCacheListener() {
			
			@Override
			public void nodeChanged() throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Node data update, new data: " + new String(cache.getCurrentData().getData()));
			}
		});
		client.setData().forPath(path, "u".getBytes());
		Thread.sleep(1000);
		client.delete().deletingChildrenIfNeeded().forPath(path);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
