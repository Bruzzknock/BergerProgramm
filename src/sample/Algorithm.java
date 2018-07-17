package sample;

public class Algorithm {

    private int _anzahl;

    public Algorithm(int anzahl)
    {
        _anzahl = anzahl;
    }


    private void updateAdjM(String wert,int i, int j)
    {
        adj[i][j] = Integer.parseInt(wert);
        //updateWegM(Integer.parseInt(wert),i,j);
        //updateDM(Integer.parseInt(wert),i,j,true);
        potenzieren();
        //System.out.println("I: "+i+"J: "+j+"Wert: "+wert);
    }
}
