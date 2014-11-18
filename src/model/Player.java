package model;

import java.io.Serializable;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * Model.player: The player model.
 */

public class Player implements Serializable
{
	private int score = 0;
	private String name;
	
	/*** @param name the name of the player.*/
	public Player(String name)
	{
		this.score = 0;
		this.name = name;
	}
	
	/*** @return the player name as String.*/
	public String getName()
	{
		return name;
	}
	
	/*** @param name new name of the player.*/
	public void setName(String name)
	{
		this.name = name;
	}
	
	/*** @return current score of player.*/
	public int getScore()
	{
		return this.score;
	}
	
	/*** @param score amount to add to player. */
	public void addScore(int score)
	{
		this.score += score;
	}
	private static final long serialVersionUID = 1L;
}
