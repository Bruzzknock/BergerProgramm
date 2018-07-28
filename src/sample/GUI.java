package sample;

import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class GUI {

    int[][] adj;
    int[][] wegm;
    int[][] potenzm;
    int[][] distm;
    int rad;
    int kompAnzahl;
    String[] komponeten;
    GridPane weg;
    GridPane dist;
    Label radius = new Label();
    Label block = new Label();
    Label durch = new Label();
    Label zentrum = new Label();
    Label komponenten = new Label();
    Label eurlishe = new Label();
    Label artikulation = new Label();
    Label zs = new Label();

    //Panes
    public Pane initAdjazent(int anzahl)
    {
        GridPane grid = new GridPane();
        adj = new int[anzahl][anzahl];
        wegm = new int[anzahl][anzahl];
        potenzm = new int[anzahl][anzahl];
        distm = new int[anzahl][anzahl];
        komponeten = new String[anzahl];

        for(int i = 0; i < anzahl;i++)
        {
            for(int j = 0;j <anzahl;j++)
            {
                IndexButton iButton = new IndexButton("0",i,j);
                grid.add(iButton,j,i);

                initWegM();
                initDistM();
                updateAdjM(iButton.getText(),i,j);
                updateDM(0,i,j);

                if(i != j)
                {
                    iButton.setOnAction(event -> {
                        if(iButton.getText().equals("0"))
                        {
                            iButton.setText("1");
                            IndexButton second = (IndexButton) getNodeByRowColumnIndex(iButton.getJ(),iButton.getI(),grid);
                            second.setText("1");
                            updateAdjM("1",iButton.getI(),iButton.getJ());
                            updateAdjM("1",second.getI(),second.getJ());
                        }
                        else
                        {
                            iButton.setText("0");
                            IndexButton second = (IndexButton) getNodeByRowColumnIndex(iButton.getJ(),iButton.getI(),grid);
                            second.setText("0");
                            updateAdjM("0",iButton.getI(),iButton.getJ());
                            updateAdjM("0",second.getI(),second.getJ());
                        }
                    });
                }
            }
        }

        return grid;
    }

    public Pane initWegM()
    {
        String wert = "";
        GridPane grid = new GridPane();

        for(int i = 0;i<wegm.length;i++)
        {
            for(int j = 0;j<wegm.length;j++)
            {
                if(i == j)
                {
                    wert = "1";
                }
                else
                    wert = "0";

                IndexButton iButton = new IndexButton(wert,i,j);
                grid.add(iButton,i,j);
            }
        }

        weg = grid;
        return grid;
    }

    public Pane initDistM()
    {
        GridPane grid = new GridPane();

        for(int i = 0;i<wegm.length;i++)
        {
            for(int j = 0;j<wegm.length;j++)
            {
                if(i != j)
                {
                    IndexButton iButton = new IndexButton("-",i,j);
                    grid.add(iButton,i,j);
                }
                else
                {
                    IndexButton iButton = new IndexButton("0",i,j);
                    grid.add(iButton,i,j);
                }
            }
        }

        dist = grid;
        return grid;
    }

    //Labels
    public Label getRadius(boolean isZusammen)
    {
        String result;
        if(isZusammen)
        {
            rad = 0;

            for(var i = 0; i< distm.length;i++)
            {
                int row = getExtrenzitet(i);

                if(row < rad || i == 0)
                {
                    rad = row;
                }
            }

            result = "Radius: "+Integer.toString(rad);
        }
        else
            result = "Radius: ";


        radius.setText(result);
        return radius;
    }

    public Label getDurchmesser(boolean isZusammen)
    {
        String result;
        if(isZusammen)
        {
            int durchmesser = 0;
            for(var i = 0; i< distm.length;i++)
            {
                var row = getExtrenzitet(i);

                if(row > durchmesser || i == 0)
                {
                    durchmesser = row;
                }
            }

            result = "Durchmesser: "+Integer.toString(durchmesser);
        }
        else
            result = "Durchmesser: ";


        durch.setText(result);
        return durch;
    }

    public Label getZentrum(boolean isZusammen)
    {
        String result;
        if(isZusammen)
        {
            String zen = "";
            int radius = rad;
            for(var i = 0; i< distm.length;i++)
            {
                int row = getExtrenzitet(i);

                if(row == radius && radius != 0)
                {
                    //alert("i: "+(i+1) +"row: "+row+"radius: "+radius);
                    zen += (i+1) + " ";
                }
            }

            result = "Zentrum: "+zen;
        }
        else
            result = "Zentrum: ";


        this.zentrum.setText(result);
        return this.zentrum;
    }

    public Label getZusammen()
    {
        zs.setText("Zusammenhängend: "+isZusammen());
        return zs;
    }

    public Label getKomponent()
    {
        String text = "";
        String[] komponent = new String[adj.length];
        for(int i = 0;i <distm.length;i++)
        {
            komponent[i] = "";
        }

        int anzahl = 0;
        int[] knoten = new int[adj.length];
        for(int i = 0;i <distm.length;i++) {
            knoten[i] = 0;
        }

        for(int i = 0;i <distm.length;i++)
        {
            boolean drinnen = false;
            for(int j = 0;j <distm.length;j++)
            {

                    /*if(!isKnoteVorhanden(knoten,i) || i ==0)
                    {*/
                if(!isKnoteVorhanden(knoten,j) || i ==0)
                {
                    //irgendetwas stimmt nicht
                    if(distm[i][j] > 0 || i == j)
                    {
                        komponent[anzahl] += ";" + Integer.toString(j);
                        knoten[j] = j;
                        drinnen = true;
                    }
                }
                /*}*/
            }
            if(drinnen)
            {
                anzahl++;
            }
        }


        for(String i : komponent)
        {
            if(!text.equals(null) || !text.equals(""))
            {
                text += "{"+formatKomponent(i)+"}";
                text += "\n";
            }
        }

        kompAnzahl = anzahl;
        System.arraycopy(komponent,0,this.komponeten,0,komponent.length);

        komponenten.setText("Komponenten: "+text+"\n"+"Anzahl der Komponenten: "+Integer.toString(anzahl));
        return komponenten;
    }

    public Label getEurlischeLinie()
    {
        if(isZusammen())
        {
            boolean geschlossene = true;
            boolean[] offene= new boolean[adj.length];
            for(int i = 0;i < adj.length;i++)
            {
                int zahl = 0;
                for(int j = 0;j <adj.length;j++)
                {
                    if(adj[i][j] == 1)
                        zahl++;
                }
                if(zahl%2 != 0)
                {
                    geschlossene = false;
                    offene[i] = false;
                }
                else
                    offene[i] = true;
            }

            int offeneZahl = 0;
            for(int i = 0;i < offene.length;i++)
            {
                if(!offene[i])
                    offeneZahl++;
            }

            if(offeneZahl != 2)
                offene[0] = false;
            else
                offene[0] = true;

            eurlishe.setText("Geschlossene Eulerische Linie: "+geschlossene+"\nOffene Eulerische Linie: "+offene[0]);
            return eurlishe;
        }
        eurlishe.setText("Geschlossene Eulerische Linie: \nOffene Eulerische Linie: ");
        return eurlishe;
    }

    public Label getArtk()
    {
        String artikulationen = "";
        Advanced avd = new Advanced(potenzm,distm,adj,wegm,komponeten,kompAnzahl);
        for(int i = 0;i < komponeten.length;i++)
        {
            artikulationen += "Knote "+(i+1)+": "+avd.setAdj(i)+"\n";
        }

        block = avd.getBlock(distm,adj,wegm,potenzm);
        String text = block.getText();
        artikulation.setText(artikulationen+"\n"+text);
        return artikulation;
    }

    //Private Methods
    private void updateAdjM(String wert,int i, int j)
    {
        adj[i][j] = Integer.parseInt(wert);
        potenzieren();
    }

    private int getExtrenzitet(int i)
    {
        var extr = 0;
        for(var j = 0; j < distm.length;j++)
        {
            //alert(wegmatrix[i][j]);
            if(distm[i][j] > extr)
            {
                //alert("Drinnen");
                extr = distm[i][j];
            }
        }

        return extr;
    }

    public boolean isZusammen()
    {
        for(var i = 0; i < wegm.length;i++)
        {
            for(var j = 0; j < wegm.length;j++)
            {
                if(wegm[i][j] == 0)
                    return false;
            }
        }

        return true;
    }

    private void updateDM(int wert, int i,int j)
    {
        if(i != j)
        {
            distm[i][j] = wert;

            IndexButton btn = (IndexButton) getNodeByRowColumnIndex(i,j,dist);
            btn.setText(wert == 0 ? "-" : Integer.toString(wert));
        }
        //moras odraditit da kada je vec definisan radius i onda nije vise zusammen da vrati vrednosti na 0


        //printArray(distm);
    }

    private void potenzieren()
    {
        //System.arraycopy(adj,0,potenzm,0,adj.length);

        for(int i = 0;i < adj.length;i++) {
            for (int j = 0; j < adj.length; j++) {
            potenzm[i][j] = adj[i][j];
                updateWegM(adj[i][j],i,j);
                updateDM(adj[i][j],i,j);
            }
        }
        boolean cont = true;
        int wert = 1;
        //for(int k = 0;k< anzahl;k++)
        while(cont)
        {
            wert++;
            cont = false;
            int[][] zwischen = new int[potenzm.length][potenzm.length];
            for(int i = 0;i < adj.length;i++)
            {
                for(int j = 0;j < adj.length;j++)
                {
                    for(int z = 0;z < adj.length;z++)
                    {
                        zwischen[i][j] = zwischen[i][j] + potenzm[i][z] * adj[j][z];

                        if(zwischen[i][j] > 0) {
                            if (wegm[i][j] == 0) {
                                cont = true;
                                updateWegM(1, i, j);

                            }

                            if (distm[i][j] == 0)
                            {
                                updateDM(wert,i,j);
                            }
                        }
                    }
                }
            }

            for(int i = 0;i < adj.length;i++) {
                for (int j = 0; j < adj.length; j++) {
                    potenzm[i][j] = zwischen[i][j];
                }
            }
        }

        getZusammen();
        getRadius(isZusammen());
        getDurchmesser(isZusammen());
        getZentrum(isZusammen());
        getKomponent();
        getEurlischeLinie();
        getArtk();
    }

    private void updateWegM(int wert,int i,int j)
    {
        if(i == j)
        {
            wegm[i][j] = 1;
        }
        else
        {
            wegm[i][j] = wert;

            IndexButton btn = (IndexButton) getNodeByRowColumnIndex(i,j,weg);
            btn.setText(Integer.toString(wert));
            //System.out.println("I: "+i+"J: "+j+"Wert: "+wert);
        }
    }

    private Node getNodeByRowColumnIndex (int row, int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private String formatKomponent(String komponent)
    {
        String[] array = komponent.split(";");
        String result = "";

        if(array != null)
        {
            for(int i = 0;i< array.length;i++)
            {
                if(!array[i].equals(""))
                    result += Integer.parseInt(array[i]) + 1+";";
            }
        }

        return result;
    }

    private boolean isKnoteVorhanden(int[] knoten,int check)
    {
        for(int i = 0;i <knoten.length;i++) {
            if(knoten[i] == check)
            {
                return true;
            }
        }

        return false;
    }
}
