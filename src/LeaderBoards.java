package assign3cst145;
//Tass Suderman CST145
/**
 * This is a simple class which holds a name and a score, and is able
 * to print them out in the proper file text formatting
 * @author Tass
 *
 */
public class LeaderBoards
{
	private String name;
	private int score;
	
	public LeaderBoards(String sName, int nScore)
	{
		this.name=sName;
		this.score=nScore;
	}




	public String getName()
	{
		return name;
	}



	public int getScore()
	{
		return score;
	}
	
	public String toFile()
	{
		return String.format("%s'%d\n", this.name, this.score);
	}
}
