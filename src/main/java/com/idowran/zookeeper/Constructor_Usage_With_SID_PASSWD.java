package com.idowran.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
/**
 * 复用sessionId和session passwd来创建一个zookeeper对象
 * @author Lingdong159
 *
 */
public class Constructor_Usage_With_SID_PASSWD implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		// 连接zookeeper服务器，并且设置继承了Watcher接口的监听类
		ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Constructor_Usage_With_SID_PASSWD());
		connectedSemaphore.await();
		
		// 获取已连接的sessionId和passwd
		long sessionId = zookeeper.getSessionId();
		byte[] passwd = zookeeper.getSessionPasswd();
		
		zookeeper = new ZooKeeper(
				"127.0.0.1:2181", 5000, 
				new Constructor_Usage_With_SID_PASSWD(), 
				sessionId, passwd);
		// 不复用
		zookeeper = new ZooKeeper(
				"127.0.0.1:2181", 5000, 
				new Constructor_Usage_With_SID_PASSWD(), 
				1l, "test".getBytes());
		
		// 复用sessionId和session passwd
		zookeeper = new ZooKeeper(
				"127.0.0.1:2181", 5000, 
				new Constructor_Usage_With_SID_PASSWD(), 
				sessionId, passwd);
		Thread.sleep(Integer.MAX_VALUE);
		System.out.println("zookeeper session established.");
		
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
