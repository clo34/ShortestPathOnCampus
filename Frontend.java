import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * This class implements the FrontendInterface and provides methods to create controls for the frontend application.
 */
public class Frontend extends Application implements FrontendInterface{

    private BackendInterface backend;
    private ComboBox<String> startLocationComboBox;
    private ComboBox<String> endLocationComboBox;
    private ComboBox<String> viaLocationComboBox;
    private Stage updatedPathStage;

    public void start(Stage stage) {
        Pane root = new Pane();

        // Create an instance of the backend
        Backend backend = new Backend(new DijkstraGraph<>());

        // Set the backend instance
        setBackend(backend);

        //load files
        try {
            backend.loadGraphData("src/campus.dot");
            System.out.println("Done loading graph");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create all controls with the backend instance
        createAllControls(root);

        //set background color (not working...)
        Color backgroundColor = Color.web("#81c483");
        Color bgcolor = Color.rgb(129, 196, 131, 1.0);

        Scene scene = new Scene(root, 1000, 600);
        scene.setFill(bgcolor);


        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("P2: Prototype");
        stage.show();
    }

    /**
     * Creates all controls in the GUI.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createAllControls(Pane parent) {
        createShortestPathControls(parent);
        createPathListDisplay(parent);
        createAdditionalFeatureControls(parent);
        createAboutAndQuitControls(parent);
        createUpdatedPathControls(parent);
    }

    @Override
    public void setBackend(BackendInterface backend) {

        this.backend = backend;
    }

    /**
     * Creates the controls for the shortest path search.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createShortestPathControls(Pane parent) {
        // Create controls for shortest path search
        // Fetch the list of locations from the backend
        List<String> allLocations = backend.getListOfAllLocations();
        ObservableList<String> locations = FXCollections.observableArrayList(allLocations);

        startLocationComboBox = new ComboBox<>(locations);
        startLocationComboBox.setLayoutX(32);
        startLocationComboBox.setLayoutY(48);
        startLocationComboBox.setPromptText("Select start location");
        parent.getChildren().add(startLocationComboBox);

        endLocationComboBox = new ComboBox<>(locations);
        endLocationComboBox.setLayoutX(32);
        endLocationComboBox.setLayoutY(80);
        endLocationComboBox.setPromptText("Select end location");
        parent.getChildren().add(endLocationComboBox);

        Button findButton = new Button("Submit/Find Button");
        findButton.setLayoutX(32);
        findButton.setLayoutY(144);
        parent.getChildren().add(findButton);

        ListView<String> pathListView = new ListView<>();
        pathListView.setLayoutX(32);
        pathListView.setLayoutY(224);
        pathListView.setPrefSize(400, 200);
        parent.getChildren().add(pathListView);

        // Add action event handler for the search button
        findButton.setOnAction(event -> {
//            String startLocation = startLocationField.getText();
//            String endLocation = endLocationField.getText();
//
//            // Call backend method to find shortest path
//            List<String> shortestPath = backend.findShortestPath(startLocation, endLocation);
//
//            // Further actions with the shortest path (e.g., display on UI)
//            System.out.println("Shortest Path: " + shortestPath);
            String startLocation = startLocationComboBox.getValue();
            String endLocation = endLocationComboBox.getValue();

            // Call backend method to find shortest path
            List<String> shortestPath = backend.findShortestPath(startLocation, endLocation);

            // Further actions with the shortest path (e.g., display on UI)
            System.out.println("Shortest Path: " + shortestPath);
            // Display the shortest path
            pathListView.getItems().clear();
            if (!shortestPath.isEmpty()) {
                pathListView.getItems().addAll(shortestPath);
            } else {
                pathListView.getItems().add("No path found between the selected locations.");
            }
        });
    }



    private void displayShortestPath(List<String> shortestPath, Pane parent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Shortest Path:\n");
        for (String location : shortestPath) {
            sb.append("\t").append(location).append("\n");
        }
        Label shortestPathLabel = new Label(sb.toString());
        shortestPathLabel.setLayoutX(32);
        shortestPathLabel.setLayoutY(200);
        parent.getChildren().add(shortestPathLabel);
    }

    private void displayNoPathFound(Pane parent) {
        Label noPathLabel = new Label("No path found between the selected locations.");
        noPathLabel.setLayoutX(32);
        noPathLabel.setLayoutY(200);
        parent.getChildren().add(noPathLabel);
    }


    /**
     * Creates the controls for displaying the shortest path returned by the search.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createPathListDisplay(Pane parent) {
        // Create controls for displaying the shortest path
//        Label pathLabel = new Label("Shortest Path:");
//        pathLabel.setLayoutX(32);
//        pathLabel.setLayoutY(192);
//        parent.getChildren().add(pathLabel);
//
//        ListView<String> pathListView = new ListView<>();
//        pathListView.setLayoutX(32);
//        pathListView.setLayoutY(224);
//        pathListView.setPrefSize(400, 200);
//        parent.getChildren().add(pathListView);


    }
//    @Override
//    public void createPathListDisplay(Pane parent) {
//        // Create controls for displaying the shortest path
//        Label pathLabel = new Label("Results List:");
//        pathLabel.setLayoutX(32);
//        pathLabel.setLayoutY(192);
//        parent.getChildren().add(pathLabel);
//
//        ListView<String> pathListView = new ListView<>();
//        pathListView.setLayoutX(32);
//        pathListView.setLayoutY(224);
//        pathListView.setPrefSize(400, 200);
//        parent.getChildren().add(pathListView);
//    }

    /**
     * Creates controls for additional features in addition to the shortest path search.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createAdditionalFeatureControls(Pane parent) {
        createTravelTimesBox(parent);
        createOptionalLocationControls(parent);
    }

    /**
     * Creates the check box to add travel times in the result display.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createTravelTimesBox(Pane parent) {
        CheckBox travelTimesBox = new CheckBox("Show Walking Times");
        travelTimesBox.setLayoutX(32);
        travelTimesBox.setLayoutY(432);
        parent.getChildren().add(travelTimesBox);

        // Initialize the travel times stage outside the event handler
        Stage travelTimesStage = new Stage();
        // Set an initial title for the stage
        travelTimesStage.setTitle("Travel Times");

        travelTimesBox.setOnAction(event -> {
            if (travelTimesBox.isSelected()) {
                // Checkbox is checked, so display the walking times
                List<Double> travelTimes = backend.getTravelTimesOnPath(startLocationComboBox.getValue(), endLocationComboBox.getValue());

                // Display travel times in the stage
                displayTravelTimes(travelTimes, travelTimesStage);
            } else {
                // Checkbox is unchecked, so hide or close the travel times display
                if (travelTimesStage.isShowing()) {
                    travelTimesStage.hide(); // Hide the stage if it's currently showing
                }
                // Alternatively, you can close the stage if you don't need it anymore
                // travelTimesStage.close();
            }
        });

    }

    private void displayTravelTimes(List<Double> travelTimes, Stage travelTimesStage) {
        // Check if the stage is null or not showing
        if (travelTimesStage == null || !travelTimesStage.isShowing()) {
            // Create a new stage for displaying travel times
            travelTimesStage = new Stage();
            travelTimesStage.setTitle("Travel Times");
        }

        // Create a pane to hold the labels
        Pane travelTimesPane = new Pane();

        // Clear the existing content of the pane
        travelTimesPane.getChildren().clear();

        // Create labels to display the travel times
        Label titleLabel = new Label("Walking Times:");
        titleLabel.setLayoutX(20);
        titleLabel.setLayoutY(20);

        double totalTime = 0.0;
        StringBuilder travelTimesString = new StringBuilder();
        for (Double time : travelTimes) {
            travelTimesString.append(time).append(" seconds\n");
            totalTime += time;
        }

        travelTimesString.append("\nTotal Time: ").append(totalTime).append(" seconds\n");

        Label timesLabel = new Label(travelTimesString.toString());
        timesLabel.setLayoutX(20);
        timesLabel.setLayoutY(50);

        // Add labels to the pane
        travelTimesPane.getChildren().addAll(titleLabel, timesLabel);

        // Create a scene with the pane and set it in the stage
        Scene travelTimesScene = new Scene(travelTimesPane, 300, 200);
        travelTimesStage.setScene(travelTimesScene);

        // Show the travel times stage
        travelTimesStage.show();
    }

    /**
     * Creates the checkbox to show travel times for the updated path.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createUpdatedPathControls(Pane parent) {
        CheckBox updatedPathCheckBox = new CheckBox("Show Updated Path Travel Times");
        updatedPathCheckBox.setLayoutX(500);
        updatedPathCheckBox.setLayoutY(432);
        parent.getChildren().add(updatedPathCheckBox);

        // Add event handler for the checkbox
        updatedPathCheckBox.setOnAction(event -> {
            if (updatedPathCheckBox.isSelected()) {
                // Show travel times for updated path
                String startLocation = startLocationComboBox.getValue();
                String viaLocation = viaLocationComboBox.getValue();
                String endLocation = endLocationComboBox.getValue();
                List<Double> travelTimes = backend.getTravelTimesOnPathVia(startLocation, viaLocation, endLocation);
                displayTravelTimesAsPopup(travelTimes);
            } else {
                // Close the pop-up window if it's open
                if (updatedPathStage != null && updatedPathStage.isShowing()) {
                    updatedPathStage.close();
                }
            }
        });
    }

    private void displayTravelTimesAsPopup(List<Double> travelTimes) {
        // Create a pane to hold the labels
        Pane travelTimesPane = new Pane();

        // Create labels to display the travel times
        Label titleLabel = new Label("Travel Times:");
        titleLabel.setLayoutX(20);
        titleLabel.setLayoutY(20);

        double totalTime = 0.0;
        StringBuilder travelTimesString = new StringBuilder();
        for (Double time : travelTimes) {
            travelTimesString.append(time).append(" seconds\n");
            totalTime += time;
        }

        travelTimesString.append("\nTotal Time: ").append(totalTime).append(" seconds\n");

        Label timesLabel = new Label(travelTimesString.toString());
        timesLabel.setLayoutX(20);
        timesLabel.setLayoutY(50);

        // Add labels to the pane
        travelTimesPane.getChildren().addAll(titleLabel, timesLabel);

        // Create a scene with the pane
        Scene travelTimesScene = new Scene(travelTimesPane, 300, 200);

        // Create a new stage for the pop-up window
        updatedPathStage = new Stage();
        updatedPathStage.setTitle("Updated Path Travel Times");
        updatedPathStage.setScene(travelTimesScene);
        updatedPathStage.setScene(travelTimesScene);

        // Show the pop-up window
        updatedPathStage.show();
    }

    /**
     * Displays the travel times for the updated path.
     *
     * @param travelTimes the list of travel times
     * @param parent      the parent pane that contains all controls
     */
    private void displayTravelTimes(List<Double> travelTimes, Pane parent) {
        // Clear any existing travel times display
        clearTravelTimesDisplay(parent);

        // Display the travel times
        StringBuilder sb = new StringBuilder("Travel Times:\n");
        for (double time : travelTimes) {
            sb.append(time).append(" seconds\n");
        }
        Label travelTimesLabel = new Label(sb.toString());
        travelTimesLabel.setLayoutX(500);
        travelTimesLabel.setLayoutY(240);
        parent.getChildren().add(travelTimesLabel);
    }

    /**
     * Clears the travel times display.
     *
     * @param parent the parent pane that contains all controls
     */
    private void clearTravelTimesDisplay(Pane parent) {
        // Remove any existing travel times display
        parent.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().startsWith("Travel Times:"));
    }


    /**
     * Creates controls to allow users to add a third location for the path to go through.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createOptionalLocationControls(Pane parent) {
        Label locationLabel = new Label("Additional Intermediate Location (optional):");
        locationLabel.setLayoutX(500);
        locationLabel.setLayoutY(16);
        parent.getChildren().add(locationLabel);

        viaLocationComboBox = new ComboBox<>(); // Correctly initialize the instance variable
        viaLocationComboBox.setLayoutX(500);
        viaLocationComboBox.setLayoutY(48);
        viaLocationComboBox.setPromptText("Select Intermediate location");
        parent.getChildren().add(viaLocationComboBox);

        // Fetch the list of locations from the backend and add them to the ComboBox
        List<String> allLocations = backend.getListOfAllLocations();
        viaLocationComboBox.setItems(FXCollections.observableArrayList(allLocations));

        Button submitButton = new Button("Submit");
        submitButton.setLayoutX(500);
        submitButton.setLayoutY(80);
        parent.getChildren().add(submitButton);

        // Add action event handler for the submit button
        submitButton.setOnAction(event -> {
            // Retrieve the selected via location from the ComboBox
            String viaLocation = viaLocationComboBox.getValue();

            if (viaLocation != null) {
                // Call the backend method to find the shortest path with the via location included
                List<String> newPath = backend.findShortestPathVia(startLocationComboBox.getValue(), viaLocation, endLocationComboBox.getValue());

                // Update the UI to display the new path
                displayPath(newPath, parent);
            }
        });
    }

    private void displayPath(List<String> path, Pane parent) {
        // Clear any existing path display
        parent.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().startsWith("Path:"));

        if (!path.isEmpty()) {
            // Display the new path in the UI
            StringBuilder sb = new StringBuilder("Shortest Path:\n");
            for (String location : path) {
                sb.append(location).append("\n");
            }
            Label pathLabel = new Label(sb.toString());
            pathLabel.setLayoutX(500);
            pathLabel.setLayoutY(120);
            parent.getChildren().add(pathLabel);
        } else {
            // Display a message if no path is found
            Label noPathLabel = new Label("No path found with the selected via location.");
            noPathLabel.setLayoutX(500);
            noPathLabel.setLayoutY(120);
            parent.getChildren().add(noPathLabel);
        }
    }

    /**
     * Creates an about and quit button.
     *
     * @param parent the parent pane that contains all controls
     */
    @Override
    public void createAboutAndQuitControls(Pane parent) {
        Button about = new Button("About");
        about.setOnAction(e -> {
            // Create a new stage for the about window
            Stage aboutStage = new Stage();
            aboutStage.setTitle("About");

            // Create a label with wrapped text
            Label aboutLabel = new Label("      This application helps find the shortest path between two destinations on campus.");
            aboutLabel.setWrapText(true);

            // Create a pane to hold the label
            Pane aboutPane = new Pane();
            aboutPane.getChildren().add(aboutLabel);

            // Create a scene with the pane and set it in the stage
            Scene aboutScene = new Scene(aboutPane, 500, 100);
            aboutScene.setFill(Color.web("#81c483"));
            aboutStage.setScene(aboutScene);

            // Show the about stage
            aboutStage.show();
        });
        about.setLayoutX(32);
        about.setLayoutY(560);
        parent.getChildren().add(about);

        Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            // Quit the application
            System.exit(0);
        });
        quit.setLayoutX(96);
        quit.setLayoutY(560);
        parent.getChildren().add(quit);
    }


    // Method to get list of all locations

}
