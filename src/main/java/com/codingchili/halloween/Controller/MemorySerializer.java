package com.codingchili.halloween.controller;

/** 
 * @author Robin Duda

 * @version 1.0
 * @date 2014-10-26
 * 
 * Controller.MemorySerializer: Serializes model to/from file.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.codingchili.halloween.model.Memory;

public abstract class MemorySerializer {
	
	// write the current game state to file.
	public static void storeGameState(Memory memory) {
		try {
			FileOutputStream fileOut = new FileOutputStream(
					"HalloweenMemory-GameState");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(memory);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	// read gamestate from file.
	public static Memory loadGameState() {
		Memory memory;
		try {
			FileInputStream fileIn = new FileInputStream(
					"HalloweenMemory-GameState");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			memory = (Memory) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception i) {
			//i.printStackTrace();
			return null;
		}
		return memory;
	}
	
	public static final String FILENAME = "HalloweenMemory-GameState";
}
