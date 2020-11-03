package com.cronometer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mohammedmaaz
 *
 */
public class MyRunnable implements Runnable {
	private String[] rolls;
	public ConcurrentHashMap<String, Integer> map;
	private String gameID;

	MyRunnable(String[] rolls, ConcurrentHashMap<String, Integer> m, String gameID) {
		this.rolls = rolls;
		this.map = m;
		this.gameID = gameID;
	}

	@Override
	public void run() {
		try {
			BowlingGame oBowlingGame = new BowlingGame();
			oBowlingGame.setRolls(this.rolls);
			for (int i = 0; i < this.rolls.length; i++)
				System.out.print(" " + this.rolls[i]);
			System.out.println("");
			int score = oBowlingGame.score();
			map.put(gameID, score);
			System.out.println(gameID + " #" + score);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
