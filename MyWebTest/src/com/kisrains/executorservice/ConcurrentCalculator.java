package com.kisrains.executorservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
//������������ĺͣ���ͬ�������
public class ConcurrentCalculator {


	private ExecutorService exec;
	//����ط��������ǡ�һ����Ը����������ִ�С��������ǿ��ƣ�ȡ���ڲ���ϵͳ�ġ�̬�ȡ�
	private int cpuCoreNumber;
	private List<Future<Long>> tasks = new ArrayList<Future<Long>>();
	
	class SumCalculator implements Callable<Long> {
		private int[] numbers;
		private int start;
		private int end;


		public SumCalculator(final int[] numbers, int start, int end) {
			this.numbers = numbers;
			this.start = start;
			this.end = end;
		}


		public Long call() throws Exception {
			Long sum = 0L;
			for (int i = start; i < end; i++) {
				sum += numbers[i];
			}
			return sum;
		}
	}


	public ConcurrentCalculator() {
		cpuCoreNumber = Runtime.getRuntime().availableProcessors();
		exec = Executors.newFixedThreadPool(cpuCoreNumber);
	}


	public Long sum(final int[] numbers) {
		// ����CPU���ĸ���������񣬴���FutureTask���ύ��Executor
		for (int i = 0; i < cpuCoreNumber; i++) {
			int increment = numbers.length / cpuCoreNumber + 1;
			int start = increment * i;
			int end = increment * i + increment;
			if (end > numbers.length)
				end = numbers.length;
			SumCalculator subCalc = new SumCalculator(numbers, start, end);
			FutureTask<Long> task = new FutureTask<Long>(subCalc);
			tasks.add(task);
			if (!exec.isShutdown()) {
				exec.submit(task);
			}
		}
		return getResult();
	}


	/**
	 * ����ÿ��ֻ���񣬻�ò��ֺͣ���ӷ���
	 */
	public Long getResult() {
		Long result = 0l;
		for (Future<Long> task : tasks) {
			try {
				// �������δ���������
				Long subSum = task.get();
				result += subSum;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public void close() {
		exec.shutdown();
	}
}