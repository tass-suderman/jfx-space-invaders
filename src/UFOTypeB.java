package assign3cst145;
import java.util.ArrayList;

import javafx.scene.image.Image;
//Tass Suderman CST145
/**
 * Subclass of UFO -- for the CAT CAM crew
 * @author Tasss
 *
 */
public class UFOTypeB extends UFOTypeA
{

	private static final int SPEED=1;

	public UFOTypeB(double nXPos, double nYPos, ArrayList<Image> lstSkin)
	{
		super(nXPos, nYPos, lstSkin);
		this.setImage(Sprite.CATIMAGES.get(0));
		this.render(Invasion.gc);
	}
	
	@Override
	protected void gravity()
	{
		this.clear(Invasion.gc);
		this.getCoors().incY(SPEED);
		this.sideToSide(); //main differences are in the images and this new sidetoside method
		this.render(Invasion.gc);
	}

	/**
	 * This method tells the UFO to move speed*2 in either left or right
	 */
	private void sideToSide()
	{
		if(this.getCoors().getX() <= 0)
		{
			this.getCoors().incX(SPEED*2);
		}
		if(this.getCoors().getX() > Invasion.CANVASWIDTH)
		{
			this.getCoors().decX(SPEED*2);
		}
		else
		{
			this.getCoors().incX((int)Math.round(Math.random()*4-2));
		}
		
	}
	
	
}
