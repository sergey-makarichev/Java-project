
package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JPanel;
import java.util.Scanner;
import server.Hello;

/**
 *
 * @author Сергей
 */
public class Client extends JPanel implements ActionListener, MouseListener {
    
    private final static int ROWS = 10;
    private final static int COLUMNS = 10;
    private final static int SIZE_BLOCK = 32;
    private boolean gameOver;
    private int lastHash = -1;
    private static Color myColor;
    private Graphics2D g2d;
    private Color[][] pieces;
    private static JFrame frame;
    private static JMenuBar menuBar;
    private static Hello stub;
    
    
    public Client() {
        pieces = new Color[COLUMNS][ROWS];
        frame = new JFrame();
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem resetAction = new JMenuItem("Reset");
        JMenuItem exitAction = new JMenuItem("Exit");
        resetAction.addActionListener(this);
        exitAction.addActionListener(this);
        menu.add(resetAction);
        menu.add(exitAction);
        menuBar.add(menu);
 
        resetBoard();
        setPreferredSize(new Dimension(COLUMNS * SIZE_BLOCK, ROWS * SIZE_BLOCK));
        addMouseListener(this);
        Runnable task = new Runnable() {
        public void run() {
            waitResponce();
        }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        drawBoard(g2d);
        drawPieces(g2d);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals("Reset")) {
                gameOver = true;
                resetBoard();
                this.repaint();
                stub.restartServer();
                Runnable task = new Runnable() {
                public void run() {
                    waitResponce();
                }
                };
                Thread thread = new Thread(task);
                thread.start();
            } else
                frame.dispose();
        } catch (Exception ex) {}
    }
 
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            if(stub.whoseMove() == myColor && !gameOver){
                updateBoard(e.getX(), e.getY(), myColor);
            }
            else
                System.out.println("Сейчас не ваш ход");
        } catch(Exception ev) {
            System.err.println("Server exception: " + ev.toString());
            ev.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    private void waitResponce() {
        try {
            while(!gameOver) {
                Piece oponentMotion = stub.lastMotion();
                if(oponentMotion != null && lastHash != oponentMotion.hashCode()) {
                    int oponentX = oponentMotion.getX();
                    int oponentY = oponentMotion.getY();
                    Color oponentColor = oponentMotion.getColor();
                    //System.out.println("responce: x = " + oponentX + " y: "+oponentY + " color: "+oponentColor);
                    updateBoard(oponentX,oponentY, oponentColor);
                    lastHash = oponentMotion.hashCode();
                    this.repaint();
                    if (stub.isGameOver() != Color.NOT_USED) {
                        JOptionPane.showMessageDialog(null,
                        stub.isGameOver() + " win");
                        gameOver = true;
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private void updateBoard(int x, int y, Color color) {
        int centreX, centreY;
        if(color == myColor ) {
            centreX = x / SIZE_BLOCK;
            centreY = y / SIZE_BLOCK;
        } else {
            centreX = x;
            centreY = y;
        }
        //System.out.println("centreX: " + centreX + " centreY: "+centreY);
        if (centreX < 0 || centreY < 0) {
            JOptionPane.showMessageDialog(null,
                    "You cannot place a piece here.");
        } else {
            if (pieces[centreX][centreY] != Color.NOT_USED) {
                JOptionPane.showMessageDialog(null,
                        "You cannot place a piece above another one.");
            } else {
                pieces[centreX][centreY] = color;
                this.repaint();
                if(color == myColor) {
                    Piece p = new Piece(centreX, centreY, color);
                    lastHash = p.hashCode();
                    try {
                        stub.motion(p);
                        if (stub.isGameOver() != Color.NOT_USED) {
                            JOptionPane.showMessageDialog(null,
                            stub.isGameOver() + " win");
                            gameOver = true;
                        }
                    } catch (Exception e) {
                        System.err.println("Server exception: " + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
        
    private void drawBoard(Graphics2D g) {
        g.drawLine(0, 0, COLUMNS * SIZE_BLOCK, 0); //x1 y1 x2 y2
        g.drawLine(0, ROWS * SIZE_BLOCK, COLUMNS * SIZE_BLOCK, ROWS * SIZE_BLOCK);
        g.drawLine(0, 0, 0, ROWS * SIZE_BLOCK);
        g.drawLine(COLUMNS * SIZE_BLOCK, 0 * SIZE_BLOCK, COLUMNS * SIZE_BLOCK, ROWS * SIZE_BLOCK);
        for (int i = 1; i < ROWS; i++) {
            g.drawLine(0, SIZE_BLOCK * i, SIZE_BLOCK / 2, SIZE_BLOCK * i);
            g.drawLine(COLUMNS * SIZE_BLOCK - (SIZE_BLOCK / 2), SIZE_BLOCK * i,
                    COLUMNS * SIZE_BLOCK, SIZE_BLOCK * i);
        }
        for (int j = 1; j < COLUMNS; j++) {
            g.drawLine(SIZE_BLOCK * j, 0, SIZE_BLOCK * j, SIZE_BLOCK / 2);
            g.drawLine(SIZE_BLOCK * j, SIZE_BLOCK * ROWS - (SIZE_BLOCK / 2),
                    j * SIZE_BLOCK, ROWS * SIZE_BLOCK);
        }
        for (int i = 0; i < COLUMNS - 1; i++) {
            for (int j = 0; j < ROWS - 1; j++) {
                g.drawLine((SIZE_BLOCK / 2) + (i * SIZE_BLOCK),
                        (j + 1) * SIZE_BLOCK,
                        ((SIZE_BLOCK / 2) + ((i + 1) * SIZE_BLOCK)),
                        (j + 1) * SIZE_BLOCK);
                g.drawLine((i + 1) * SIZE_BLOCK,
                        (SIZE_BLOCK / 2) + (j * SIZE_BLOCK),
                        (i + 1) * SIZE_BLOCK,
                        (SIZE_BLOCK / 2) + ((j + 1) * SIZE_BLOCK));
            }
        }
    }
 
    private void drawPieces(Graphics2D g) {
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (pieces[i][j] != Color.NOT_USED) {
                    int x = i * SIZE_BLOCK;
                    int y = j * SIZE_BLOCK;
                    if (pieces[i][j] == Color.BLACK) {
                        g.fillOval(x, y, SIZE_BLOCK, SIZE_BLOCK);
                    }
                    if (pieces[i][j] == Color.WHITE) {
                        g.drawOval(x, y, SIZE_BLOCK, SIZE_BLOCK);
                    }
                }
            }
        }
    }
        
    private void resetBoard() {
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                pieces[i][j] = Color.NOT_USED;
            }
        }
        gameOver = false;
    }
    
    private static void createAndShowGUI() {
        JPanel panel = new Client();
 
        frame.setTitle("Gomoku(" + myColor + ")");
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //String host = (args.length < 1) ? null : args[0];
        String host = "localhost";
        int port = 8080;
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            System.out.println("registry :" +host+":"+port);
            stub = (Hello) registry.lookup("HelloServer");
            System.out.println(stub);
            Scanner scanner = new Scanner(System.in);
            String s1;
            do {
                System.out.println("Введите цвет камня, которым хотите играть");
                s1 = scanner.nextLine();
            } while(!stub.changeColor(s1));
            if (s1.equals("black"))
                myColor = Color.BLACK;
            else
                myColor = Color.WHITE;
            System.out.println("Ваш цвет - " + myColor);
            scanner.close();
            createAndShowGUI();
           /* boolean flag = stub.motion();
            System.out.println("responce: " + response);*/
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
