import static org.junit.jupiter.api.Assertions.*;

import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


public class FrontendDeveloperTests extends ApplicationTest{

//    /**
//    * initializes the JavaFX toolkit by creating a new JFXPanel toolkit instance before running any tests
//    * */
//    @BeforeAll
//    public static void initJFX() {
//        new JFXPanel(); //should fix makefile problem
//    }

    //private objects for integrations
    private FrontendPlaceholder frontend;
    private Backend backend;
    private Pane parent;

    @BeforeEach
    public void setup() throws Exception {
        ApplicationTest.launch(Frontend.class);

        //for integration
        frontend = new FrontendPlaceholder();
        // Placeholder for backend until actual implementation is available
        backend = new Backend(new DijkstraGraph<>());
        parent = new Pane();

        // backend instance in frontend
        frontend.setBackend(backend);
    }

//    /**
//     * Here is a simple sample JavaFX application that demonstrates how this
//     * test utility class can be used for testing.  See tests below.
//     */
//    public static class Frontend extends Application {
//        /**
//         * This sample program has a single label and button.  Whenever the
//         * button is clicked, extra text is appended to the end of the label.
//         */
//        public void start(Stage stage) {
//            Label label = new Label("Waiting:");
//            label.setId("onlyLabelId"); // reference label from test below
//            Button button = new Button("click me");
//            button.setId("onlyButtonId"); // to reference button from test
//            button.setOnAction(actionEvent -> label.setText(
//                    label.getText() + " clicked"));
//            stage.setScene(new Scene(new VBox(label,button),200,100));
//            stage.show();
//        }
//
//
//    }

    /**
     * Tests the createAllControls method of the Frontend class.
     */
    @Test
    public void testCreateAllControls() {
        Pane parent = new Pane();
        FrontendPlaceholder frontend = new FrontendPlaceholder();

        frontend.createAllControls(parent);

        // Test if all controls are created correctly
        // For example, check if specific controls are present in the parent pane
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Button));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Label));
    }

    /**
     * Tests the createShortestPathControls method of the Frontend class.
     */
    @Test
    public void testCreateShortestPathControls() {
        Pane parent = new Pane();
        FrontendPlaceholder frontend = new FrontendPlaceholder();

        frontend.createShortestPathControls(parent);

        // Test the creation of controls for shortest path search
        // For example, check if specific labels and buttons are added to the parent pane
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Label));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Button));
    }

    /**
     * Tests the createTravelTimesBox method of the Frontend class.
     */
    @Test
    public void testCreateTravelTimesBox() {
        Pane parent = new Pane();
        FrontendPlaceholder frontend = new FrontendPlaceholder();

        frontend.createTravelTimesBox(parent);

        // Test the creation of the travel times checkbox
        // Check if a checkbox is added to the parent pane
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof CheckBox));
    }

    /**
     * Tests the createOptionalLocationControls method of the Frontend class.
     */
    @Test
    public void testCreateOptionalLocationControls() {
        Pane parent = new Pane();
        FrontendPlaceholder frontend = new FrontendPlaceholder();

        frontend.createOptionalLocationControls(parent);

        // Test the creation of optional location controls
        // Check if labels and checkboxes are added to the parent pane
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Label));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof CheckBox));
    }


    // Integration tests
    /**
     * Integration test to ensure frontend controls are created correctly with backend integration.
     * This test checks if specific controls from both frontend and backend are present in the parent pane.
     */
//    @Test
//    public void testFrontendBackendIntegration() {
//        Pane parent = new Pane();
//        Frontend frontend = new Frontend();
//        // Create frontend controls
//        frontend.createAllControls(parent);
//        // Pass the backend interface instance to the frontend
//        frontend.setBackend(backend);
//
//        // Verify if specific controls from both frontend and backend are present in the parent pane
//        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof javafx.scene.control.Button));
//        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof javafx.scene.control.Label));
////        boolean yes = true;
////        assertTrue(yes);
//        // Add more assertions as needed to ensure frontend and backend integration
//    }

    @Test
    public void testFrontendBackendIntegration() {
        frontend.createAllControls(parent);
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Button));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof Label));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof CheckBox));
        // Add more assertions as needed to ensure frontend and backend integration
    }

    @Test
    public void testFrontendIntegration() {
        Pane parent = new Pane();
        FrontendPlaceholder frontend = new FrontendPlaceholder();

        // Create frontend controls
        frontend.createAllControls(parent);

         //Verify if specific frontend controls are present in the parent pane
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof javafx.scene.control.Button));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof javafx.scene.control.Label));
        assertTrue(parent.getChildren().stream().anyMatch(node -> node instanceof javafx.scene.control.CheckBox));
         //Add more assertions as needed to ensure frontend controls are present
//        boolean yes = true;
//        assertTrue(yes);
    }


}


