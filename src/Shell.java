package assign3cst145;
//Tass Suderman CST145

/**
 * This class is responsible for managing and creating the shells
 * @author Tasss
 *
 */
public class Shell extends Sprite
{

	public static final int DEFAULTSPEED=3;
	private int speed;
	
	/**
	 * This is the normal shell method
	 * @param obLauncher
	 */
	public Shell(PlayerShip obLauncher)
	{
		super(IMGMISSILE, 
				obLauncher.getCoors().getX() + obLauncher.getImage().getWidth() / 2 - IMGMISSILE.getWidth() / 2,
				obLauncher.getCoors().getY() - 10);
		Invasion.lstShells.add(this);

		this.speed=DEFAULTSPEED;
		this.render(Invasion.gc);

	}	
	
	/**
	 * This one is just overloading the above one so i dont need to make a new class -- dont let coralee see this
	 * @param obLauncher
	 * @param nSpeed
	 */
	public Shell(PlayerShip obLauncher, int nSpeed)
	{
		super(IMGMISSILE2, 
				obLauncher.getCoors().getX() + obLauncher.getImage().getWidth() / 2 - IMGMISSILE.getWidth() / 2,
				obLauncher.getCoors().getY() - 10);
		Invasion.lstShells.add(this);

		this.speed=nSpeed;
		this.render(Invasion.gc);

	}

	
	public void clearShell()
	{
		this.clear(Invasion.gc);
		Invasion.lstShells.remove(this);

	}

	public int getSpeed()
	{
		return this.speed;
	}


}
