package assign3cst145;
//Tass Suderman CST145
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used for the save file I/O
 * The code could probably be trimmed down quite a bit; i spent a long time debugging a simple syntax error and lost the plot on some of this code 
 * @author Tasss
 *
 */
public class ScoreIO
{
	public static ArrayList<LeaderBoards> lstScores = new ArrayList<LeaderBoards>();
	
	private static String FILEPATH = "images/tassscore.cst145";
	private static final String NEWFILE = "Captain'0\nCaptain'0\nCaptain'0\n"; //We dont really want to have a file without three saves, so i use this to pad as needed

	private static String lstScoreSave ="";
	
	/**
	 * This method will load the score from a file path specified above. if the file does not exist, it will be created and filled with filler data
	 */
	synchronized public static void loadScore()
	{
		lstScores.clear();
		File obFile = new File(FILEPATH);
		if (!obFile.exists())
		{
			try
			{
				PrintWriter obOut = new PrintWriter(obFile);
				obOut.print(NEWFILE);
				obOut.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		//After we are sure it is filled properly, we read it in and populate the score list accordingly
		try (Scanner obIn = new Scanner(new File(FILEPATH)))
		{
			
			String sFile = "";
			while (obIn.hasNext())
			{
				sFile += obIn.nextLine()+";";
			}
			if(sFile.contains(";")) 
			{
				String[] saLines = sFile.split(";");
				for (String sSave : saLines)
				{
					if(sSave.isBlank())
					{
						continue;
					}
					String[] saSaveVal = sSave.split("'");
					lstScores.add(new LeaderBoards(saSaveVal[0], Integer.parseInt(saSaveVal[1])));
				}
			}
			
		}
		catch (FileNotFoundException e)
		{		
			e.printStackTrace();
			
		}
	}
	
	
	/**
	 * This is called upon game end and is responsible for saving the updated scores,
	 */
	synchronized public static void saveScores()
	{
		synchronized (lstScoreSave)
		{
			lstScoreSave = "";
			lstScores.stream().sorted((x,y)-> y.getScore()-x.getScore()).
			limit(3).forEach(x->lstScoreSave+=x.toFile());
		}
		File obFile = new File(FILEPATH);
		try
		{
			PrintWriter obOut = new PrintWriter(obFile);
			obOut.print(lstScoreSave);
			obOut.close();
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadScore();
	}
	
}
