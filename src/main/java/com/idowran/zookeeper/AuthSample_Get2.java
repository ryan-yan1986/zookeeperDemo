package com.idowran.zookeeper;

import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

public class AuthSample_Get2 {
	final static String PATH = "/zk-book-auth_test";
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, null);
		
		// digest模式
		zk.addAuthInfo("digest", "foo:true".getBytes());
		zk.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
		
		// 创建另一个会话，但是不包含权限信息
		ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		// 使用错误的权限信息
		zk2.addAuthInfo("digest", "foo:false".getBytes());
		// 依旧会报错 KeeperErrorCode = NoAuth for /zk-book-auth_test
		zk2.getData(PATH, false, null);
	}
	
	
}
