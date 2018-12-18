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
    GridPane root = new GridPane();

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
        root.setGridLinesVisible(false);
        setupGrid(5);
        root.setHgap(20);
        root.setVgap(20);

        VBox adjm = new VBox(new Label("Main Matrix"),alg.initAdjazent(anzahl));
        root.add(adjm,1,1);
        VBox wegm = new VBox(new Label("Weg Matrix"),alg.initWegM());
        root.add(wegm,0,3);
        VBox distm = new VBox(new Label("Distanz Matrix"),alg.initDistM());
        root.add(distm,2,3);

        VBox eigenschaften = new VBox(alg.getZusammen(),alg.getRadius(false),alg.getDurchmesser(false),alg.getZentrum(false));
        root.add(eigenschaften,0,0);

        VBox komponenten = new VBox(alg.getKomponent(),alg.getArtk(),alg.getEurlischeLinie());
        root.add(komponenten,3,0,2,5);

        //root.setAlignment(alg.initAdjazent(anzahl),Pos.CENTER);

        return root;
    }

    private void setupGrid(int numCols)
    {
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            root.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numCols; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numCols);
            root.getRowConstraints().add(rowConst);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
