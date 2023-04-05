package com.abhishek.gametwo;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int COLUMNS = 7;
	private static final int ROW = 6;
	private static final int Circle_Diameter = 80;
	private static final String disc_color1 = "#24303E";
	private static final String disc_color2 = "#4CAA88";

	private static String Player_One = "Player One";
	private static String Player_Two = "Player Two";

	private boolean isPlayerOneTurn = true;
	private  Disc[][] insertedDiscarray = new Disc[ROW][COLUMNS];



	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane;

	@FXML
	public Label playerNameLabel;
	@FXML
	public TextField Playeronetextfield , Playertwotextfield;
	@FXML
	public Button SetNames;


	private  boolean isallowedToInsertdisc = true;


	public void createplayGround() {
		Shape rectangleWithHoles = createGameStructuralgrid();

		rootGridPane.add(rectangleWithHoles, 0, 1);
		List<Rectangle> rectangleList = Clickablecolumns();
		for (Rectangle rectangle : rectangleList) {

			rootGridPane.add(rectangle, 0, 1);
			SetNames.setOnAction(event -> {
				Player_One = Playeronetextfield.getText();
				Player_Two = Playertwotextfield.getText();

			});

		}
	}

	private Shape createGameStructuralgrid(){

		Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * Circle_Diameter, (ROW + 1) * Circle_Diameter);
		for (int row = 0; row < ROW; row++){
			for (int col =0; col < COLUMNS; col++){
				Circle circle = new Circle();
				circle.setRadius(Circle_Diameter/2);
				circle.setCenterX(Circle_Diameter/2);
				circle.setCenterY(Circle_Diameter/2);
				circle.setSmooth(true);
				circle.setTranslateX(col* (Circle_Diameter +5)+ Circle_Diameter/4);
				circle.setTranslateY(row * (Circle_Diameter +5)+ Circle_Diameter/4);

				rectangleWithHoles = Shape.subtract(rectangleWithHoles,circle);
			}

		}

		rectangleWithHoles.setFill(Color.WHITE);
		return rectangleWithHoles;

	}

	private List<Rectangle> Clickablecolumns(){
		List<Rectangle> rectangleList = new ArrayList<>();
		for (int col=0;col<COLUMNS;col++){

			Rectangle rectangle = new Rectangle(Circle_Diameter,(ROW + 1) * Circle_Diameter);
			rectangle.setFill(Color.TRANSPARENT);

			rectangle.setTranslateX(col* (Circle_Diameter +5)+ Circle_Diameter/4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));

			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
			final int column = col;
			rectangle.setOnMouseClicked(event -> {
				if (isallowedToInsertdisc) {
					isallowedToInsertdisc = false;

					insertDisc(new Disc(isPlayerOneTurn), column);
				}
				});
			rectangleList.add(rectangle);

		}

         return rectangleList;
	}

	private void insertDisc(  Disc disc,int col){
		int row =ROW-1;
		while (row>=0){
			if (discisPresent(row,col) == null)
				break;

			row--;
		}
		if (row<0)
			return;

      insertedDiscarray[row][col] = disc;
      insertedDiscsPane.getChildren().add(disc);
      disc.setTranslateX(col* (Circle_Diameter +5)+ Circle_Diameter/4);
      int currentRow = row;
		TranslateTransition translateTransition =new TranslateTransition(Duration.seconds(0.5), disc);
		translateTransition.setToY(row * (Circle_Diameter +5)+ Circle_Diameter/4);
		translateTransition.setOnFinished(event -> {
			isallowedToInsertdisc = true;
			if (gameEnded (currentRow,col)){
				gameOver();
				return;

			}

			isPlayerOneTurn =!isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn? Player_One : Player_Two);
		});
       translateTransition.play();

	}

	private boolean gameEnded(int row, int col){

		List<Point2D> verticalPoints =IntStream.rangeClosed(row-3,row+3)
				.mapToObj(r-> new Point2D(r,col))
				.collect(Collectors.toList());
		List<Point2D> horizontalPoints =IntStream.rangeClosed(col -3,col +3)
				.mapToObj(C-> new Point2D(row,C))
				.collect(Collectors.toList());
		Point2D startPoint1 = new Point2D(row -3,col+3);
		List<Point2D> diagonalonepoints = IntStream.rangeClosed(0,6)
				.mapToObj(i-> startPoint1.add(i,-i))
				.collect(Collectors.toList());
		Point2D startPoint2 = new Point2D(row -3,col-3);
		List<Point2D> diagonaltwopoints = IntStream.rangeClosed(0,6)
				.mapToObj(i-> startPoint1.add(i,+i))
				.collect(Collectors.toList());

		boolean isEnded = checkCombination(verticalPoints) || checkCombination(horizontalPoints)
				|| checkCombination(diagonalonepoints) ||checkCombination(diagonaltwopoints);

		return isEnded;

	}

	private boolean checkCombination(List<Point2D> Points) {
		int chain = 0;
		for (Point2D points: Points) {
			int rowIndexofArray = (int) points.getX();
			int colIndexofArray = (int) points.getY();
			Disc disc = discisPresent(rowIndexofArray,colIndexofArray);
			if (disc!= null && disc.isPlayerOnemove== isPlayerOneTurn){
				chain++;
				if (chain==4){
					return true;
				}

			}else{
				chain=0;

			}

		}
		return false;

	}

	private Disc discisPresent(int row,int col){
		if (row >=ROW || row<0|| col>= COLUMNS || col<0)
         return null;
		return insertedDiscarray[row][col];
	}

	private void gameOver() {
		String winner = isPlayerOneTurn ? Player_One : Player_Two;
		System.out.println("Winner is " + winner);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("ConnectFour");
		alert.setHeaderText("The Winner is " + winner);
		alert.setContentText("Want to Play Again");
		ButtonType yesBtn = new ButtonType("Yes");
		ButtonType noBtn = new ButtonType("No,Exit");
		alert.getButtonTypes().addAll(yesBtn, noBtn);

		Platform.runLater(() -> {
			Optional<ButtonType> buttoncliked = alert.showAndWait();
			if (buttoncliked.isPresent() && buttoncliked.get() == yesBtn) {
				resetGame();
			} else {
				Platform.exit();
				System.exit(0);
			}

		});
	}



	public void resetGame() {
		insertedDiscsPane.getChildren().clear();
		for (int row=0; row < insertedDiscarray.length;row++){
			for (int col =0; col < insertedDiscarray.length;col++){
				insertedDiscarray[row][col] = null;
			}
		}
		isPlayerOneTurn = true;
		playerNameLabel.setText(Player_One);

		createplayGround();
	}

	private static class Disc extends Circle{

		private final boolean isPlayerOnemove;
		public Disc(boolean isPlayerOnemove){
			this.isPlayerOnemove = isPlayerOnemove;
			setRadius(Circle_Diameter/2);
			setCenterX(Circle_Diameter/2);
			setCenterY(Circle_Diameter/2);

			setFill(isPlayerOnemove? Color.valueOf(disc_color1):Color.valueOf(disc_color2));

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}


}