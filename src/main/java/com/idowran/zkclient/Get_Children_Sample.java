package com.idowran.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

public class Get_Children_Sample {
	public static void main(String[] args) throws Exception {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		
		String path = "/zk-book";
		zkClient.subscribeChildChanges(path, new IZkChildListener() {
			
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(parentPath + " 's child changed, currendChilds: " + currentChilds);
			}
		});
		
		zkClient.createPersistent(path);
		Thread.sleep(1000);
		System.out.println(zkClient.getChildren(path));
		Thread.sleep(1000);
		zkClient.createPersistent(path + "/c1");
		Thread.sleep(1000);
		zkClient.delete(path + "/c1");
		Thread.sleep(1000);
		zkClient.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
	}
}
