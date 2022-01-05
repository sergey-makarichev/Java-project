package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.Piece;
import Client.Color;
/**
 *
 * @author Сергей
 */
public interface Hello extends Remote {
    void motion(Piece p) throws RemoteException;
    boolean changeColor(String color) throws RemoteException;
    Piece lastMotion() throws RemoteException;
    Color whoseMove() throws RemoteException;
    Color isGameOver() throws RemoteException;
    void restartServer() throws RemoteException;
}
