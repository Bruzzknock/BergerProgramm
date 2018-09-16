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

        primaryStage.setTitle("Berger Programm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Pane getHome(int anzahl)
    {
        GridPane root = new GridPane();
        root.setHgap(20);
        root.setVgap(20);
        //setupGrid(root);

        root.add(alg.initAdjazent(anzahl),1,0);
        HBox matrizen = new HBox(alg.initWegM(),alg.initDistM());
        root.add(matrizen,1,1);

        VBox eigenschaften = new VBox(alg.getZusammen(),alg.getRadius(false),alg.getDurchmesser(false),alg.getZentrum(false));
        root.add(eigenschaften,0,0);

        VBox komponenten = new VBox(alg.getKomponent(),alg.getArtk(),alg.getEurlischeLinie());
        root.add(komponenten,2,0);

        //root.setAlignment(alg.initAdjazent(anzahl),Pos.CENTER);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
