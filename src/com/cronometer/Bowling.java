package com.cronometer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mohammedmaaz
 *
 */

public class Bowling {
	private static final int MYTHREADS = 5;

	public static void main(String args[]) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		List<String[]> rollsList = new ArrayList<String[]>();
		List<Integer> scoreList = new ArrayList<Integer>();

		try {
			URL url = new URL("https://s3.amazonaws.com/cronometer.media/codetest/bowling-data.csv");
			try (InputStream in = url.openStream(); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				String line = br.readLine();
				String cvsSplitBy = ",";
				while (line != null) {
					String[] rollsArr = line.split(cvsSplitBy);
					Arrays.asList(rollsArr).replaceAll(s -> s.replaceAll("X", "10"));

					for (int i = 0; i < rollsArr.length; i++) {
						if (rollsArr[i].equals("/")) {
							int diff = 10 - Integer.parseInt(rollsArr[i - 1]);
							rollsArr[i++] = String.valueOf(diff);
						}
					}
					rollsList.add(rollsArr);
					line = br.readLine();
				}
			}

			ConcurrentHashMap<String, Integer> m = new ConcurrentHashMap<>();
			for (Iterator<String[]> itr = rollsList.iterator(); itr.hasNext();) {
				String[] rolls = itr.next();
				Runnable worker = new MyRunnable(Arrays.copyOfRange(rolls, 1, rolls.length), m, rolls[0]);
				executor.execute(worker);
			}
			executor.shutdown();
			// Wait until all threads are finish
			while (!executor.isTerminated()) {

			}
			System.out.println("\nFinished all threads");
			Bowling b = new Bowling();
			for (String key : m.keySet()) {
				scoreList.add(m.get(key));
			}
			Collections.sort(scoreList);

			System.out.println("Median Score: " + b.median(scoreList));
			System.out.println("Mean Score: " + b.mean(scoreList));
			System.out.println("Standard Deviation of Score: " + b.sd(scoreList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int sum(List<Integer> a) {
		if (a.size() > 0) {
			int sum = 0;

			for (Integer i : a) {
				sum += i;
			}
			return sum;
		}
		return 0;
	}

	public double mean(List<Integer> a) {
		int sum = sum(a);
		double mean = 0;
		mean = sum / (a.size() * 1.0);
		return mean;
	}

	public double median(List<Integer> a) {
		int middle = a.size() / 2;

		if (a.size() % 2 == 1) {
			return a.get(middle);
		} else {
			return (a.get(middle - 1) + a.get(middle)) / 2.0;
		}
	}

	public double sd(List<Integer> a) {
		int sum = 0;
		double mean = mean(a);

		for (Integer i : a)
			sum += Math.pow((i - mean), 2);
		return Math.sqrt(sum / (a.size() - 1)); // sample
	}
}
