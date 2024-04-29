import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int score = 0;
    int highscore = 0;

    //image atributes
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;
    Image gameOverImage;

    //player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // pipes atributes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    //game logic
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    private Color backgroundColor = Color.WHITE;
    private boolean gameOver = false;
    Timer gameOverTimer;
    private static final String HIGHSCORE_FILE = "highscore.txt";



    //constructor
    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth,frameHeight));
        setFocusable(true);
        addKeyListener(this);
        // setBackground(Color.blue);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/kuromi.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        gameOverImage = new ImageIcon(getClass().getResource("assets/gameover.png")).getImage();

        // Load highscore
        loadHighscore();

        player = new Player(frameWidth / 8, frameHeight / 2, 34, 24, birdImage);
        pipes = new ArrayList<>();

        pipesCooldown = new Timer(1500, e -> placePipes());
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // Inisialisasi Timer dengan delay 2000 ms (2 detik)
        gameOverTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tampilkan game over dialog setelah jeda waktu
                showGameOverDialog();
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, frameWidth, frameHeight);

        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        if (gameOver) {
            g.drawImage(gameOverImage, 0, 0, frameWidth, frameHeight, null);
        }

        // menampilkan skor
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
    }

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        // Check if Kuromi falls below the screen
        if (player.getPosY() >= frameHeight) {
            gameOver();
        }

        boolean passedPipe = false; // Penanda apakah pipa telah dilewati atau belum

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            // Check collision with pipes
            if (player.getPosX() + player.getWidth() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidth()) {
                if (player.getPosY() < pipe.getPosY() + pipe.getHeight() && player.getPosY() + player.getHeight() > pipe.getPosY()) {
                    gameOver();
                }
            }

            // Check if a pipe has been passed
            if (!pipe.isPassed() && pipe.getPosX() < playerStartPosX) {
                pipe.setPassed(true);
                passedPipe = true; // Set pipa telah dilewati
            }
        }

        // Tambahkan skor hanya jika pipa atas telah dilewati
        if (passedPipe) {
            score++;
        }
    }

    public void gameOver() {
        // Menghentikan timer game loop dan cooldown pipes
        gameLoop.stop();
        pipesCooldown.stop();

        // Menampilkan gambar game over
        gameOver = true;
        repaint();

        // Memulai timer untuk menampilkan game over dialog setelah jeda waktu
        gameOverTimer.start();
    }

    private void showGameOverDialog() {
        // Menampilkan dialog game over setelah jeda waktu
        gameOverTimer.stop(); // Menghentikan timer agar tidak berulang
        if (score > highscore) {
            highscore = score;
            saveHighscore();
        }

        // Tampilkan dialog game over
        int option = JOptionPane.showConfirmDialog(this, "Your Score: " + score + "\nHighscore: " + highscore + "\nDo you want to restart the game?", "Game Over", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    public void restartGame() {
        score = 0;
        pipes.clear();
        player.setPosY(frameHeight / 2);
        player.setVelocityY(0);
        gameOver = false;

        pipesCooldown.restart();
        gameLoop.restart();
    }

    private void loadHighscore() {
        try {
            File file = new File(HIGHSCORE_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                highscore = Integer.parseInt(reader.readLine());
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveHighscore() {
        try {
            FileWriter writer = new FileWriter(HIGHSCORE_FILE);
            writer.write(String.valueOf(highscore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
