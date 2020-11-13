package com.cronometer;

/**
 * @author mohammedmaaz
 *
 */
public class BowlingGame {
	String[] rolls;
	int currentRoll;

	public BowlingGame() {
		this.rolls = new String[21];
	}

	public void roll(String p) {
		rolls[currentRoll++] = p;
	}

	public String[] getRolls() {
		return rolls;
	}

	public void setRolls(String[] rolls) {
		this.rolls = rolls;
	}

	public int score() {
		int score = 0;
		int frame = 0;

		for (int i = 0; i < 10; i++) {
			if (isStrike(frame)) {
				if (i < 9) {
					// Check for Double strike
					int tempFrame = frame + 2;
					if (isStrike(tempFrame)) {
						score += 10 + sumOfDoubleRolls(tempFrame);
					} else {
						score += 10 + strikeBonus(frame);
					}
					frame += 2;
				}else {
					// Changes for Last frame
					score += 10 + strikeBonusForLastFrame(frame);
					frame++;
				}
			} else if (isSpare(frame)) {
				score += 10 + spareBonus(frame);
				frame += 2;
			} else {
				score += sumOfRolls(frame);
				frame += 2;
			}
		}
		return score;
	}

	private boolean isStrike(int frame) {
		return rolls[frame].equals("10");
	}

	private boolean isSpare(int frame) {
		return sumOfRolls(frame) == 10;
	}

	private int strikeBonus(int frame) {
		return sumOfRolls(frame + 2);
	}
	
	private int strikeBonusForLastFrame(int frame) {
		return sumOfRolls(frame + 1);
	}

	private int spareBonus(int frame) {
		return Integer.parseInt(rolls[frame + 2]);
	}

	private int sumOfRolls(int frame) {
		return Integer.parseInt(rolls[frame]) + Integer.parseInt(rolls[frame + 1]);
	}

	private int sumOfDoubleRolls(int frame) {
		return Integer.parseInt(rolls[frame]) + Integer.parseInt(rolls[frame + 2]);
	}
}