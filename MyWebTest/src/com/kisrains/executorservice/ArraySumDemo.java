package com.kisrains.executorservice;

import java.math.BigDecimal;
//数组求和3个Demo
public class ArraySumDemo {
	public static void main(String[] args) {
		int n = 200000000;
		int[] numbers = new int[n];
		for(int i=1;i<=n;i++){
			numbers[i-1]=i;
		}
		basic(numbers);
		
		long time = System.currentTimeMillis();
		concurrentCaculatorAsync(numbers);
		long endTime=System.currentTimeMillis();
		System.out.println("多核并行计算，异步相加："+time(time,endTime));
		
		long time2 = System.currentTimeMillis();
		concurrentCaculator(numbers);
		long endTime2=System.currentTimeMillis();
		System.out.println("多核并行计算，同步相加："+time(time2,endTime2));
	}


	private static void basic(int[] numbers) {
		long time1 = System.currentTimeMillis();
		long sum=BasicCaculator.sum(numbers);
		long endTime1 = System.currentTimeMillis();
		System.out.println("单线程："+time(time1,endTime1));
		System.out.println("Sum:"+sum);
	}


	private static double time(long time, long endTime) {
		long costTime = endTime-time;
		BigDecimal bd = new BigDecimal(costTime);
		System.out.println("bd===="+bd);
		//本来想着，把毫秒转换成秒的，最后发现计算太快了
		BigDecimal unit = new BigDecimal(1L);
		System.out.println("unit===="+unit);
		BigDecimal s= bd.divide(unit,3);
		return s.doubleValue();
	}


	//并行计算，“同步”求和
	private static void concurrentCaculator(int[] numbers) {
		ConcurrentCalculator calc = new ConcurrentCalculator();
		Long sum = calc.sum(numbers);
		System.out.println(sum);
		calc.close();
	}


	//并行计算，“异步”求和
	private static void concurrentCaculatorAsync(int[] numbers) {
		ConcurrentCalculatorAsync calc = new ConcurrentCalculatorAsync();
		Long sum = calc.sum(numbers);
		System.out.println("Sum:"+sum);
		calc.close();
	}
}
