package com.idowran.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * 同步创建节点
 * @author Lingdong159
 *
 */
public class GetChildren_API_ASync_Usage implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	
	public static void main(String[] args) throws Exception {
		// 连接zookeeper服务器，并且设置继承了Watcher接口的监听类
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new GetChildren_API_ASync_Usage());
		connectedSemaphore.await();
		
		String path = "/zk-book";
		zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		// 异步获取子节点
		zk.getChildren(path, true, new IChildren2Callback(), null);
		
		zk.create(path + "/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		Thread.sleep(Integer.MAX_VALUE);
		// 最后要关闭服务连接
//		zookeeper.close();
	}
	
	@Override
	public void process(WatchedEvent event) {
		// 实现接口方法，有事件发生时，会回调该接口
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			}else if(event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("ReGet Child: " + zk.getChildren(event.getPath(), true));
				} catch (Exception e) {}
			}
		}
	}

}

class IChildren2Callback implements AsyncCallback.Children2Callback{

	@Override
	public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
		// TODO Auto-generated method stub
		System.out.println("Get Children znode result: [resoponse code: "+ rc +", "
				+ "param path: "+ path +", "
				+ "ctx: "+ ctx +", "
				+ "children list: "+ children +", "
				+ "stat: "+ stat);
	}
	
}
