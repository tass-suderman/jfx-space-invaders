package assign3cst145;
//Tass Suderman CST145
import java.util.ArrayList;

import javafx.scene.image.Image;
/**
 * UFO Subclass -- classic space invaders variant
 * These come in groups of 4 and move in a synchronized pattern more akin to classic space invaders
 * @author Tasss
 *
 */
public class UFOTypeC extends UFOTypeA
{
	
	private int direction;
	private int timeToChange;
	private static final int SPEED = 1;

	public UFOTypeC(double nXPos, double nYPos, ArrayList<Image> lstSkin)
	{
		super(nXPos, nYPos, lstSkin);
		this.setImage(GUARDIANIMAGES.get(0));
		this.render(Invasion.gc);
		this.timeToChange=20;
		this.direction = -1;
	}

	
	@Override
	protected void gravity()
	{
		this.clear(Invasion.gc);
		this.getCoors().incY(SPEED);
		this.sideToSide();
		this.render(Invasion.gc);
	}

	
	private void sideToSide()
	{
		if(this.timeToChange<=0)
		{
			direction *= -1;
			this.timeToChange=20;
		}

		this.timeToChange--;
		this.getCoors().incX(direction);
	}
	
}
