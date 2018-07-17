package sample;

public class Block {
    private int[] Nodes;

    public Block(int numberOfNodes)
    {
        this.Nodes = new int[numberOfNodes];
    }

    public void addNode(int node)
    {
        Nodes[node] = node;
    }

    public boolean checkIfNodeExist(int node)
    {
        if(Nodes[node] == 0)
        {
            return false;
        }

        return true;
    }
}
