package application;
import java.util.ArrayList;
import java.util.Stack;

import javafx.scene.text.FontWeight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.geometry.Insets;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public class simulationPage {
	Text title;
	Stage stage=new Stage();
	int lengthOfScenes;
	Scene scene;
	
	
	ArrayList<Scene> scenes;
	simulationPage(ArrayList<Step> s){
		//add the last Step whoch says final Step
		s=AddEndToStates(s);
		lengthOfScenes=s.size();
		//load Scenes for all Steps
		buildScenes(s);
		//load the first Scene
        scene = scenes.get(0);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.show();
	}
	void buildScenes(ArrayList<Step> steps) {
		scenes=new ArrayList<Scene>();
		for(Step step:steps) {
			Scene scene=new Scene(Body(step,steps.indexOf(step)),1200,800);
			scenes.add(scene);
		}
	}
	HBox middleTabels(Step step) {
		HBox row=new HBox();
		///include all the table of contents in row and then print it on screen
		row.getChildren().addAll(TableOfContent("Jobs",step.jobs),TableOfContent("Memory",step.memory),TableOfContent("Finished",step.FinishedPCB));
		row.setPrefHeight(650);
		return row;
	}	
	public StackPane Body(Step step,int index) {
		StackPane stack=new StackPane();
		VBox root=new VBox();
		Text time=time(step.time);
		Text explanation=explanation(step.explanation);
		Button forward=createForwardButton(stage,index);
		Button backward=createGoBackButton(stage,index);
		//set gradient color
		LinearGradient gradient = new LinearGradient(
	            0, 0, 1, 0, true,  // Start (0,0) to End (1,0)
	            CycleMethod.NO_CYCLE,
	            new Stop(0.5, Color.BLUE),
	            new Stop(1, Color.WHITE),
	            new Stop(1, Color.WHITE)
	        );
		root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, null)));
		root.getChildren().addAll(header(step.explanation),middleTabels(step));
		stack.setAlignment(forward,Pos.CENTER_RIGHT);
		stack.setAlignment(backward,Pos.CENTER_LEFT);
		stack.setAlignment(time,Pos.BOTTOM_RIGHT);
		stack.setAlignment(explanation,Pos.BOTTOM_CENTER);
		stack.setMargin(time, new Insets(10));
		stack.setMargin(explanation, new Insets(10));
		stack.getChildren().addAll(root,time,explanation,forward,backward);
		return stack;
	}
	private Text explanation(String explanation) {


        Text text = new Text("Event:"+explanation);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        text.setFill(Color.WHITE);
        return text;
	}
	ArrayList<Step> AddEndToStates(ArrayList<Step> steps) {
	Step step=new Step(new ArrayList(), new ArrayList(), new ArrayList(), steps.get(steps.size()-1).time, "");
	step.memory=steps.get(steps.size()-1).memory;
	step.FinishedPCB=steps.get(steps.size()-1).FinishedPCB;
	step.explanation="Simulation Ended!";
	steps.add(step);
	return steps;
}	
	public VBox header(String explanation) {
		VBox column=new VBox();
		
        // Create the Text
        Text Simulation = new Text("Simulation: MVT-First_Fit");
        Simulation.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        Simulation.setFill(Color.WHITE);
        // Create the HBox
        
        // Add the Text to the HBox
        column.getChildren().addAll(Simulation);
        column.setAlignment(Pos.CENTER);
        column.setPrefHeight(100);
        return column;
    }
	public Button createForwardButton(Stage stage,int index) {
	    Button forward = new Button("");
	    // Define the right arrow image
	    forward.setVisible(lengthOfScenes!=(index+1));
	    Image rightArrow = new Image(getClass().getResourceAsStream("/right-arrow.png"));
	    ImageView imageView = new ImageView(rightArrow);

	    // Set properties for the forward button
	    forward.setGraphic(imageView);
	    imageView.setFitWidth(50);  // Set the width to 50 pixels
	    imageView.setFitHeight(50); // Set the height to 50 pixels
	    forward.setPrefWidth(50);
	    forward.setPrefHeight(50);
	    forward.setBackground(new Background(new BackgroundFill(
	        Color.TRANSPARENT, CornerRadii.EMPTY, null
	    )));

	    // Set button style
	    forward.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24;");

	    // Set action for forward button
	    forward.setOnAction(e -> {
	        Scene currentScene = stage.getScene();
	        int currentIndex = scenes.indexOf(currentScene);
	        if (currentIndex < scenes.size() - 1) {
	            Scene nextScene = scenes.get(currentIndex + 1);
	            stage.setScene(nextScene);
	            stage.show();
	        }
	    });

	    return forward;
	}
	public Button createGoBackButton(Stage stage, int index) {
    Button goBack = new Button("");
    goBack.setVisible(index!=0);
    // Define the left arrow image
    Image leftArrow = new Image(getClass().getResourceAsStream("/left-arrow.png"));
    ImageView leftArrowImage = new ImageView(leftArrow);

    // Set properties for the backward button
    goBack.setGraphic(leftArrowImage);
    leftArrowImage.setFitWidth(50);  // Set the width to 50 pixels
    leftArrowImage.setFitHeight(50); // Set the height to 50 pixels
    goBack.setPrefWidth(50);
    goBack.setPrefHeight(50);
    goBack.setBackground(new Background(new BackgroundFill(
        Color.TRANSPARENT, CornerRadii.EMPTY, null
    )));

    // Set button style
    goBack.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24;");

    // Set action for goBack button
    goBack.setOnAction(e -> {
        if (index > 0) {
            Scene previousScene = scenes.get(index - 1);
            stage.setScene(previousScene);
            stage.show();
        }
    });

    return goBack;
}
	public HBox BottomLayer(int time,int index) {
		//these two buttons work to go for the next Step or previus step
		Button goBack = new Button("");
        Button Forward = new Button("");
        
        //define the right arrow image
        Image rightArrow = new Image(getClass().getResourceAsStream("/right-arrow.png"));
        ImageView imageView = new ImageView(rightArrow);

        //define the left arrow image
        Image LeftArrow = new Image(getClass().getResourceAsStream("/left-arrow.png"));
        ImageView leftArrowImage = new ImageView(LeftArrow);
        
        //set properties for forward button
        Forward.setGraphic(imageView);
        imageView.setFitWidth(50);  // Set the width to 50 pixels
        imageView.setFitHeight(50);
        Forward.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, null
            )));
        
        //set properites for backward button
        goBack.setGraphic(leftArrowImage);
        leftArrowImage.setFitWidth(50);  // Set the width to 50 pixels
        leftArrowImage.setFitHeight(50);
        goBack.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, null
            )));
        
        ///actions
        goBack.setOnAction(E->{
        	scene = scenes.get(scenes.indexOf(scene)-1);
        	stage.setScene(scene);
        	stage.show();
        });
        Forward.setOnAction(E->{
        	scene = scenes.get(scenes.indexOf(scene)+1);
        	stage.setScene(scene);
        	stage.show();
        });

        // Set button colors
        goBack.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24;");
        Forward.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24;");

        // Create and configure text
        Text Time = new Text("Time:"+time);
        Time.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        Time.setFill(javafx.scene.paint.Color.WHITE);

        // Create HBox for each button
        HBox goBackBox = new HBox(goBack);
        HBox ForwardButtonBox = new HBox(Forward);

        // Set HBox width and alignment
        goBackBox.setPrefWidth(600); 
        ForwardButtonBox.setPrefWidth(600); // Divide the width equally for two buttons
        goBackBox.setAlignment(Pos.CENTER_LEFT);
        ForwardButtonBox.setAlignment(Pos.CENTER_RIGHT);

        // Create HBox to contain all button HBoxes and the middle text
        HBox row = new HBox(10, goBackBox, ForwardButtonBox);
        row.setPrefWidth(100);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10));
        goBack.setVisible(index!=0);
        Forward.setVisible(index!=lengthOfScenes-1);
        return row;
	}
	public BorderPane returnSpaceShape(Space block) {
		//we used BorderPane so we can devide the content
        BorderPane SpaceContent = new BorderPane();
        Text id = new Text(block.spaceType);
        id.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        SpaceContent.setCenter(id);
        
        Text base = new Text(block.base+"");
        base.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        SpaceContent.setTop(base);

        Text limit = new Text(block.limit+"");
        limit.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        SpaceContent.setBottom(limit);
        
        // Set the background color to grey
        //if the hole is OS then it has a color if not OS then it's has other color
        	if(((Space)block).spaceType.contains("Hole")) {
        		//
                SpaceContent.setStyle("-fx-background-color: purple;");
        	}
        	else {
                SpaceContent.setStyle("-fx-background-color: red;");
        	}
        
        // Set the preferred height and width
        SpaceContent.setMaxWidth(200);
        SpaceContent.setPrefHeight(((block.limit-block.base)/1536)*400);
        // Set padding for the BorderPane
        return SpaceContent;		
}
	public ScrollPane TableOfContent(String name,ArrayList list) {
		   VBox v = new VBox();
	        v.setAlignment(Pos.CENTER);
	        Text title = new Text(name+"Queue");
	        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
	        // Create a VBox for the list items with alignment at the center
	        VBox itemsBox = new VBox();

	        // Add each ProcessControlBlock container to the itemsBox
	        for (var item : list) {
	        	BorderPane pane;
	        	if(item instanceof ProcessControlBlock) {
	        		ProcessControlBlock x=(ProcessControlBlock)item;
	        		pane = returnProcessShape(x);
		        	pane.setPrefHeight(((x.size)/2048)*600);//make the size of the element dynamic and depend on the actualy size
	        	}
	        	else {
	        		Space x=(Space)item;
	        		pane = returnSpaceShape(x);
	        		pane.setPrefHeight(((x.limit-x.base)/2048)*600);
	        	}
	        	itemsBox.getChildren().add(pane);
	            VBox.setMargin(pane, new Insets(0, 0, 10, 0));
	        }
	        v.getChildren().addAll(title, itemsBox);
	        //we used Scroll Pane to avoid the height exceeded
			ScrollPane scrollPane = new ScrollPane(v);
			v.setPrefWidth(400);
	        itemsBox.setAlignment(Pos.CENTER);
	        return scrollPane;
	}
	public BorderPane returnProcessShape(ProcessControlBlock block) {
        BorderPane borderPane = new BorderPane();
        Text id = new Text("id:"+block.processId);
        id.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        borderPane.setCenter(id);
        
        Text base = new Text(block.base+"");
        base.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        borderPane.setTop(base);

        Text limit = new Text(block.limit+"");
        limit.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        borderPane.setBottom(limit);
        	HBox bottomRow=new HBox();
        	HBox upperRow=new HBox();
        	Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
        	Region spacer1 = new Region();
            HBox.setHgrow(spacer1, Priority.ALWAYS);
            Text remaningTime = new Text("RemainingTime "+(((ProcessControlBlock)block).timeInMemory-((ProcessControlBlock)block).counter));
            Text size= new Text("Size "+((ProcessControlBlock)block).size);
            base.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        borderPane.setRight(remaningTime);
        upperRow.getChildren().addAll(base,spacer1,size);
        bottomRow.getChildren().addAll(limit,spacer,remaningTime);
        borderPane.setBottom(bottomRow);
        borderPane.setTop(upperRow);
        borderPane.setStyle("-fx-background-color: green;");
        // Set the preferred height and width
        borderPane.setMaxWidth(200);
        borderPane.setPrefHeight(((block.limit-block.base)/1536)*400);
        // Set padding for the BorderPane
        return borderPane;	
    }
	public Text time(int time) {
		

        Text timeText = new Text("Time:"+time);
        timeText.setFont(new Font(24));
        timeText.setFill(Color.BLACK);
		return timeText;
	}

	
}