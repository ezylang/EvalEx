
package com.udojava.evalex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

public class TestThreadSafety {
	private static final int THREAD_COUNT = 4;
	
	@Test
	public void testThreadSafety() {
		CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT + 1);
		
		Expression expression = new Expression("a+b+c");
		expression.with("a", new BigDecimal("1"));
		
		List<TestRunnable> tests = new ArrayList<TestRunnable>();
		
		for (int counter = 0; counter < THREAD_COUNT; counter++) {
			tests.add(new TestRunnable(
					expression,
					new BigDecimal(1 + (counter + 1) * 15),
					barrier,
					new BigDecimal((counter + 1) * 5),
					new BigDecimal((counter + 1) * 10)));
		}
		
		List<Thread> threads = new ArrayList<Thread>();
		
		for (TestRunnable test : tests) {
			Thread thread = new Thread(test, "Test Thread");
			threads.add(thread);
			
			thread.start();
		}
		
		try {
			barrier.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Assert.fail(e.getMessage());
		} catch (BrokenBarrierException e) {
			Assert.fail(e.getMessage());
		} catch (TimeoutException e1) {
			Assert.fail("Threads for testing failed to start correctly.");
		}
		
		for (Thread thread : threads) {
			while (thread.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (TestRunnable test : tests) {
			if (test.getException() != null) {
				Assert.fail(test.getException().getMessage());
			}
		}
	}
	
	private static final class TestRunnable implements Runnable {
		
		private volatile Exception exception = new Exception("Thread has not started or did not finish correctly.");
		
		private BigDecimal expected = null;
		
		private Expression expression = null;
		
		private Scope scope = new Scope();
		
		private CyclicBarrier barrier = null;
		
		public TestRunnable(Expression expression, BigDecimal expected, CyclicBarrier barrier, BigDecimal b, BigDecimal c) {
			super();
			
			this.expression = expression;
			this.expected = expected;
			this.barrier = barrier;
			
			scope.addVariable("b", b);
			scope.addVariable("c", c);
		}
		
		public Exception getException() {
			return exception;
		}
		
		@Override
		public void run() {
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				exception = e;
				return;
			}
			
			for (int counter = 0; counter < 100000; counter++) {
				BigDecimal result = null;
				
				try {
					result = expression.eval(scope, true);
				} catch (Exception e) {
					e.printStackTrace();
					exception = e;
					return;
				}
				
				if (result.compareTo(expected) != 0) {
					exception = new Exception("Expected <" + result.toString() + "> but was <" + expected.toString() + ">");
					return;
				}
			}
			
			exception = null;
		}
	}
}