package student;

import java.util.Arrays;

/* 
 *   Methods:
 *   
 *   loadBalance - 	Solves the load balancing problem by using the heuristic method
 *  				found in the function boundedBalancer()
 *  
 *  boundedBalancer - 	Uses a bound and heuristics to find the best solution 
 *  					given the provided bound
 */

public class LoadBalance {

	/*
	 * Load Balancing Algorithm
	 * 
	 * Inputs: procs- the number of processors; tasks - an array of task values
	 * Outputs: partitions - result array return the amount of work in the
	 * largest partition
	 * 
	 * 
	 * Calculates the maximum using the balancing algorithm. The initial pass is
	 * done attempting to obtain the average value in each subarray. Then
	 * subsequent passes are performed up to the lowest maximum value
	 * encountered. This greedily searches a subset of the problem, using a
	 * heuristic method to calculate each pass.
	 * 
	 * The lowest maximum value is used as the threshold, as any solution with
	 * an array larger than this would not be a better solution. This works
	 * because the method iterates from attempting best-case scenario (all the
	 * average size) to the best-possible solution.
	 */

	public static int loadBalance(int procs, int[] tasks, int[] partitions) {

		int average = average(tasks, procs);
		int max = boundedBalancer(procs, tasks, partitions, average);
		int[] partitiontemp = partitions;

		for (int j = 1; j <= max - average; j++) {
			int temp = boundedBalancer(procs, tasks, partitiontemp, average + j);
			if (temp < max) {
				max = temp;
				partitions = partitiontemp;
			}
		}
		return max;
	}

	/*
	 * Load Balancing Algorithm
	 * 
	 * Inputs: procs- the number of processors tasks - an array of task values
	 * Outputs: partitions - result array return the amount of work in the
	 * largest partition
	 * 
	 * This function uses a heuristic method to determine the best possible
	 * arrangement given a specific bound. It uses the bound, as well as the
	 * average for the remaining arrays to determine the size of the array for
	 * each of the processors.
	 */

	private static int boundedBalancer(int procs, int[] tasks,
			int[] partitions, int bound) {
		int max = 0;
		int i = 0;
		for (int p = 0; p < procs; p++) {
			int total = 0;
			while (i < tasks.length) {
				
				// If adding this element would increase the total
				// past the current bound
				if (total + tasks[i] > bound) {
					int[] remainder = Arrays.copyOfRange(tasks, i + 1,
							tasks.length);
					
					// If the bound is greater than the average of the
					// remaining elements (and not last process), break
					// else, continue and add element
					if (bound > average(remainder, procs - p - 1)
							&& procs - 1 != p)
						break;
				}
				total += tasks[i++];
			}
			partitions[p] = total;
			if (total > max)
				max = total;
		}
		return max;
	}

	/*
	 * Average Helper Function
	 * 
	 * Inputs: a - an array of integers procs - a number of processors \
	 * 
	 * Outputs: average - sum of a divided by procs
	 */

	private static int average(int[] a, int procs) {
		if (procs == 0)
			return 0;
		int sum = 0;
		for (int number : a) {
			sum += number;
		}
		return sum / procs;
	}
}
