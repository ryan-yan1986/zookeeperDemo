package com.idowran.curator.recipes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Recipes_NoLock {
	
	public static void main(String[] args) throws Exception {
		final CountDownLatch down = new CountDownLatch(1);
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						down.await();
					}catch (Exception e) {
						// TODO: handle exception
					}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo = sdf.format(new Date());
					System.err.println("生成的订单号是：" + orderNo);
				}
			}).start();
		}
		down.countDown();
	}

}
