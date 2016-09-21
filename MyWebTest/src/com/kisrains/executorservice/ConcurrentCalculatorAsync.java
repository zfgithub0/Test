package com.kisrains.executorservice;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//������������ĺͣ����첽�����
public class ConcurrentCalculatorAsync {


	private ExecutorService exec;
	private CompletionService<Long> completionService;
	//����ط��������ǡ�һ����Ը����������ִ�С��������ǿ��ƣ�ȡ���ڲ���ϵͳ�ġ�̬�ȡ�
	private int cpuCoreNumber;


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
			Long sum = 0l;
			for (int i = start; i < end; i++) {
				sum += numbers[i];
			}
			return sum;
		}
	}


	public ConcurrentCalculatorAsync() {
		cpuCoreNumber = Runtime.getRuntime().availableProcessors();
		exec = Executors.newFixedThreadPool(cpuCoreNumber);
		completionService = new ExecutorCompletionService<Long>(exec);
	}


	public Long sum(final int[] numbers) {
		// ����CPU���ĸ���������񣬴���FutureTask���ύ��Executor
		for (int i = 0; i < cpuCoreNumber; i++) {
			int increment = numbers.length / cpuCoreNumber + 1;
			int start = increment * i;
			int end = increment * i + increment;
			if (end > numbers.length){
				end = numbers.length;
			}
			SumCalculator subCalc = new SumCalculator(numbers, start, end);
			if (!exec.isShutdown()) {
				completionService.submit(subCalc);
			}
			
		}
		return getResult();
	}


	/**
	 * ����ÿ��ֻ���񣬻�ò��ֺͣ���ӷ���
	 */
	public Long getResult() {
		Long result = 0l;
		for (int i = 0; i < cpuCoreNumber; i++) {			
			try {
				Long subSum = completionService.take().get();
				result += subSum;			
				System.out.println("subSum="+subSum+",result="+result);
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
