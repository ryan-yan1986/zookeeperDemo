package com.idowran.zookeeper;

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
public class GetData_API_ASync_Usage implements Watcher {
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	
	public static void main(String[] args) throws Exception {
		// 连接zookeeper服务器，并且设置继承了Watcher接口的监听类
		zk = new ZooKeeper("127.0.0.1:2181", 5000, new GetData_API_ASync_Usage());
		connectedSemaphore.await();
		
		String path = "/zk-book";
		zk.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
		zk.getData(path, true, new IDataCallback(), null);
		
		// 更新数据
		zk.setData(path, "123".getBytes(), -1);
		
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
			}else if(event.getType() == EventType.NodeDataChanged) {
				try {
					zk.getData(event.getPath(), true, new IDataCallback(), null);
				} catch (Exception e) {}
			}
		}
	}

}

class IDataCallback implements AsyncCallback.DataCallback{

	@Override
	public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
		// TODO Auto-generated method stub
		System.out.println(rc + ", " + path + ", " + new String(data));
		System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
	}
	
}


