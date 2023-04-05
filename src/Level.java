package assign3cst145;
//Tass Suderman CST145

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class manages all the levels and their differences in the game.
 * The ufo quantity, type variety, and speed increase as the game progresses.
 * Another weapon is unlocked for the player, and their cycle speed increases.
 * I opted to increased rocket reload speed rather than decrease it in response to levelling up, because watching the UFOs come down with nothing you can do feels unfun
 * @author Tasss
 *
 */
public class Level
{
	private int difficulty;
	private static final int SCALING = 4;
	private static final String[] DIALOGCONTENT = { "Here comes the first wave, Watch out!.\n[Remember, left and right to maneuver; up to shoot!]", 
			"Great work on this one! We've built something up in the lab for you: The JavaScript Cannon. As it is interpreted rather than compiled, "
			+ "you'll notice its bullets travel a little faster, but it takes us so much longer to debug each one that they take longer to recharge. Fire them by pressing DOWN", 
			"Beware! They've been monkeying away with their 3D Printers and now they are deploying the CAT CAM Units."};

	
	
	public Level()
	{
		this.difficulty=1;
//		levelUp();
		
	}
	
	/**
	 * This is called whenever all UFOs are destroyed. it changes stats and triggers the next wave to spawn in 
	 */
	protected void levelUp()
	{
		if(this.difficulty ==2)
		{
			Invasion.obLauncher.weapon2=true;//At level 2, the second weapon is unlocked
		}
		Invasion.ufoList.clear();
		
		levelAnnounce();
		addUFOs();
		this.difficulty++;

		Invasion.obLauncher.reloadTime-= Invasion.obLauncher.reloadTime > 30 ? 2 : 0;
		UFOTypeA.turnDelay-= UFOTypeA.turnDelay>20 ? 7 : 0;
		Invasion.turnDelay-= Invasion.turnDelay>10 ? 1 : 0;
	}
	
	/**
	 * This textbox pops up on every new level.
	 */
	private void levelAnnounce()
	{
		Alert obAlert = new Alert(AlertType.INFORMATION);
		obAlert.setTitle("Level Complete!");
		obAlert.setContentText(difficulty-1 < DIALOGCONTENT.length ? DIALOGCONTENT[difficulty-1] : "Great work! Your bullet speed and recharge speed have increased");
		obAlert.setHeaderText("You've reached level " + difficulty);
		obAlert.showAndWait();
	}
	
	/**
	 * This method populates the field with the UFOs as they spawn in
	 * As the difficulty increases, new variants and larger quantities spawn
	 */
	private void addUFOs()
	{	
		int nTotalUFO = SCALING*this.difficulty;
		int nGuard = (int) (Math.random()*nTotalUFO/2) + 4;
		int nHeight = 0;
		for (int i = 1; i<= nGuard; i++)
		{
			Invasion.ufoList.add(new UFOTypeC(((i%5)*70)+55, nHeight, UFOTypeC.GUARDIANIMAGES));
			if(i%5==0) 
			{
				nHeight-=100;
			}
			
		}
		for (int i=1; i<=nTotalUFO-nGuard+2; i++)
		{
			nHeight-= 75;
			Invasion.ufoList.add(new UFOTypeA((Math.random()*Invasion.CANVASWIDTH-50)+25,nHeight, UFOTypeA.UFOIMAGES));
		}
		if(this.difficulty>=3) 
		{
			for (int i=0; i<SCALING*(difficulty-2); i++)
			{
				nHeight -=75;
				Invasion.ufoList.add(new UFOTypeB((Math.random()*Invasion.CANVASWIDTH-50)+75, nHeight,UFOTypeB.CATIMAGES));
			}
		}
	}

}
