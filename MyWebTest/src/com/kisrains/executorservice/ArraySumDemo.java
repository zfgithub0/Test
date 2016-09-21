package com.kisrains.executorservice;

import java.math.BigDecimal;
//�������3��Demo
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
		System.out.println("��˲��м��㣬�첽��ӣ�"+time(time,endTime));
		
		long time2 = System.currentTimeMillis();
		concurrentCaculator(numbers);
		long endTime2=System.currentTimeMillis();
		System.out.println("��˲��м��㣬ͬ����ӣ�"+time(time2,endTime2));
	}


	private static void basic(int[] numbers) {
		long time1 = System.currentTimeMillis();
		long sum=BasicCaculator.sum(numbers);
		long endTime1 = System.currentTimeMillis();
		System.out.println("���̣߳�"+time(time1,endTime1));
		System.out.println("Sum:"+sum);
	}


	private static double time(long time, long endTime) {
		long costTime = endTime-time;
		BigDecimal bd = new BigDecimal(costTime);
		System.out.println("bd===="+bd);
		//�������ţ��Ѻ���ת������ģ�����ּ���̫����
		BigDecimal unit = new BigDecimal(1L);
		System.out.println("unit===="+unit);
		BigDecimal s= bd.divide(unit,3);
		return s.doubleValue();
	}


	//���м��㣬��ͬ�������
	private static void concurrentCaculator(int[] numbers) {
		ConcurrentCalculator calc = new ConcurrentCalculator();
		Long sum = calc.sum(numbers);
		System.out.println(sum);
		calc.close();
	}


	//���м��㣬���첽�����
	private static void concurrentCaculatorAsync(int[] numbers) {
		ConcurrentCalculatorAsync calc = new ConcurrentCalculatorAsync();
		Long sum = calc.sum(numbers);
		System.out.println("Sum:"+sum);
		calc.close();
	}
}
