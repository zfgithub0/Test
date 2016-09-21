package com.kisrains.executorservice;

public class BasicCaculator {
	public static long sum(int[] numbers){
	    long sum = 0;
	    for(int i=0;i<numbers.length;i++){
	    	sum += numbers[i];
	    }
	    return sum;
	}
}
