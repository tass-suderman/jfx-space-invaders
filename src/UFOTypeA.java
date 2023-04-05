package assign3cst145;
//Tass Suderman CST145
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.image.Image;

public class UFOTypeA extends Sprite
{


	public static Thread gravityThread;
	
	public boolean isDropping;
	
	private ArrayList<Image> ufoSkin;
	
	private static final int SPEED=1;
	
	public static final double MURDER_THRESHHOLD = Invasion.obCanvas.getHeight()-75.0;
	
	public static int turnDelay = 60; //we change this to make UFOs go zoooooooom
	
	
	public UFOTypeA(double nXPos, double nYPos, ArrayList<Image> lstSkin)
	{
		super(lstSkin.get(0), nXPos, nYPos);
		this.ufoSkin = lstSkin;
		isDropping=true;
		this.render(Invasion.gc);
		this.beginDescend();
	}


	
	/**
	 * This method tells a UFO to begin falling
	 */
	public void beginDescend()
	{
		gravityThread = new Thread(() -> this.runTask());
		gravityThread.setDaemon(true);
		gravityThread.start();
	}
		
	/**
	 * The work completed in the thread specified above-- regulates falling and png changing
	 */
	protected void runTask()
	{
		try
		{
			int nPos = 0;
			while (true && this.isDropping)
			{
				this.setImage(this.ufoSkin.get(nPos % this.ufoSkin.size()));
				nPos++;
				Platform.runLater(() -> this.gravity());
				Platform.runLater(()-> this.checkBottom());
				Thread.sleep(turnDelay);
			}

		}

		catch (InterruptedException exp)
		{
			exp.printStackTrace();
		}
	}
	
	/**
	 * This method checks if the UFO has killed the player yet
	 */
	private void checkBottom()
	{
		if  (this.getCoors().getY() > MURDER_THRESHHOLD && !Invasion.dead) 
		{
			Platform.runLater(()->Invasion.playerKill("Game OVER"));
		}
	}

	/**
	 * simulates a single ufo dropping
	 * 
	 * @param obSprite
	 */
	protected void gravity()
	{
		this.clear(Invasion.gc);
		this.getCoors().incY(SPEED);
		this.render(Invasion.gc);
	}
	
	
	/**
	 * This is called when a player destroys a UFO
	 * @param obShell
	 */
	public void ufoDeath(Shell obShell)
	{
		Platform.runLater(()->{

			obShell.clear(Invasion.gc);
			this.clear(Invasion.gc);
			this.isDropping = false;
		
		});
		Thread obThread = new Thread(() -> this.coolExplosion());
		obThread.setDaemon(true);
		obThread.start();
		Invasion.lstShells.remove(obShell);
		Invasion.ufoList.remove(this);
		if(Invasion.ufoList.size()<1)
		{
			Invasion.OBLEVEL.levelUp();
		}
		Invasion.obLauncher.increment();
	}
	


	/**
	 * your fancy explosion method
	 */
	private void coolExplosion()
	{
		for (int i = 1; i <= 9; i++)
		{
			this.setImage(new Image("file:images/bang" + i + ".png"));
			Platform.runLater(() -> this.gravity());
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException exp)
			{

			}
		}

		Platform.runLater(() ->
		{
			this.clear(Invasion.gc);
		});

	}

}
