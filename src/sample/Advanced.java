package sample;

import javafx.scene.control.Label;

public class Advanced {
    int[][] potenzm;
    int[][] distm;
    int[][] adj;
    int[][] wegm;
    boolean[] art;
    int kompAnzahl;
    String[] komponeten;
    String[] neuKomponeten;

    Label block = new Label();

    public Advanced(int[][] potenzm,int[][] distm, int[][] adj, int[][] wegm,String[] komponeten,int kompAnzahl)
    {
        this.kompAnzahl = kompAnzahl;
        this.art = new boolean[potenzm.length];
        this.komponeten = new String[komponeten.length];
        this.neuKomponeten = new String[komponeten.length];
        this.potenzm = new int[potenzm.length][potenzm.length];
        this.distm = new int[distm.length][distm.length];
        this.adj = new int[adj.length][adj.length];
        this.wegm = new int[wegm.length][wegm.length];

        for(int i = 0;i< distm.length;i++)
        {
            this.komponeten[i] = komponeten[i];
            this.neuKomponeten[i] = "";
            for(int j = 0;j< distm.length;j++)
            {
                this.potenzm[i][j] = potenzm[i][j];
                this.distm[i][j] = distm[i][j];
                this.adj[i][j] = adj[i][j];
                this.wegm[i][j] = wegm[i][j];
            }
        }
    }

    private boolean isArtikulation()
    {
        //uporedi aDistm i distm da bi video da li je knote artikulation
        if(kompAnzahl < (getKomponent()-1))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public Label getBlock(int[][] distm,int[][] adj,int[][] wegm,int[][] potenzm)
    {

        BlockAlgorithm blck = new BlockAlgorithm(art,coppyArray(distm),coppyArray(adj),coppyArray(wegm),coppyArray(potenzm),komponeten);
        //blck.start();
        //String blocke = blck.getBlock();

        /*for(int i = 0; i <blck.getBlock().length;i++)
        {
            blocke += "{"+blck.getBlock()[i]+"}\n";
        }*/

        //block.setText("Blöcke: "+blocke);

        return blck.getBlock();
    }

    private int[][] coppyArray(int[][] src)
    {
        int[][] dest = new int[src.length][src.length];
        for(int i = 0; i <src.length;i++)
        {
            for(int j = 0; j <src.length;j++)
            {
                dest[i][j] = src[i][j];
            }
        }

        return dest;
    }

    public boolean setAdj(int n)
    {
        int[][] neuAdj = new int[adj.length][adj.length];

        for(int i = 0; i < distm.length;i++) {
            for (int j = 0; j < distm.length; j++) {
                neuAdj[i][j] = adj[i][j];
            }

        }
        for(int i = 0; i < distm.length;i++)
        {
            for(int j = 0; j < distm.length;j++)
            {
                if(i == n || j == n)
                {
                    neuAdj[i][j] = 0;
                }
            }
        }

        if(potenzieren(neuAdj))
        {
            art[n] = true;
        }
        else
            art[n] = false;

        return potenzieren(neuAdj);
    }

    public boolean potenzieren(int[][] neu)
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

        return isArtikulation();
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
}
