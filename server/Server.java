package server;

import Client.Piece;
import Client.Color;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

/**
 *
 * @author Сергей
 */
public class Server extends UnicastRemoteObject implements Hello {
    private final static int ROWS = 10;
    private final static int COLUMNS = 10;
    private Color[][] pieces;
    private Color whoseColorMotion = Color.WHITE;
    private Color colorWin = Color.NOT_USED;
    private Vector motions;
    private boolean black = false;
    private boolean white = false;
    
    public Server() throws java.rmi.RemoteException {
        super();
        pieces = new Color[COLUMNS][ROWS];
        motions = new Vector();
        //motions.add(ref)
    }
    public static void main(String args[]) {
        try {
            Server obj = new Server();
            //Registry registry = LocateRegistry.getRegistry(); // подключение к уже существующему сервису имён
            Registry registry = LocateRegistry.createRegistry(8080);
            registry.bind("HelloServer", obj);
            //Naming.rebind("rmi://localhost/HelloServer", obj);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private boolean fiveInHorisontal(int y, Color color) {
        int counter = 0;
        for(int i = 0; i < COLUMNS; i++) {
            if(pieces[i][y]== color) {
                counter++;
                System.out.println("counter " + counter);
                if(counter == 5)
                    return true;
            }else {
                counter = 0;
            }
        }
        return false;
    }
    
    private boolean fiveInVertical(int x, Color color) {
        int counter = 0;
        for(int i = 0; i < COLUMNS; i++) {
            if(pieces[x][i]== color) {
                counter++;
                if(counter == 5)
                    return true;
            }else {
                counter = 0;
            }
        }
        return false;
    }
    
    private boolean fiveInDiagonal(int x, int y, Color color) {
        int i = x;
        int j = y;
        while(i > 0 && j < COLUMNS - 1) {
            i--;
            j++;
        }
        int counter = 0;
        for( ; i < COLUMNS && j >= 0; i++, j--) {
            if(pieces[i][j]== color) {
                counter++;
                System.out.println("counter " + counter);
                if(counter == 5)
                    return true;
            }else {
                counter = 0;
            }
        }
        i = x;
        j = y;
        while(i < ROWS - 1 && j < COLUMNS - 1) {
            i++;
            j++;
        }
        counter = 0;
        for( ; i >= 0 && j >= 0; i--, j--) {
            if(pieces[i][j]== color) {
                counter++;
                System.out.println("counter " + counter);
                if(counter == 5)
                    return true;
            }else {
                counter = 0;
            }
        }
        return false;
    }
    
    public boolean isWin(int x, int y, Color color) {
        if(fiveInHorisontal(y, color) || fiveInVertical(x, color) || fiveInDiagonal(x, y, color))
            return true;
        return false;
    }

    @Override
    public void motion(Piece p) throws RemoteException {
        int x = p.getX();
        int y = p.getY();
        Color color = p.getColor();
        pieces[x][y] = color;
        motions.add(p);
        System.out.println("responce: x = " + x + " y: "+ y + " color: "+color + " hash "+p.hashCode());
        if(color == Color.BLACK)
            whoseColorMotion = Color.WHITE;
        else if(color == Color.WHITE)
            whoseColorMotion = Color.BLACK;
        if(isWin(x,y,color)) {
            colorWin = color;
        }
    }

    @Override
    public boolean changeColor(String color)  throws RemoteException {
        if (color.equals("black") && !black) {
            black = true;
            return true;
        } else if(color.equals("white") && !white) {
            white = true;
            return true;
        }
        return false;
    }

    @Override
    public Piece lastMotion() throws RemoteException {
        if(motions.isEmpty())
            return null;
        return (Piece)motions.lastElement();
    }

    @Override
    public Color whoseMove() throws RemoteException {
        return whoseColorMotion;
    }

    @Override
    public Color isGameOver() throws RemoteException {
        return colorWin;
    }

    @Override
    public void restartServer() throws RemoteException {
        motions.clear();
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                pieces[i][j] = Color.NOT_USED;
            }
        }
        colorWin = Color.NOT_USED;
        Color whoseColorMotion = Color.WHITE;
    }
    
}
