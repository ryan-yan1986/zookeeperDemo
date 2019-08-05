package com.idowran.zookeeper;

import java.util.concurrent.CountDownLatch;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
/**
 * 同步创建节点
 * @author Lingdong159
 *
 */
public class SetData_API_Sync_Usage implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	public static void main(String[] args) throws Exception {
		// 连接zookeeper服务器，并且设置继承了Watcher接口的监听类
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new SetData_API_Sync_Usage());
		connectedSemaphore.await();
		
		String path = "/zk-book";
		
		// 创建一个节点
		zk.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

		zk.getData(path, true, null);
		
		Stat stat = zk.setData(path, "456".getBytes(), -1);
		System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
		
		Stat stat2 = zk.setData(path, "456".getBytes(), stat.getVersion());
		System.out.println(stat2.getCzxid() + ", " + stat2.getMzxid() + ", " + stat2.getVersion());
		
		try {
			zk.setData(path, "456".getBytes(), stat.getVersion());
		}catch (KeeperException e) {
			// TODO: handle exception
			System.out.println("Error: " + e.code() + ", " + e.getMessage());
		}
		
		Thread.sleep(Integer.MAX_VALUE);
		
		// 最后要关闭服务连接
//		zookeeper.close();
	}
	
	@Override
	public void process(WatchedEvent event) {
		// 实现接口方法，有事件发生时，会回调该接口
		if(KeeperState.SyncConnected == event.getState()) {
			if(EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			}
		}
	}

}
