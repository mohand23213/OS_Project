package application;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private ArrayList<ProcessControlBlock> readyPCBList = new ArrayList<>();
    private ArrayList<ProcessControlBlock> jobPCBList = new ArrayList<>();
    private Button startButton;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the UI components
            BorderPane root = createRootLayout();
            HBox header = createHeader();
            VBox centerContent = createCenterContent();
            HBox bottomButtons = createBottomButtons();

            // Add components to the BorderPane
            root.setTop(header);
            root.setCenter(centerContent);
            root.setBottom(bottomButtons);

            // Set scene and stage properties
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Load Files");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BorderPane createRootLayout() {
        BorderPane root = new BorderPane();
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE,
                new Stop(0.5, Color.BLUE),
                new Stop(1, Color.WHITE)
        );
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, null)));
        return root;
    }

    private HBox createHeader() {
        Text compilerText = new Text("Compiler");
        compilerText.setFill(Color.WHITE);
        compilerText.setFont(Font.font("Arial", FontWeight.BOLD, 42));

        HBox header = new HBox(compilerText);
        header.setPrefHeight(200);
        header.setAlignment(Pos.CENTER);
        return header;
    }

    private VBox createCenterContent() {
        Text title = new Text("Load Files");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 42));

        Button loadReadyButton = createLoadButton("ready.txt");
        Button loadJobButton = createLoadButton("job.txt");
        Button clear =new  Button("clear Data");
        clear.setStyle("-fx-background-color: grey; -fx-text-fill: white; -fx-font-size: 24px;");
        HBox row=new HBox();
        row.setPrefWidth(200);
        HBox row1=new HBox();
        row1.setPrefWidth(200);
        loadJobButton.setOnAction(e -> {
            jobPCBList =new FileHandler().loadFile();
            if(!jobPCBList.isEmpty())loadJobButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 24px;");
            checkLoaded();
        });
        loadReadyButton.setOnAction(e -> {
            readyPCBList =new FileHandler().loadFile();
            if(!readyPCBList.isEmpty())loadReadyButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 24px;");
            checkLoaded();
        });
        clear.setOnAction(e->{
        	loadReadyButton.setStyle("-fx-background-color: grey; -fx-text-fill: white; -fx-font-size: 24px;");
        	loadJobButton.setStyle("-fx-background-color: grey; -fx-text-fill: white; -fx-font-size: 24px;");
        	jobPCBList.clear();
        	readyPCBList.clear();
        	startButton.setVisible(false);
        	
        });
        HBox buttonRow = new HBox(loadReadyButton, row, loadJobButton,row1,clear);
        buttonRow.setAlignment(Pos.CENTER);

        VBox content = new VBox(50, title, buttonRow);
        content.setStyle("-fx-padding: 50; -fx-alignment: center;");
        return content;
    }

    private Button createLoadButton(String fileName) {
        Button button = new Button("Load " + fileName);
        button.setStyle("-fx-background-color: grey; -fx-text-fill: white; -fx-font-size: 24px;");
        button.setOnAction(e -> {
            // Handle load button click logic for specific file
            // (e.g., call FileHandler to load processes)
            checkLoaded();
        });
        return button;
    }

    private HBox createBottomButtons() {
        startButton = new Button("Start Simulation");
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 24px;");
        startButton.setVisible(false);
        startButton.setOnAction(e -> {
        	organizeBaseLimit(readyPCBList);
        	new memorySimulaton(new ArrayList<>(Arrays.asList(copy(readyPCBList), copy(jobPCBList)))).runSimulation();
        });

        HBox buttons = new HBox(startButton);
        buttons.setPrefHeight(200);
        buttons.setAlignment(Pos.CENTER);
        return buttons;
    }

    private void checkLoaded() {
        if (!jobPCBList.isEmpty() || !readyPCBList.isEmpty()) {
            startButton.setVisible(true);
        }
    }
    private ArrayList<ProcessControlBlock> copy(ArrayList<ProcessControlBlock> originalList) {
        ArrayList<ProcessControlBlock> copiedList = new ArrayList<>();
        for (ProcessControlBlock originalProcessControlBlock : originalList) {
            // Create a new ProcessControlBlock instance with the same attributes as the original ProcessControlBlock
            ProcessControlBlock copiedProcessControlBlock = new ProcessControlBlock(originalProcessControlBlock);
            // Add the copied ProcessControlBlock to the new list
            copiedList.add(copiedProcessControlBlock);
        }
        return copiedList;
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void organizeBaseLimit(ArrayList<ProcessControlBlock> activeProcesses) {
        int base = 0;
        for (ProcessControlBlock process : activeProcesses) {
            process.base = base;
            process.limit = process.base + process.size-1;
            base = process.limit+1;
        }
        
        // Create a new Step representing the initial Step
    }
}
