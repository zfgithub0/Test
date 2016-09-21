package com.kisrains.executorservice;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//并发计算数组的和，“异步”求和
public class ConcurrentCalculatorAsync {


	private ExecutorService exec;
	private CompletionService<Long> completionService;
	//这个地方，纯粹是“一厢情愿”，“并行执行”不受咱们控制，取决于操作系统的“态度”
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
		// 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
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
	 * 迭代每个只任务，获得部分和，相加返回
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
