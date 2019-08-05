package com.idowran.zookeeper;

import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

public class AuthSample_Delete {
	final static String PATH = "/zk-book-auth_test";
	final static String PATH2 = "/zk-book-auth_test/Child";
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 5000, null);
		
		// digest模式
		zk.addAuthInfo("digest", "foo:true".getBytes());
		zk.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
		zk.create(PATH2, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
		
		try {
			// 创建另一个会话，但是不包含权限信息，保存没有权限
			ZooKeeper zk2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
			zk2.delete(PATH2, -1);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("删除节点失败: " + e.getMessage());
		}
		
		ZooKeeper zk3 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		// 使用正确的权限，所以删除会成功
		zk3.addAuthInfo("digest", "foo:true".getBytes());
		zk3.delete(PATH2, -1);
		System.out.println("成功删除节点: " + PATH2);
		
		// 当客户端对一个数据节点添加权限信息后，对于删除操作而已，其作用范围是其子节点
		// 所以当我们对一个数据节点添加权限后，依然可以自由的删除这个节点，
		// 但是对于这个节点的子节点，就必须使用相应地权限信息才能够删除它。
		ZooKeeper zk4 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zk4.delete(PATH, -1);
		System.out.println("成功删除节点: " + PATH);
	}
	
	
}
