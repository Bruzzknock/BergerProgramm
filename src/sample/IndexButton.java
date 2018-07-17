package sample;

import javafx.scene.control.Button;

/**
 * Button mit zwei Zahlen
 * Reihen und Spaltennummer in einer GridPane
 *
 * @author Hans Moritsch
 * @version 2018-06-10
 */

public class IndexButton extends Button
{
    int i;
    int j;

    public IndexButton(String text, int i, int j)
    {
        super(text);
        this.i = i;
        this.j = j;
    }

    public int getI()
    {
        return i;
    }

    public int getJ()
    {
        return j;
    }
}
