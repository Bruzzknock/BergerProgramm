package sample;

import javafx.scene.control.Label;

public class Advanced {
    int[][] potenzm;
    int[][] distm;
    int[][] adj;
    int[][] wegm;
    boolean[] art;
    int kompAnzahl;
    String[] neuKomponeten;

    public Advanced(int[][] potenzm,int[][] distm, int[][] adj, int[][] wegm,String[] komponeten,int kompAnzahl)
    {
        this.kompAnzahl = kompAnzahl;
        this.art = new boolean[potenzm.length];
        this.neuKomponeten = new String[komponeten.length];
        this.potenzm = new int[potenzm.length][potenzm.length];
        this.distm = new int[distm.length][distm.length];
        this.adj = new int[adj.length][adj.length];
        this.wegm = new int[wegm.length][wegm.length];

        for(int i = 0;i< distm.length;i++)
        {
            this.neuKomponeten[i] = "";
            for(int j = i;j< distm.length;j++)
            {
                this.potenzm[i][j] = potenzm[i][j];
                this.distm[i][j] = distm[i][j];
                this.adj[i][j] = adj[i][j];
                this.wegm[i][j] = wegm[i][j];

                this.potenzm[j][i] = potenzm[j][i];
                this.distm[j][i] = distm[j][i];
                this.adj[j][i] = adj[j][i];
                this.wegm[j][i] = wegm[j][i];
            }
        }
    }

    //poredmo prethodni komponenten anzahl sa novim
    private boolean isArtikulation()
    {
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
        int[][] distm1 = new int[distm.length][distm.length];
        int[][] adj1 = new int[distm.length][distm.length];
        int[][] wegm1 = new int[distm.length][distm.length];
        int[][] potenzm1 = new int[distm.length][distm.length];

        for(int i = 0; i <distm1.length;i++)
        {
            for(int j = i; j <distm1.length;j++)
            {
                distm1[i][j] = distm[i][j];
                distm1[j][i] = distm[j][i];

                adj1[i][j] = adj[i][j];
                adj1[j][i] = adj[j][i];

                wegm1[i][j] = wegm[i][j];
                wegm1[j][i] = wegm[j][i];

                potenzm1[i][j] = potenzm[i][j];
                potenzm1[j][i] = potenzm[j][i];
            }
        }

        BlockAlgorithm blck = new BlockAlgorithm(art,distm1,adj1,wegm1,potenzm1);

        return blck.getBlock();
    }


    public boolean setAdj(int n)
    {
        int[][] neuAdj = new int[adj.length][adj.length];

        //inicijalizira novi array
        //knota koja je izabrana se "brise"
        for(int i = 0; i < distm.length;i++)
        {
            for(int j = i; j < distm.length;j++)
            {
                if(!(i == n || j == n))
                {
                    neuAdj[i][j] = adj[i][j];
                    neuAdj[j][i] = adj[j][i];
                }
                else
                {
                    neuAdj[i][j] = 0;
                    neuAdj[j][i] = 0;
                }
            }
        }

        boolean isArtikulation = potenzieren(neuAdj);

        if(isArtikulation)
        {
            art[n] = true;
        }
        else
            art[n] = false;

        return isArtikulation;
    }

    //kalkulise distm i wegm ako je knota izbrisana
    public boolean potenzieren(int[][] neu)
    {
        for(int i = 0;i < neu.length;i++) {
            for (int j = i; j < neu.length; j++) {
                potenzm[i][j] = neu[i][j];
                updateWegM(neu[i][j],i,j);
                updateDM(neu[i][j],i,j);

                potenzm[j][i] = neu[j][i];
                updateWegM(neu[j][i],j,i);
                updateDM(neu[j][i],j,i);
            }
        }

        boolean cont = true;
        int wert = 1;

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
        int anzahl = 0;
        boolean[] knoten = new boolean[adj.length];

        for(int i = 0;i <distm.length;i++)
        {
            boolean drinnen = false;

            if(!isKnoteVorhanden(knoten,i)) {
                for (int j = i; j < distm.length; j++) {
                    if (!isKnoteVorhanden(knoten, j)) {
                        if (distm[i][j] > 0 || i == j) {
                            knoten[j] = true;
                            drinnen = true;
                        }
                    }
                }
            }

            if(drinnen)
            {
                anzahl++;
            }
        }

        return anzahl;
    }

    private boolean isKnoteVorhanden(boolean[] knoten,int check)
    {
        if(knoten[check])
            return true;
        else
            return false;
    }
}
