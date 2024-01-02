package org.tairobea;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    private class Tile{
        int x;
        int y;
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    private int boardWidth;
    private int boardHeight;

    int tileSize = 25;
    Tile snakeHead;
    Tile food;

    LinkedList<Tile> body;

    Timer gameLoop;
    int speedX;
    int speedY;

    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        snakeHead = new Tile(5, 5);
        food = generateRandomTile();
        speedX = 1;
        speedY = 0;
        body = new LinkedList<>();
        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        for (Tile tile : body){
            g.fillRect(tile.x * tileSize, tile.y * tileSize, tileSize, tileSize);
        }

        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.magenta);
        drawGrid(g);

        displayMessage(g);
    }

    private void drawGrid(Graphics g){
        g.setColor(Color.DARK_GRAY);
        for(int i = 0; i * tileSize < boardWidth; i++){
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
            g.drawLine(i *tileSize, 0, i *tileSize, boardHeight);
        }
    }

    private Tile generateRandomTile(){
        Random rand = new Random();
        int upperBound = 600 / 25;

        int x = rand.nextInt(upperBound);
        int y = rand.nextInt(upperBound);

        return new Tile(x, y);
    }

    private void move(){
        if (collison(snakeHead, food)){
            body.add(new Tile(food.x, food.y));
            food = generateRandomTile();
        }



        for (int i = body.size() -1; i >= 0; i--){
            Tile tile = body.get(i);
            if (i == 0){
                tile.x = snakeHead.x;
                tile.y = snakeHead.y;
            }else{
                Tile prev = body.get(i-1);
                tile.x = prev.x;
                tile.y = prev.y;
            }
        }

        snakeHead.x += speedX;
        snakeHead.y += speedY;

        if (collideWithBody() || collideWithWalls()){
            gameOver = true;
        }

    }

    private boolean collideWithBody(){
        for (Tile tile : body){
            if (collison(tile, snakeHead)){
                return true;
            }
        }

        return false;
    }

    private void displayMessage(Graphics g){
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game over: " + String.valueOf(body.size()), 10, 30);
        }else{
            g.setColor(Color.CYAN);
            g.drawString("Score: " + String.valueOf(body.size()), 10, 30);
        }
    }

    private boolean collideWithWalls(){
        return snakeHead.x < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y < 0 || snakeHead.y * tileSize > boardHeight;
    }


    public boolean collison(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver){
            gameLoop.stop();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && speedY != 1){
            speedX = 0;
            speedY = -1;

        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && speedY != -1){
            speedX = 0;
            speedY = 1;
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && speedX != 1){
            speedX = -1;
            speedY = 0;
        }else if (e.getKeyCode() == KeyEvent.VK_RIGHT && speedX != -1){
            speedX = 1;
            speedY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
