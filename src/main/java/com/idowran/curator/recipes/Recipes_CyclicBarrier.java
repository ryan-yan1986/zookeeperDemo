package com.idowran.curator.recipes;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 用自带的CyclicBarrier实现同步
 * @author Tony
 *
 */
public class Recipes_CyclicBarrier {
	public static CyclicBarrier barrier = new CyclicBarrier(3);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(new Thread(new Runner("1 号选手")));
		executor.submit(new Thread(new Runner("2 号选手")));
		executor.submit(new Thread(new Runner("3 号选手")));
		executor.shutdown();
	}
}

class Runner implements Runnable{
	private String name;
	public Runner(String name) {
		this.name = name;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(name + "准备好了.");
		try {
			Recipes_CyclicBarrier.barrier.await();
		}catch (Exception e) {}
		System.out.println(name + "起跑！");
	}
}
