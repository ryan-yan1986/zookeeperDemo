package com.idowran.curator.test;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

public class TestingCluster_Sample {

	static String path = "/zookeeper";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 创建一个测试集群
		TestingCluster cluster = new TestingCluster(3);
		cluster.start();
		Thread.sleep(2000);
		
		TestingZooKeeperServer leader = null;
		for (TestingZooKeeperServer zs : cluster.getServers()) {
			System.out.print(zs.getInstanceSpec().getServerId() + "-");
			System.out.print(zs.getQuorumPeer().getServerState() + "-");
			System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
			
			if (zs.getQuorumPeer().getServerState().equals("leading")) {
				leader = zs;
			}
		}
		leader.kill();
		System.out.println("--After leader kill:");
		for (TestingZooKeeperServer zs: cluster.getServers()) {
			System.out.print(zs.getInstanceSpec().getServerId() + "-");
			System.out.print(zs.getQuorumPeer().getServerState() + "-");
			System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
		}
		cluster.stop();
	}

}
