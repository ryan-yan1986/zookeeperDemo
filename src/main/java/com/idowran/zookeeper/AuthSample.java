package com.idowran.zookeeper;

import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

public class AuthSample {
	final static String PATH = "/zk-book-auth_test";
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, null);
		
		// digest模式
		zk.addAuthInfo("digest", "foo:true".getBytes());
		zk.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	
}
