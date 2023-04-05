package com.abhishek.gametwo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {

	private Controller controller;


	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("gaming.fxml"));
		GridPane rootGridPane = loader.load();

		controller = loader.getController();
		controller.createplayGround();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		Pane menupane = (Pane) rootGridPane.getChildren().get(0);
		menupane.getChildren().add(menuBar);

		Scene scene = new Scene(rootGridPane);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private MenuBar createMenu() {

		// File Menu
		Menu fileMenu = new Menu("File");

		MenuItem newGame = new MenuItem("New game");
		newGame.setOnAction(event -> controller.resetGame());

		MenuItem resetGame = new MenuItem("Reset game");
		resetGame.setOnAction(event -> controller.resetGame());


		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		MenuItem exitGame = new MenuItem("Exit game");
		exitGame.setOnAction(event -> Exitgame());
		fileMenu.getItems().addAll(newGame, resetGame, separatorMenuItem, exitGame);

		Menu HelpMenu = new Menu("Help");
		MenuItem aboutGame = new MenuItem("About game");
		aboutGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				aboutconnect4game();
			}
		});
		SeparatorMenuItem separator = new SeparatorMenuItem();
		MenuItem aboutme = new MenuItem("About Developer");
		aboutme.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Aboutme();
			}
		});
		HelpMenu.getItems().addAll(aboutGame, separator, aboutme);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, HelpMenu);
		return menuBar;
	}

	private void Aboutme() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About The Developer");
		alert.setHeaderText("Deepanshu Kaushik");
		alert.setContentText("I love to Play Games and Creating New Game." +
				" I hope you liked Connect Four Game " +
				"and now Enjoy the Game");
		alert.show();
	}

	private void aboutconnect4game() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Connect Four Game");
		alert.setHeaderText("How To Play");
		alert.setContentText("Connect Four (also known as Connect 4," +
				" Four Up, Plot Four, Find Four, Captain's Mistress," +
				" Four in a Row, Drop Four, and Gravitrips in the Soviet Union)" +
				" is a two-player connection board game, " +
				"in which the players choose a color and then " +
				"take turns dropping colored tokens into a seven-column," +
				" six-row vertically suspended grid.");
		alert.show();
	}

	private void Exitgame() {
		Platform.exit();
		System.exit(0);
	}

	private void resetGame() {
	}


	public static void main(String[] args) {
		launch(args);
	}
}
