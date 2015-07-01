import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class BlockBreakerPanel extends JPanel implements KeyListener{

    ArrayList<Block> blocks = new ArrayList<Block>();
    ArrayList<Block> ball = new ArrayList<Block>();
    ArrayList<Block> powerUp = new ArrayList<Block>();

    Block paddle;
    Thread thread;
    Animate animate;
    int ballSize = 25;

    BlockBreakerPanel() {
        paddle = new Block(175, 480, 150, 25, "paddle.png");

        for(int i = 0; i < 8; i++) {
            blocks.add(new Block((i*60+2), 0, 60, 25, "blue.png"));
        }
        for(int i = 0; i < 8; i++) {
            blocks.add(new Block((i*60+2), 25, 60, 25, "red.png"));
        }
        for(int i = 0; i < 8; i++) {
            blocks.add(new Block((i*60+2), 50, 60, 25, "green.png"));
        }
        for(int i = 0; i < 8; i++) {
            blocks.add(new Block((i*60+2), 75, 60, 25, "yellow.png"));
        }
        Random random = new Random();
        blocks.get(random.nextInt(32)).powerup = true;
        blocks.get(random.nextInt(32)).powerup = true;
        blocks.get(random.nextInt(32)).powerup = true;
        blocks.get(random.nextInt(32)).powerup = true;
        blocks.get(random.nextInt(32)).powerup = true;

        ball.add(new Block(237, 437, 25, 25, "ball.png"));
        // Won't read keys without these:
        addKeyListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Erase the screen and reprint
        for(Block b : blocks) {
            b.draw(g, this);
        }
        for(Block b : ball) {
            b.draw(g, this);
        }
        for(Block p : powerUp) {
            p.draw(g, this);
        }
        paddle.draw(g, this);
    }

    public void update() {
        for(Block p : powerUp) {
            p.y+=1;
            if(p.intersects(paddle) && !p.destroyed) {
                p.destroyed = true;
                ball.add(new Block(paddle.dx+75, 437, 25, 25, "ball.png"));
            }
        }
        for(Block ba : ball) {
            ba.x+=ba.dx;
            if(ba.x > (getWidth()-ballSize) && ba.dx > 0 || ba.x < 0) {
                ba.dx*=-1;
            }
            if(ba.y < 0 || ba.intersects(paddle)) {
                ba.dy*=-1;
            }
            for(Block b : blocks) {
                if((b.left.intersects(ba) || b.right.intersects(ba)) && !b.destroyed) {
                    b.destroyed = true;
                    ba.dx*=-1;
                    if(b.powerup) {
                        powerUp.add(new Block(b.x, b.y, 25, 19, "extra.png"));
                    }
                }
                if(ba.intersects(b) && !b.destroyed) {
                    b.destroyed = true;
                    ba.dy*=-1;
                }
            }
            ba.y+=ba.dy;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            animate = new Animate(this);
            thread = new Thread(animate);
            thread.start();
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT && paddle.x > 0) {
            paddle.x-=15;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT && paddle.x < (getWidth()-paddle.width)) {
            paddle.x+=15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
