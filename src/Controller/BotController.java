package Controller;

/** 
 * @author Robin Duda

 * @version 1.0
 * @date 2014-10-26
 * 
 * Controller:BotController: Bot Driver.
 */

import model.Bot;
import model.Memory;

public class BotController implements Runnable{
	private Memory memory;
	private Bot bot;
	private Controller controller;
	private Thread thread;

	// create the botcontroller.
	public BotController(Memory memory, Bot bot, Controller controller)
	{
		this.memory = memory;
		this.bot = bot;
		this.controller = controller;
	}
	
	// start the controller thread.
	public void start()
	{
		this.thread = new Thread(this, bot.getName());
		thread.start();
	}
	
	/*** thread Execute method, detect if bot should pick.*/
	@Override
	public void run() {
		// the thread is alive while the game is alive.
		while (!memory.isFinished()) {
			if (memory.whoseTurn().equals(bot.getName())) {
				try {
					Thread.sleep(375);
				} catch (InterruptedException e) {
				} 
				memory.turn();
				controller.botMessage();
			}
			try {
				Thread.sleep((-bot.getMindfulness() * 2 + 12) * 250); // 1s, 2s, 3s, 4s 
																// time/turn.
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}
		}
		System.out.println("Game Finished, Thread Stopped.");
	}

}
