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

    private void updateAdjM(String wert,int i, int j)
    {
        adj[i][j] = Integer.parseInt(wert);
        //updateWegM(Integer.parseInt(wert),i,j);
        //updateDM(Integer.parseInt(wert),i,j,true);
        potenzieren();
        //System.out.println("I: "+i+"J: "+j+"Wert: "+wert);
    }

    public Label getRadius()
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


        radius.setText("Radius: "+Integer.toString(rad));
        return radius;
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
    public Label getDurchmesser()
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

        durch.setText("Durchmesser: "+Integer.toString(durchmesser));
        return durch;
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

    public Label getZentrum()
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

        this.zentrum.setText("Zentrum: "+zen);
        return this.zentrum;
    }

    public void updateDM(int wert, int i,int j)
    {
        if(i == j)
        {
            distm[i][j] = 0;
        }
        else
        {
            distm[i][j] = wert;

            IndexButton btn = (IndexButton) getNodeByRowColumnIndex(i,j,dist);
            btn.setText(Integer.toString(wert));
        }
        //moras odraditit da kada je vec definisan radius i onda nije vise zusammen da vrati vrednosti na 0


        //printArray(distm);
    }

    public void potenzieren()
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
            //printArray(zwischen);
        }
        /*if(anzahl == 1)
        {
            getA2(potenzm);
        }
        else
        {
            //System.arraycopy(getA2(potenzm),0,potenzm,0,adj.length);
            //treba ti metoda za iscitavanje arraya

            //printArray(potenzm);
        }*/

        if(true)//isZusammen())
        {
            getRadius();
            getDurchmesser();
            getZentrum();
            getKomponent();
            getEurlischeLinie();

            //mora za sve knote da vidi
            getArtk();
        }
        else
        {
            //mora sve da bude nula
            getArtk();
        }
    }

    private void printArray(int[][] array)
    {
        for(int i = 0;i < array.length;i++) {
            for (int j = 0; j < array.length; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println("\n");
        }

        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
    }

    /*public int[][] getA2(int[][] potenzm)
    {
        int[][] zwischen = new int[potenzm.length][potenzm.length];
        for(int i = 0;i < adj.length;i++)
        {
            for(int j = 0;j < adj.length;j++)
            {
                for(int z = 0;z < adj.length;z++)
                {
                    zwischen[i][j] = zwischen[i][j] + potenzm[i][z] * adj[j][z];

                    if(zwischen[i][j] > 0)
                    {
                        updateWegM(1,i,j);
                    }
                }
            }
        }

        printArray(zwischen);
        return zwischen;
    }*/

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
                IndexButton iButton = new IndexButton("0",i,j);
                grid.add(iButton,i,j);
            }
        }

        dist = grid;
        return grid;
    }


    //treba druga logika za update
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

    public Node getNodeByRowColumnIndex (int row, int column, GridPane gridPane) {
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
                text += "{"+(i)+"}";
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

    public Label getArtk()
    {
        String artikulationen = "";
        Advanced avd = new Advanced(potenzm,distm,adj,wegm,komponeten,kompAnzahl);
        for(int i = 0;i < komponeten.length;i++)
        {
            artikulationen += "Knote "+i+": "+avd.setAdj(i)+"\n";
        }

        //artikulationen += "Knote "+0+": "+avd.setAdj(0)+"\n";

        //System.out.println(artikulationen);

        block = avd.getBlock(distm,adj,wegm,potenzm);
        String text = block.getText();
        artikulation.setText(artikulationen+"\n"+text);
        return artikulation;
    }

}
