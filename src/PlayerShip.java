package assign3cst145;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.KeyEvent;
//Tass Suderman CST145
/**
 * This class controls the player ship
 * @author Tass
 *
 */
public class PlayerShip extends Sprite
{
	
	private final static double XPOS = 150, YPOS = 540;

	private final static int RECOVERY_PER_CYCLE = 1;
	public int reloadTime = 100; //this refers to the reload time; this is the maximum number of cycles reloading will take
	private int recovery = 0; //weapon 1 recovery state
	private int recovery2 = 0; //weapon 2 recovery state
	
	public boolean weapon2 = false; //this controls whether or not the player has access to the second weapon
	private int speed; //this controls ship speed
	
	private int score = 0;
	
	
	public PlayerShip()
	{
		super(LAUNCHER, XPOS, YPOS);
		this.render(Invasion.gc);
		this.speed=3;
	}
	
	/**
	 * This is called from the window and handles all controls
	 * @param e
	 */
	public void controller(KeyEvent e)
	{
		switch (e.getCode())
		{
			case LEFT:
				this.flyLeft();
				break;
			case RIGHT:
				this.flyRight();
				break;
			case UP:
				if (recovery > 0)
				{
					break;
				}
				this.launchMissile();
				break;			
			case DOWN:
				if (recovery2 > 0 || !weapon2)
				{
					break;
				}
				this.launchWeapon2();
				break;
			case ESCAPE:
				ChoiceDialog<String> obConf = new ChoiceDialog<>();
				obConf.getItems().addAll("Yes","No");
				obConf.setSelectedItem("Yes");
				obConf.setTitle("You giving up already?");
				obConf.setHeaderText("Are you sure you want to quit?");
				Optional<String> sAnswer = obConf.showAndWait();
				if(sAnswer.get().equals("Yes"))
				{
					Platform.runLater(()->Invasion.playerKill("Running away already?"));	
				}
			default:
				break;
		}
	}
	
	/**
	 * This method is called upon left keypress-- results in ship flying left
	 */
	private void flyLeft()
	{
		this.clear(Invasion.gc);
		this.getCoors().decX(this.getCoors().getX()<-25 ? 0 : this.speed);
		this.render(Invasion.gc);
	}
	
	/**
	 * This method is called upon right keypress-- results in ship flying right
	 */

	private void flyRight()
	{
		this.clear(Invasion.gc);
		this.getCoors().incX(this.getCoors().getX()>Invasion.CANVASWIDTH-40 ? 0 : this.speed);
		this.render(Invasion.gc);
	}
	
	/**
	 * This results in a shell firing
	 */
	private void launchMissile()
	{
		Platform.runLater(() -> { 
			new Shell(this);
			recovery = reloadTime;
		}); 
		
	}
	
	private void launchWeapon2()
	{
		Platform.runLater(() -> { 
			new Shell(this, Shell.DEFAULTSPEED*3);
			recovery2 = reloadTime*3;
		}); 
	}
	
	/**
	 * This is called in the runbarrage method and reduces cooldown timers
	 */
	public void reloading()
	{
		//This is called from the shell thread and is used to reload the weapons
		if (recovery <= 0 && recovery2 <= 0)
		{
			return;
		}
		recovery -= recovery > 0 ? RECOVERY_PER_CYCLE : 0;
		recovery2 -= recovery2 > 0 ? RECOVERY_PER_CYCLE: 0;
		
	}
	

	//this is just to add to the score upon a kill
	public void increment()
	{
		synchronized (this)
		{
			score++;
		}
	}
	
	/**
	 *Getter for score for the leaderboards constructor 
	 * @return
	 */
	public int getScore()
	{
		return this.score;
	}
}
