
package Client;

import java.io.Serializable;

/**
 *
 * @author Сергей
 */
public class Piece implements Serializable{
    private int x;
    private int y;
    private Color color;
    
    public Piece() {
        
    }
    
    public Piece(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.x + 32 * this.y;
    }
}
