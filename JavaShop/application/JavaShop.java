package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class JavaShop extends Application {
	//Declare variables with class scope
	ListView lvMenu, lvOrder;

	Label lblStatus, lblProgress, lblTotal, lblTotalPrice, lblDone;                                                                                 ;
	Button btnAddCoffee, btnPlaceOrder, btnClearAll, btnClearCoffee;
	Double totalPrice;
	ProgressBar progBar;
	Task<Void> task;

	// menu Strings and drink prices 
	HashMap<String, Double> hmDrinks = new HashMap<String, Double>();

	//DecimalFormat Object found on java2blog.com when I looked up how to format Doubles for pricing
	//https://java2blog.com/format-double-to-2-decimal-places-java/
	DecimalFormat df = new DecimalFormat("#0.00");

	//Constructor
	public JavaShop() {
		totalPrice = 0.0;

		lvMenu = new ListView();
		lvOrder = new ListView();

		lblStatus = new Label("");
		lblProgress = new Label();
		lblTotal = new Label("Total:");
		//First formatting the Double, then converting the new format to String, since this method only works on Doubles
		lblTotalPrice = new Label("€" + df.format(totalPrice).toString());
		btnPlaceOrder = new Button("Place Order");
		btnAddCoffee = new Button("Add Drink");
		btnClearAll = new Button("Clear All");
		btnClearCoffee = new Button("Clear Drink");
		
		progBar = new ProgressBar(0);
	}
	//Read menu and populate lvMenu with coffeemenu.csv
	private void readMenu(String menu) {
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(menu));
			while((line = br.readLine()) != null) {
				String[] menuItems = new String[3];
				menuItems = line.split(",");
				for (int i=0; i<menuItems.length; i++) {
					lvMenu.getItems().add(menuItems[i]); 

				}
				//Populating HashMap with name-price pairs to be able to access prices per drink
				
				String name[] = line.split("-");
				for (int i=0; i<=name.length; i++) {

					//I found the Double.valueOf() method on geeksforgeeks.org, a site I use a lot to help with syntax/
					//methods. I was having trouble filling the hashmap with String and Double values because the array
					//created from the csv is just Strings. I would keep the price as a String if it was just there for appearances, 
					//but need it to be a number value so I can add the total price.
					//I used trim because the values have whitespace which would probably add complications when converting to Doubles

					//So with this, the HashMap is populated with name-price pairs, so prices are accessible
					//There seems like I could probably find a better way to do this, and I was going to try just hard coding the
					//HashMap values but that wouldn't be efficient if ever the menu changed
					hmDrinks.put(name[0].trim(), Double.valueOf(name[1].trim()));
				}
			}
			br.close();
		}
		//Just in case csv not found
		catch(IOException io) {
			System.out.println(menu + " file not found.");
		}
	}//end readMenu method

	private void updateTotalPrice() {
		//I found the following idea from StackOverflow. I wanted to find out how to access a value from a HashMap
		//if the key simply contained a certain String, rather than if the key EQUALS that String. This is because
		//as it stands, my menu items are entirely one String, for better or worse...
		//So this String of course won't have a value in the HashMap, since the HashMap separates that into a String:Double pair.
		//So I wanted to access the prices of drinks from the HashMap if the entire selected menu item contained the String key from the HashMap.
		//https://stackoverflow.com/questions/55030393/if-hashmap-containskeyany-part-of-a-string

		//I realise this isn't a great way to do things because there could be a case where two menu items contain the same String, for example
		//poses a risk to the way I am going about this. 

		Double price;
		String drink = lvMenu.getSelectionModel().getSelectedItem().toString();
		
		for (String drinkName:hmDrinks.keySet()) {
			
			if (drink.contains(drinkName)) {
				//store it in a Double variable, and add it to the totalPrice

				price = hmDrinks.get(drinkName);

				totalPrice+=price;

				lblTotalPrice.setText("€" + df.format(totalPrice).toString());
			}

		}

	}

	
	private void enableAddDrink() {
		String selectedDrink = lvMenu.getSelectionModel().getSelectedItem().toString();

		//if any item is selected, user can add drink
		if (selectedDrink!=null) {
			btnAddCoffee.setDisable(false);
		}

	}//end enalbeAddDrink()

	private void addToOrder() {
		//Store the user's selected drink in a String, add the String to the order ListView
		String selectedDrink = lvMenu.getSelectionModel().getSelectedItem().toString();
		lvOrder.getItems().add(selectedDrink);
		//Enable Buttons since user has an order now
		btnClearAll.setDisable(false);
		btnPlaceOrder.setDisable(false);
		//Update price and give user feedback on actions
		updateTotalPrice();
		lblStatus.setText("Added to order!");
	

	}//end addToOrder

	private void placeOrder() {
		//Alert confirming payment: 
		Alert alertConfirm = new Alert(AlertType.CONFIRMATION);

		alertConfirm.setTitle("Confirm Order");
		alertConfirm.setHeaderText("Confirm to Pay");
		alertConfirm.setContentText("Order total: €" + df.format(totalPrice).toString());

		//Changing Button text on the Alerts, reusing this method from BookFace I had found on StackOverflow	
		Button btnConfirm = (Button)(alertConfirm.getDialogPane().lookupButton(ButtonType.OK));
		btnConfirm.setText("Confirm Payment");
		
		btnConfirm.setOnAction(click -> {
			//Trying to get the status to go blank again while the  is cooking, since the user knows their payment has been approved
			//and just want to make the app look more dynamic but not able to figure it out at the moment unfortunately
			lblStatus.setText("Payment approved.");
			//The ProgressBar update:
			showOrderStatus();
		});
		alertConfirm.showAndWait();
	}//end placeOrder()
	private void clearDrink() {

		String selectedDrink = lvOrder.getSelectionModel().getSelectedItem().toString();
		try {
			lvOrder.getItems().remove(selectedDrink);
			lblStatus.setText("Drink cleared from order.");

			Double price;

			//Updating price when drink is removed:
			//Not exactly happy about having to reuse lines from the updateTotalPrice() method and would rather the one method could take care of
			//increasing and decreasing price, but not sure how to implement that for now 
			for (String drinkName:hmDrinks.keySet()) {

				if (selectedDrink.contains(drinkName)) {

					price = hmDrinks.get(drinkName);

					//decrease price if removed
					totalPrice-=price;

					lblTotalPrice.setText("€" + df.format(totalPrice).toString());
				}	
			}
			disableButtons();

		}
		catch(Exception e) {
			System.out.println("No drink selected");
		}
	}//end clearDrink()

	private void showOrderStatus() {
		//Show ProgressBar and its Label
		lblProgress.setVisible(true);
		progBar.setVisible(true);
		//Don't allow for actions while order is cooking
		btnAddCoffee.setDisable(true);
		btnPlaceOrder.setDisable(true);
		btnClearCoffee.setDisable(true);
		btnClearAll.setDisable(true);
		
		lblProgress.setText("Preparing your order...");
		//Same format as our ProgInd Task
		task = new Task<Void>() {
			public Void call() {
				
				lvMenu.setDisable(true);
				lvOrder.setDisable(true);
				final long max = 100000000;
				for (long i=1; i<=max; i++) {
					if (isCancelled()) {
						updateProgress(0, max);
						break;
					}

					//Dynamically changing the ProgressBar colour, this took a long time to figure out until I realised
					//the updates have to take place in this loop if I want them to be applied while the Task is running. I'm not
					//sure if there's a better way for this
					if (i < 30000000) {
						progBar.setStyle("-fx-accent: #875008;");

					}
					else if (i < 60000000) {
						progBar.setStyle("-fx-accent: #B28C4D;");

					}

					else if (i < 80000000) {
						progBar.setStyle("-fx-accent: #DDC792;");
					}
					else if (i == 100000000) {
						//Thread.sleep() has to be in try-catch
						try {
							//Want a brief pause before changes are made, to look more realistic
							Thread.sleep(500);

							//Found the following tip on StackOverflow: https://stackoverflow.com/questions/47150884/label-not-being-automatically-updated-with-thread-in-javafx

							//Most notable here was this user's point that all UI changes that happen (I presume without user input first) need to be in Platform.runLater().
							//I think this would be since if you make try to make updates after calling a Task, they will still occur even
							//while the Task is still running. And certain changes can't be made within the Task method, like updating Label text.
							//For that to work it has to be in the following method.
							//So this method seems to take a Runnable object as its parameter, and the following lines create that Runnable in the same instance.
							//In doing so, overriding the built in run() method with the events that I want to take place
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									//Taking away the progress bar and its Label, clearing the selected drink and updating the status Label
									lblProgress.setText("Done!");

									lblStatus.setText("Please collect your order.");
									lvOrder.getItems().clear();
									resetTotalPrice();
									disableButtons();
								}
							});
							Thread.sleep(1800);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									//Waiting a bit then clearing the status for the next order
									//This didn't work unless I put it in a new runLater()... I imagine there's a better way 

									lblStatus.setText("");
									progBar.setVisible(false);
									lblProgress.setVisible(false);
									lvMenu.setDisable(false);
									lvOrder.setDisable(false);
								}
							});
						}
						catch(Exception e) {
							System.out.println(e.toString());
						}
					}
					updateProgress(i, max);

				}
				return null;
			}
		};

		//bind the Task's progress to the ProgressBar and start!
		progBar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();	 


	}//end showOrderStatus();

	private void resetTotalPrice() {
		//Update the double itself and replace the Label
		totalPrice = 0.0;
		lblTotalPrice.setText("€" +df.format(totalPrice).toString());
	}//end resetTotalPrice()

	private void disableButtons() {
		//If the price is 0, either no drinks have been added to the order or 
		//the user has cleared their order -- either way, disable Buttons that only result in action if
		//user has drinks in their cart
		if (Math.round(totalPrice) ==0) {
			btnClearCoffee.setDisable(true);

			btnClearAll.setDisable(true);
			btnPlaceOrder.setDisable(true);
		}
	}//end disableButtons
	private void clearAllDrinks() {
		//Ask user to confirm any major event, clearing all drinks is more consequential than just clearing one
		//so making users confirm to clear them all
		Alert alertClearOrder = new Alert(AlertType.CONFIRMATION);
		alertClearOrder.setTitle("Clear Order");
		alertClearOrder.setHeaderText("Are you sure you want to clear your order?");
		alertClearOrder.setContentText("This action cannot be undone.");

		
		//Renaming Buttons for clarity to users
		Button btnBack = (Button) alertClearOrder.getDialogPane().lookupButton(ButtonType.CANCEL);
		btnBack.setText("Take me back");
		Button btnClear = (Button) alertClearOrder.getDialogPane().lookupButton(ButtonType.OK);
		btnClear.setText("Yes, clear my order");
		//only proceed with the clearing when the Alert is confirmed
		btnClear.setOnAction(click -> {
			lvOrder.getItems().clear();
			lblStatus.setText("Order cleared.");
			resetTotalPrice();
			disableButtons();
		});

		alertClearOrder.showAndWait();


	}//end clearAllDrinks()

	//Event handling
	public void init() {

		lvMenu.setOnMouseClicked(click -> {
			//Just in case user can some how click Button when disabled..
			try {
				if (lvMenu.getSelectionModel().getSelectedItem().toString()==null) {
					btnAddCoffee.setDisable(true);

				}
				else {
					btnAddCoffee.setDisable(false);
				}	
			}
			catch(Exception e) {
				System.out.println("No drink selected");
			}


		});

		btnAddCoffee.setOnAction(click -> addToOrder());

		lvOrder.setOnMouseClicked(click -> {
			try {
				if (lvOrder.getSelectionModel().getSelectedItem().toString()==null) {
					btnClearCoffee.setDisable(true);
				}
				else {
					btnClearCoffee.setDisable(false);
				}	
			}
			catch(Exception e) {
				System.out.println("No drink selected");
			}

		});

		btnClearCoffee.setOnMouseClicked(click -> clearDrink());

		btnClearAll.setOnMouseClicked(click -> clearAllDrinks());

		btnPlaceOrder.setOnMouseClicked(click -> placeOrder());

	}

	@Override
	public void start(Stage primaryStage) {

		//Our containers
		primaryStage.setHeight(500);
		primaryStage.setWidth(600);
		primaryStage.setTitle("Java Shop!");
		BorderPane bpMain = new BorderPane();
		VBox vbMenu = new VBox();
		VBox vbOrder = new VBox();
		HBox hbMenuButtons = new HBox();
		HBox hbOrderButtons = new HBox();
		VBox vbProg = new VBox();
		HBox hbTotal = new HBox();

		//Component sizing and alignment
		lvOrder.maxHeightProperty().bind(vbOrder.heightProperty().divide(2));
		lblTotal.setAlignment(Pos.CENTER_RIGHT);


		//Some styling for specific components
		btnClearCoffee.setStyle("-fx-background-color: transparent;" + 
		"-fx-text-fill: #875008;" +
		"-fx-underline: true;");
		btnClearAll.setStyle("-fx-background-color: transparent;" + 
				"-fx-text-fill: #875008;" +
				"-fx-underline: true;");
		btnPlaceOrder.setMinWidth(80);
		//Spacing
		vbProg.setSpacing(10);
		vbProg.setAlignment(Pos.CENTER_LEFT);
		vbMenu.setPadding(new Insets(10));
		vbOrder.setPadding(new Insets(10));
		bpMain.setPadding(new Insets(15));
		vbMenu.setSpacing(10);
		vbOrder.setSpacing(10);

		hbTotal.setSpacing(primaryStage.getWidth()/3);

		hbMenuButtons.setSpacing(hbTotal.getSpacing()/9);
		hbOrderButtons.setSpacing(5);
		//Want order Buttons aligned with order ListView
		hbOrderButtons.setTranslateX(-10);
		btnPlaceOrder.setTranslateX(25);

		//All Buttons set disabled until a drink is either selected or added

		btnAddCoffee.setDisable(true);


		if (Math.round(totalPrice) == 0 || lvOrder.getItems() == null) {
			btnClearCoffee.setDisable(true);
			btnClearAll.setDisable(true);
			btnPlaceOrder.setDisable(true);
		}
		else {
			btnClearCoffee.setDisable(false);
			btnClearAll.setDisable(false);
			btnPlaceOrder.setDisable(false);
		}

		//Add components to containers

		progBar.setVisible(false);
		vbProg.getChildren().addAll(lblProgress, progBar);
		hbTotal.getChildren().addAll(lblTotal, lblTotalPrice);
		hbMenuButtons.getChildren().addAll(btnAddCoffee);
		hbOrderButtons.getChildren().addAll(btnClearAll, btnClearCoffee, btnPlaceOrder);
		vbMenu.getChildren().addAll(lvMenu, hbMenuButtons, lblStatus);
		vbOrder.getChildren().addAll(lvOrder, hbTotal, hbOrderButtons);

		//Add containers to main
		bpMain.setTop(vbProg);
		bpMain.setLeft(vbMenu);
		bpMain.setRight(vbOrder);

		Scene s = new Scene(bpMain);

		primaryStage.setScene(s);
		
		//Try-catches for the add-on documents: menu, app icon, and stylesheet
		try {
			readMenu("coffeemenu.csv");
		}
		catch(Exception e) {
			System.out.println("Menu not found!");
		}

		try {
			Image appIcon = new Image("javashop-logo.png");
			primaryStage.getIcons().add(appIcon);
			}
		catch(Exception e) {
			System.out.println("unable to locate app logo");
		}
		
		
		try {
			s.getStylesheets().add("application.css");
		}
		catch(Exception e) {
			System.out.println("Stylesheet missing");
		}
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}                                 
