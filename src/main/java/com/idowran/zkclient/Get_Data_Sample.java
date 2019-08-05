package com.idowran.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class Get_Data_Sample {
	public static void main(String[] args) throws Exception {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		
		String path = "/zk-book";
		zkClient.createEphemeral(path, "123");

		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Node " + dataPath + " deleted.");
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Node " + dataPath + " changed, new data: " + data);
			}
		});
		
		System.out.println(zkClient.readData(path).toString());
		zkClient.writeData(path, "456");
		Thread.sleep(1000);
		zkClient.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
	}
}
