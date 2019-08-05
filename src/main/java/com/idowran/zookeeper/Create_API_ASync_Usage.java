package com.idowran.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
/**
 * 同步创建节点
 * @author Lingdong159
 *
 */
public class Create_API_ASync_Usage implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		// 连接zookeeper服务器，并且设置继承了Watcher接口的监听类
		ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Create_API_ASync_Usage());
		connectedSemaphore.await();
		
		// 创建一个节点
		zookeeper.create(
				"/zk-test-ephemeral-", 
				"".getBytes(), 
				Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL, new IStringCallback(), "I am context.");
		// 再次创建同一个节点，会创建失败
		zookeeper.create(
				"/zk-test-ephemeral-", 
				"".getBytes(), 
				Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL, new IStringCallback(), "I am context.");
		
		// 创建一个顺序节点，rc:-110 表示指定节点已存在
		zookeeper.create(
				"/zk-test-ephemeral-", 
				"".getBytes(), 
				Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallback(), "I am context.");
		
		Thread.sleep(Integer.MAX_VALUE);
		// 最后要关闭服务连接
//		zookeeper.close();
	}
	
	@Override
	public void process(WatchedEvent event) {
		// 实现接口方法，有事件发生时，会回调该接口
		System.out.println("Receive watched event: " + event);
		if (KeeperState.SyncConnected == event.getState()) {
			// 如果发生异步连接事件。。。。。
			connectedSemaphore.countDown();
		}
	}

}

class IStringCallback implements AsyncCallback.StringCallback{

	@Override
	public void processResult(int rc, String path, Object ctx, String name) {
		// TODO Auto-generated method stub
		System.out.println("Create path result: [" + rc + ", "+ path +", "+ ctx +", real path name: " + name);
	}
	
}
