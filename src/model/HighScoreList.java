package model;

/***
* @author Robin Duda
* @version 1.0
* @date 2014-10-26
* 
* Model: Highscores to file, from file, update.
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HighScoreList {
	private int[] score;
	private String[] name;

	/*** load HighScores from a file at startup.
	 * @see HighScoreList#load()*/
	public HighScoreList() {
		score = new int[ENTRIES];
		name = new String[ENTRIES];
		this.load();
	}

	/*** returns a score from dataset.
	 * 
	 * @param index index of the score to be obtained.
	 * @return the value of the score, in string format.
	 */
	public String getScore(int index) {
		return score[index] + "";
	}

	/*** use this method to get the name of the highscore-holder by index.
	 * 
	 * @param index index of the highscore-holder to be returned.
	 * @return returns the highscore-holders name in String format.
	 */
	public String getName(int index) {
		if (name[index] != null) {
			return name[index];
		} else
			return "";
	}

	/*** load highscores from file given by HighScoreList#FILENAME 
	 * @see HighScoreList#FILENAME*/
	public void load() {
		String line = null;
		int i = 0;

		try {
			// point the filereader to a file.
			FileReader fileReader = new FileReader(FILENAME);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// loop through the file while there is data left.
			while ((line = bufferedReader.readLine()) != null && line != "") {
				name[i] = line;

				if ((line = bufferedReader.readLine()) != null) {
					score[i] = Integer.parseInt(line);
				}
				i++;
			}
			// close the file.. :(
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// the file does not exist, but its not a problem. Expected for first launches.
		} catch (IOException e) {
			// some IOException occured, data in file not crucial to application.
		}
	}

	/*** write highscore data to file. file is specified in file.
	 * @see HighScoreList#FILENAME
	 */
	public void save() {
		try {
			FileWriter fileWriter = new FileWriter(FILENAME);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (int i = 0; i < ENTRIES; i++) {
				if (name[i] != null) {
					bufferedWriter.write(name[i]);
					bufferedWriter.newLine();
					bufferedWriter.write(String.valueOf(score[i]));
					bufferedWriter.newLine();
				}
			}

			// close the file.. :(
			bufferedWriter.close();
		} catch (IOException ex) {
			// IOException occured, highscore could not be saved. Nothing much to do.
		}
	}

	/*** insert mode, make room for a new highscorer. The last index (player with lowest score)
	 *   will be overwritten. 
	 * 
	 * @param index index to be packed, all items after and including index are moved back.
	 */
	private void pack(int index) {
		for (int i = ENTRIES - 1; i > index; i--) {
			name[i] = name[i - 1];
			score[i] = score[i - 1];
		}
	}

	/*** this method will set the current winners name and score.
	 * @param name name of the new winner.
	 * @param score score of the new winner.
	 */
	public void update(String name, int score) {
		int i = -1;

		// loop through the highscores, finding an entry point.
		do {
			i++;
		} while (i < ENTRIES && score < this.score[i]);

		// new scores that ties with an old one is considered higher valued.
		if (i < ENTRIES && score >= this.score[i]) {
			this.pack(i);
			this.name[i] = name;
			this.score[i] = score;
			this.save();
		}
	}

	public static final int ENTRIES = 10;
	private static final String FILENAME = "HalloweenMemory-HighScore";
}
