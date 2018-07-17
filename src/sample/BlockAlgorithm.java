package sample;

import javafx.scene.control.Label;

public class BlockAlgorithm {
    int[][] distm;
    int[][] adj;
    int[][] wegm;
    int[][] potenzm;
    String[] komps;
    boolean[] art;

    public BlockAlgorithm(boolean[] art, int[][] distm, int[][] adj, int[][] wegm, int[][] potenzm,String[] komponents) {
        this.art = new boolean[art.length];
        this.komps = new String[komponents.length];

        this.potenzm = new int[potenzm.length][potenzm.length];
        this.distm = new int[distm.length][distm.length];
        this.adj = new int[adj.length][adj.length];
        this.wegm = new int[wegm.length][wegm.length];

        for(int i = 0;i< distm.length;i++)
        {
            for(int j = 0;j< distm.length;j++)
            {
                this.potenzm[i][j] = potenzm[i][j];
                this.distm[i][j] = distm[i][j];
                this.adj[i][j] = adj[i][j];
                this.wegm[i][j] = wegm[i][j];
            }

            this.komps[i] = komponents[i];
            this.art[i] = art[i];
        }
    }

    public Label getBlock()
    {
        Label block = new Label();
        int artAnzahl = 0;
        int verbessern = 0;
        //mora i da proveri da li je art povezana sa jos jednom art
        for(int i = 0; i < art.length;i++)
        {
            if(art[i])
            {
                for(int j = 0;j <art.length;j++)
                {
                    if(adj[i][j] == 1 && art[j])
                    {
                        adj[i][j] = 0;
                        adj[j][i] = 0;
                        if(shouldImprove(adj,i,j))
                        {
                            verbessern++;
                        }
                    }
                    adj[i][j] = 0;
                    adj[j][i] = 0;
                }
                artAnzahl++;
            }
        }

        block.setText("Anzahl der Blöcke: "+potenzieren(adj,artAnzahl,verbessern));
        return block;
    }

    private boolean shouldImprove(int[][] adj,int i, int j)
    {
        potenzieren(adj,0,0);

        if(distm[i][j] != 0)
        {
            return false;
        }

        return true;
    }

    public int potenzieren(int[][] neu,int artAnzahl,int verbessern)
    {
        for(int i = 0;i < neu.length;i++) {
            for (int j = 0; j < neu.length; j++) {
                potenzm[i][j] = neu[i][j];
                updateWegM(neu[i][j],i,j);
                updateDM(neu[i][j],i,j);
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
            for(int i = 0;i < neu.length;i++)
            {
                for(int j = 0;j < neu.length;j++)
                {
                    for(int z = 0;z < neu.length;z++)
                    {
                        zwischen[i][j] = zwischen[i][j] + potenzm[i][z] * neu[j][z];

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

            for(int i = 0;i < neu.length;i++) {
                for (int j = 0; j < neu.length; j++) {
                    potenzm[i][j] = zwischen[i][j];
                }
            }
        }

        //System.out.println(getKomponent()-artAnzahl+verbessern);
        return getKomponent()-artAnzahl+verbessern;
    }

    public int getKomponent()
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
                        komponent[anzahl] += Integer.toString(j) + " ";
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

        return anzahl;
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

    private void updateWegM(int wert,int i,int j)
    {
        if(i == j)
        {
            wegm[i][j] = 1;
        }
        else
        {
            wegm[i][j] = wert;
        }
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
        }
    }
}
