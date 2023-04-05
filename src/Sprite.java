package assign3cst145;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

//Tass Suderman CST145
/**
* This class is largely untouched from how it was when you gave it to us
* I really just made it abstract and threw all the assets in here
*/
public abstract class Sprite
{
	private final static String PLAYER_SPIRTE = "file:images/Launch.png";
	
	protected final static Image LAUNCHER = new Image(PLAYER_SPIRTE);
	
	private static final ArrayList<Integer> UFONUM = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5));
	private static final ArrayList<Integer> EXPLOSIONNUM = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12));
	
	
	private static final String FILEPATH = "file:images/", FILEEXT=".png";
	private static final String UFONAME = "ufo_", UFOBNAME="meow", UFOCNAME = "guard_", EXPLOSIONNAME="bang";
	
	
	
	public static final ArrayList<Image> UFOIMAGES= UFONUM.stream()
			.map(x->new Image(FILEPATH+UFONAME+x.toString()+FILEEXT))
			.collect(Collectors.toCollection(ArrayList<Image>::new));
	

	public static final ArrayList<Image> CATIMAGES= UFONUM.stream()
			.map(x->new Image(FILEPATH+UFOBNAME+x.toString()+FILEEXT))
			.collect(Collectors.toCollection(ArrayList<Image>::new));

	
	public static final ArrayList<Image> GUARDIANIMAGES= UFONUM.stream()
			.map(x->new Image(FILEPATH+UFOCNAME+x.toString()+FILEEXT))
			.collect(Collectors.toCollection(ArrayList<Image>::new));

	
	
	public static final ArrayList<Image> EXPLOSIONIMAGES= EXPLOSIONNUM.stream()
			.map(x->new Image(FILEPATH+EXPLOSIONNAME+x.toString()+FILEEXT))
			.collect(Collectors.toCollection(ArrayList<Image>::new));


	private static final String JAVAMISSILE = "file:images/shell.png";
	private static final String JAVASCRIPTMISSILE = "file:images/shell2.png";
	
	public static final Image IMGMISSILE = new Image(JAVAMISSILE);
	public static final Image IMGMISSILE2 = new Image(JAVASCRIPTMISSILE);
	
	
	
	private Image image;
	private Coors obCoor;

	private double width;
	private double height;

	public Sprite(Image image, double nXPos, double nYPos)
	{
		setImage(image);
		obCoor = new Coors(nXPos, nYPos);

	}

	public Image getImage()
	{
		return this.image;
	}

	public void setImage(Image image)
	{
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();

	}

	public double getWidth()
	{
		return this.width;
	}

	public double getHeight()
	{
		return this.height;
	}

	public void setPosition(double x, double y)
	{
		this.obCoor.setX(x);
		this.obCoor.setY(y);

	}

	public Coors getCoors()
	{
		return this.obCoor;

	}

	public void moveLeft(GraphicsContext gc)
	{

	}

	public void clear(GraphicsContext gc)
	{
		gc.clearRect(obCoor.getX(), obCoor.getY(), image.getWidth(), image.getHeight());

	}

	public void render(GraphicsContext gc)
	{
		gc.drawImage(image, obCoor.getX(), obCoor.getY());

	}

	public Rectangle2D getBoundary()
	{
		return new Rectangle2D(obCoor.getX(), obCoor.getY(), width, height);
	}

	public boolean intersects(Sprite spr)
	{
		return spr.getBoundary().intersects(this.getBoundary());
	}
}
