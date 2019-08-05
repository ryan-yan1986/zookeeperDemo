package com.idowran.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
/**
 * 同步创建节点
 * @author Lingdong159
 *
 */
public class Exist_API_Sync_Usage implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	public static void main(String[] args) throws Exception {
		// 连接zookeeper服务器，并且设置继承了Watcher接口的监听类
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new Exist_API_Sync_Usage());
		connectedSemaphore.await();
		
		String path = "/zk-book";
		
		zk.exists(path, true);
		
		// 创建一个节点
		zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		zk.setData(path, "123".getBytes(), -1);
		
		zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zk.delete(path + "/c1", -1);
		zk.delete(path, -1);
		
		Thread.sleep(Integer.MAX_VALUE);
		
		// 最后要关闭服务连接
//		zookeeper.close();
	}
	
	@Override
	public void process(WatchedEvent event) {
		// 实现接口方法，有事件发生时，会回调该接口
		try {
			if(KeeperState.SyncConnected == event.getState()) {
				if(EventType.None == event.getType() && null == event.getPath()) {
					connectedSemaphore.countDown();
				}else if(EventType.NodeCreated == event.getType()) {
					System.out.println("Node("+ event.getPath() +") Created");
					zk.exists(event.getPath(), true);
				}else if(EventType.NodeDeleted == event.getType()) {
					System.out.println("Node("+ event.getPath() +") Deleted");
					zk.exists(event.getPath(), true);
				}else if(EventType.NodeDataChanged == event.getType()) {
					System.out.println("Node("+ event.getPath() +") DataChanged");
					zk.exists(event.getPath(), true);
				}
			}
		}catch (Exception e) {}
	}
}
