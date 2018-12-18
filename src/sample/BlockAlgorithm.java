package sample;

import javafx.scene.control.Label;

public class BlockAlgorithm {
    int[][] distm;
    int[][] adj;
    int[][] wegm;
    int[][] potenzm;
    boolean[] art;

    public BlockAlgorithm(boolean[] art, int[][] distm, int[][] adj, int[][] wegm, int[][] potenzm) {
        this.art = new boolean[art.length];
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
            this.art[i] = art[i];
        }
    }

    public Label getBlock()
    {
        Label block = new Label();
        //broj artikulacija
        int artAnzahl = 0;
        int verbessern = 0;

        for(int i = 0; i < art.length;i++)
        {
            if(art[i])
            {
                for(int j = 0;j <art.length;j++)
                {
                    //ako je ta knota povezana sa jos jednom artikulacijom
                    if(adj[i][j] == 1 && art[j])
                    {
                        adj[i][j] = 0;
                        adj[j][i] = 0;

                        if(shouldImprove(adj,i,j))
                        {
                            verbessern++;
                        }
                    }

                    //brise se konekcija izmedju artikulacije i te knote
                    adj[i][j] = 0;
                    adj[j][i] = 0;
                }
                artAnzahl++;
            }
        }

        block.setText("Anzahl der Blöcke: "+potenzieren(adj,artAnzahl,verbessern));
        return block;
    }

    //ako je artikulacija povezana sa artikulacijom i izbrisemo konekciju, ako prva nije nekim drugim putem povezana sa drugom onda te dve knote cine jedan blok
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

        return getKomponent()-artAnzahl+verbessern;
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
