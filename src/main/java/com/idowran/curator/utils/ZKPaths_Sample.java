package com.idowran.curator.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;

public class ZKPaths_Sample {
	
	static String path = "/curator_zkpath_sample";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString("127.0.0.1:2181")
			.sessionTimeoutMs(5000)
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		client.start();
		
		System.out.println(ZKPaths.fixForNamespace("sub", path));
		System.out.println(ZKPaths.makePath(path, "sub"));
		System.out.println(ZKPaths.getNodeFromPath(path + "/sub1"));
		
		PathAndNode pn = ZKPaths.getPathAndNode(path + "/sub1");
		System.out.println(pn.getPath());
		System.out.println(pn.getNode());

		ZooKeeper zk = client.getZookeeperClient().getZooKeeper();
		String dir1 = path + "/child1";
		String dir2 = path + "/child2";
		ZKPaths.mkdirs(zk, dir1);
		ZKPaths.mkdirs(zk, dir2);
		System.out.println(ZKPaths.getSortedChildren(zk, path));
		
		ZKPaths.deleteChildren(zk, path, true);
	}

}
