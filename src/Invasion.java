package assign3cst145;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//Tass Suderman CST145
public class Invasion extends Application
{
	protected static final double CANVASWIDTH=400, CANVASHEIGHT=600;

	protected static Canvas obCanvas = new Canvas(CANVASWIDTH, CANVASHEIGHT);
	protected static GraphicsContext gc = obCanvas.getGraphicsContext2D();
	
	protected static ArrayList<Shell> lstShells = new ArrayList<>();
	protected static ArrayList<UFOTypeA> ufoList = new ArrayList<>();
	
	protected static final Level OBLEVEL = new Level();
	
	public static int turnDelay =25;
	public static Optional<String> userName;
	private static Line obLine;
	private static Thread obGThread;
	private static Stage stgScore = new Stage();
	
	public static PlayerShip obLauncher = new PlayerShip();
	private static Stage obStage = new Stage();;
	public static boolean dead = false;
	
	//Game Launches into splash screen
	@Override
	public void start(Stage gameStage) throws Exception
	{
		obStage=gameStage;
		obStage.getIcons().add(UFOTypeA.UFOIMAGES.get(0));
		obStage.setScene(new Scene(introScreen(),694,520));
		obStage.show();
		

	}
	
	/**
	 * Constructs the splash screen.
	 * Gives buttons to start game, view Scores, or exit
	 * @return
	 */
	public static GridPane introScreen()
	{
		TextInputDialog dlText = new TextInputDialog();
		
		Text txtTitle = new Text("It's up to you to send em back!");
		txtTitle.setStyle("-fx-font-color: white");
		txtTitle.autosize();
		txtTitle.setFont(Font.font("Verdana", 14));
		txtTitle.setFill(Color.WHITE);
		
        Image img = new Image("file:images/background.png");
        BackgroundImage bImg = new BackgroundImage(img,
                                                   BackgroundRepeat.NO_REPEAT,
                                                   BackgroundRepeat.NO_REPEAT,
                                                   BackgroundPosition.DEFAULT,
                                                   BackgroundSize.DEFAULT);
		
		dlText.setTitle("Identification");
		dlText.setHeaderText("Welcome aboard captain.. uhh.. what was your name again?\n*letters and spaces only");
		
		
		Button btnPlay = new Button("Play now!");
		Button btnScore = new Button("View Leaderboards");
		Button btnExit = new Button("Exit");
		btnPlay.setOnAction(e->{
			while(true) 
			{

				userName = dlText.showAndWait();
				if(userName.isPresent() && userName.get().matches("[A-Z a-z]+"))
				{
					break;
				}
				
			}
			obStage.close();
			Platform.runLater(()->startGame());
		});

		btnScore.setOnAction(e-> {
			showScore("High Score");
		});
		
		btnExit.setOnAction(e->System.exit(0));

		GridPane obReturn = new GridPane();
		
		obReturn.add(btnPlay, 0,1);
		obReturn.add(btnScore, 0, 2);
		obReturn.add(btnExit, 0, 3);
		obReturn.getChildren().stream().map(x->(Button)x).forEach(x->{
			x.setAlignment(Pos.BOTTOM_CENTER); 
			x.setPadding(new Insets(20,20,20,20));
			x.setStyle("-fx-background-color: #FF000099");
			GridPane.setHalignment(x, HPos.CENTER);
			x.setMinWidth(200);
			x.setMaxWidth(200);
			x.setOnMouseEntered(e->x.setStyle("-fx-background-color: red"));
			x.setOnMouseExited(e->x.setStyle("-fx-background-color: #FF000099"));
		});
		obReturn.setVgap(10);
		obReturn.setAlignment(Pos.BOTTOM_CENTER);
		obReturn.setBackground(new Background(bImg));

		obReturn.add(txtTitle, 0, 0);
		return obReturn;
	}
	
	

	/**
	 * This is called to start the actual game, after user name is entered
	 */
	private static void startGame()
	{
		Pane obPane = new Pane(obCanvas);

		OBLEVEL.levelUp();
		startTask();


		obPane.setBackground(new Background(new BackgroundImage(new Image("file:images/space.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		obLine = new Line(0, CANVASHEIGHT-50, CANVASWIDTH, CANVASHEIGHT-50);
		obLine.setStroke(Color.RED);
		obPane.getChildren().add(obLine);
		
		obStage.setScene(new Scene(obPane));
		obStage.setTitle("Send them back!");
		obStage.show();

		obPane.requestFocus();

		obPane.setOnKeyPressed(e ->
		{
			obLauncher.controller(e);

		});
		
	}
	
	/**
	 * This is checked once every game cycle for collisions
	 * @param obShell
	 * @param obUFO
	 */
	private static void track(Shell obShell, UFOTypeA obUFO)
	{
		// Check to see if we have an intersection
		if (obShell.intersects(obUFO) && obUFO.isDropping)
		{
			Platform.runLater(()->obUFO.ufoDeath(obShell));
		}
	}

	/**
	 * This is called when the game is exited or lost
	 * it removes all shells and UFOs and launches the score screen
	 */
	public static void playerKill(String sExitState)
	{
		dead = true;
		obStage.close();
		stgScore.setTitle(sExitState);
		stgScore.setOnCloseRequest(e->System.exit(0));
		ufoList.forEach(x->x.clear(gc));
		lstShells.forEach(x->x.clear(gc));
		ufoList.clear();
		lstShells.clear();
		obLauncher.clear(gc);
		synchronized (ScoreIO.lstScores)
		{
			ScoreIO.loadScore();
			ScoreIO.lstScores.add(new LeaderBoards(userName.get(), obLauncher.getScore()));
			ScoreIO.saveScores();
			showScore(sExitState);
		}

		
	}

	/**
	 * This begins the shell thread
	 */
	synchronized private static void startTask()
	{
		obGThread = new Thread(() -> runBarrage());
		obGThread.setDaemon(true);
		obGThread.start();
		

	}

	/**
	 * This is the turn cycle controller. one big thread for the shells and ships
	 */
	private static void runBarrage()
	{
		while (true)
		{
			for (Shell obShell : lstShells)
			{
				if (obShell.getCoors().getY() < 0)
				{
					Platform.runLater(() -> obShell.clearShell());
					continue;
				}
				else 
				{
					Platform.runLater(() -> {
						obShell.clear(gc);
						obShell.getCoors().decY(obShell.getSpeed());
						obShell.render(gc);
					});
				}
				for (UFOTypeA obUFO : ufoList)
				{
					Platform.runLater(() -> track(obShell, obUFO));
					
				}
			}
			try
			{
				obLauncher.reloading();
				Thread.sleep(turnDelay);
				
			} 
			catch (InterruptedException exp)
			{
				exp.printStackTrace();
			}
		}

	}

	/**
	 * This is called from the 'show leaderboards' and from the death screen.
	 */
	private static void showScore(String sScoreState)
	{
		ScoreIO.loadScore();
		Text txtTop = new Text(String.format("1. %s.......%d", ScoreIO.lstScores.get(0).getName(),ScoreIO.lstScores.get(0).getScore()));
		Text txtMid = new Text(String.format("2. %s.......%d", ScoreIO.lstScores.get(1).getName(),ScoreIO.lstScores.get(1).getScore()));
		Text txtBot = new Text(String.format("3. %s.......%d", ScoreIO.lstScores.get(2).getName(),ScoreIO.lstScores.get(2).getScore()));
	
		GridPane obScore = new GridPane();
		obScore.add(txtTop, 0, 0);
		obScore.add(txtMid, 0, 1);
		obScore.add(txtBot, 0, 2);
		obScore.setAlignment(Pos.CENTER);
		stgScore.setScene(new Scene(obScore,300,300));
		stgScore.setTitle(sScoreState);
		stgScore.showAndWait();
		
	}

	public static void main(String[] args)
	{
		Application.launch(args);

	}
	
	
}
