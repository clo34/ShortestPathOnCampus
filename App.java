import javafx.application.Application;

import java.io.IOException;

public class App {
  public static void main(String[] args) {
    System.out.println("v0.1");
    Backend backend = new Backend(new DijkstraGraph<>());

    System.out.println("Backend instance created successfully");

    Frontend frontend = new Frontend();
    frontend.setBackend(backend);

    System.out.println("Launching Frontend...");
    Application.launch(Frontend.class, args);
    System.out.println("Frontend launched");
  }
}
