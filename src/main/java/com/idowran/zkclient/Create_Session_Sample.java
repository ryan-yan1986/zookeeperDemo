package com.idowran.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class Create_Session_Sample {
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		System.out.println("ZooKeeper session established.");
	}
}
