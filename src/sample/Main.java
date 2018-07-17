package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {
    GUI alg = new GUI();

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();

        Label knoten = new Label("Anzahl von knoten: ");
        TextField txknoten = new TextField();
        Button chose = new Button("Bestätigen");

        HBox hb = new HBox(knoten,txknoten,chose);
        root.setCenter(hb);
        root.setAlignment(hb, Pos.CENTER);


        Scene scene = new Scene(root,800.00,600.00);

        Controller controller = new Controller(scene);

        chose.setOnAction(event -> {
            Pane home = getHome(Integer.parseInt(txknoten.getText()));
            controller.addScreen("home",home);
            controller.activate("home");
        });

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Pane getHome(int anzahl)
    {
        Button bt = new Button("NESTO");
        BorderPane root = new BorderPane();
        HBox hb = new HBox();
        hb.setPadding(new Insets(10.00));
        hb.setStyle("-fx-background-color:green");



        root.setTop(hb);
        root.setCenter(alg.initAdjazent(anzahl));
        HBox matrizen = new HBox(alg.initWegM(),bt,alg.initDistM());
        root.setBottom(matrizen);

        VBox eigenschaften = new VBox(alg.getRadius(),alg.getDurchmesser(),alg.getZentrum());
        root.setLeft(eigenschaften);

        VBox komponenten = new VBox(alg.getKomponent(),alg.getArtk(),alg.getEurlischeLinie());
        root.setRight(komponenten);

        //root.setAlignment(alg.initAdjazent(anzahl),Pos.CENTER);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
