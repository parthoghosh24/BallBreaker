package com.BallBreaker;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
class GamePanel extends JPanel implements KeyListener, ActionListener, Runnable {

	static double platformX = 315;
	static double platformDx = 20;
	boolean moveLeft = false, moveRight = false;
	static double ballX = 390, ballY = 504;
	static double ballDx = 5, ballDy = 5;
	private static final int RADIUS = 8;
	public ArrayList<BufferedImage> brickList = new ArrayList<BufferedImage>();
	public ArrayList<Integer> coordinates = new ArrayList<Integer>(9);
	public static final int MAX = 20;
	public boolean destroyBrick = false;
	public boolean resetGame = false;
	public boolean pause=false;
	public static int lives = 3;
	public int score = 0;
	public JLabel livesCount, playerScore;
	public BufferedImage getPlatformSprite, getBallSprite, getBrickSprite;
	Thread thread = new Thread(this);
	JFrame mainScreen;

	public GamePanel(JFrame mainScreen) {
		// The gamePanel on which sprites are to be drawn
		setSize(800, 600);
		setLayout(null);
		this.mainScreen = mainScreen;
		// adding score label
		livesCount = new JLabel("Lives: " + lives);
		playerScore = new JLabel("Score: " + score);
		livesCount.setBounds(getSize().width - 60, getSize().height - 80, 50,
				20);
		playerScore.setBounds(0, getSize().height - 80, 120, 50);
		add(livesCount);
		add(playerScore);
		try {

			// Adding bricks grid

			// Getting brick sprite
			getBrickSprite = ImageIO
					.read(new File("Resources/Images/brick.png"));
			for (int i = 0; i < MAX; ++i) {
				brickList.add(i, getBrickSprite);
			}
			coordinates.add(150);//x
			coordinates.add(200);//y
			coordinates.add(220);//y
			coordinates.add(230);//x
			coordinates.add(240);//y
			coordinates.add(260);//y			
			coordinates.add(310);//x			
			coordinates.add(390);//x
			coordinates.add(470);//x					

			// Getting ball sprite
			getBallSprite = ImageIO.read(new File("Resources/Images/ball.png"));

			// Getting platform
			getPlatformSprite = ImageIO.read(new File(
					"Resources/Images/platform.png"));
		} catch (Exception exception) {
			System.out.println("Error creating sprites due to " + exception);
		}
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		thread.start();

	}

	public void playBallSound() {
		try {
			// Open an audio input stream.
			AudioInputStream audioIn = AudioSystem
					.getAudioInputStream(new File("Resources/Sounds/bounce.WAV"));
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void playCrashSound() {
		try {
			// Rights belong to SuperMarioBros 16 bit
			AudioInputStream audioIn = AudioSystem
					.getAudioInputStream(new File("Resources/Sounds/crash.WAV"));
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.drawImage(brickList.get(0), null, coordinates.get(0),
				coordinates.get(1));
		graphics2d.drawImage(brickList.get(1), null, coordinates.get(3),
				coordinates.get(1));
		graphics2d.drawImage(brickList.get(2), null, coordinates.get(6),
				coordinates.get(1));
		graphics2d.drawImage(brickList.get(3), null, coordinates.get(7),
				coordinates.get(1));
		graphics2d.drawImage(brickList.get(4), null, coordinates.get(8),
				coordinates.get(1));
		graphics2d.drawImage(brickList.get(5), null, coordinates.get(0),
				coordinates.get(2));
		graphics2d.drawImage(brickList.get(6), null, coordinates.get(3),
				coordinates.get(2));
		graphics2d.drawImage(brickList.get(7), null, coordinates.get(6),
				coordinates.get(2));
		graphics2d.drawImage(brickList.get(8), null, coordinates.get(7),
				coordinates.get(2));
		graphics2d.drawImage(brickList.get(9), null, coordinates.get(8),
				coordinates.get(2));
		graphics2d.drawImage(brickList.get(10), null, coordinates.get(0),
				coordinates.get(4));
		graphics2d.drawImage(brickList.get(11), null, coordinates.get(3),
				coordinates.get(4));
		graphics2d.drawImage(brickList.get(12), null, coordinates.get(6),
				coordinates.get(4));
		graphics2d.drawImage(brickList.get(13), null, coordinates.get(7),
				coordinates.get(4));
		graphics2d.drawImage(brickList.get(14), null, coordinates.get(8),
				coordinates.get(4));
		graphics2d.drawImage(brickList.get(15), null, coordinates.get(0),
				coordinates.get(5));
		graphics2d.drawImage(brickList.get(16), null, coordinates.get(3),
				coordinates.get(5));
		graphics2d.drawImage(brickList.get(17), null, coordinates.get(6),
				coordinates.get(5));
		graphics2d.drawImage(brickList.get(18), null, coordinates.get(7),
				coordinates.get(5));
		graphics2d.drawImage(brickList.get(19), null, coordinates.get(8),
				coordinates.get(5));
		graphics2d.drawImage(getBallSprite, null, (int) ballX, (int) ballY);
		graphics2d.drawImage(getPlatformSprite, null, (int) platformX, 520);

	}

	public void updateBall() {

		// Boundary check
		if (ballX + RADIUS < 0) {
			playBallSound();
			ballDx = Math.abs(ballDx);
		}
		if (ballX + RADIUS > getSize().width - 16) {
			playBallSound();
			ballDx = -Math.abs(ballDx);
		}
		if (ballY + RADIUS < 0) {
			playBallSound();
			ballDy = Math.abs(ballDy);
		}
		if (ballY + RADIUS > getSize().height - 16) {
			try {
				lives--;
				// System.out.println(lives);
				livesCount.setText("Lives: " + lives);
				JOptionPane.showMessageDialog(this, "Press ok to continue",
						"Life Lost", JOptionPane.ERROR_MESSAGE);
			} catch (Exception exception) {
			}
			ballX = 390;
			ballY = 504;
			platformX = 315;
		}

		// checking platform collision and performing deflection
		if (ballX > platformX
				&& ballX < platformX + getPlatformSprite.getWidth()) {
			if (ballY + RADIUS >= 520) {
				playBallSound();
				ballDy = -Math.abs(ballDy);
			}
		}

		// Performing brick collision and performing deflection and brick
		// destruction
		for (int i = 0; i < brickList.size(); ++i) {
			destroyBricks(i);
			if (destroyBrick) {
				playCrashSound();
				score += 10;
				playerScore.setText("Score: " + score);
				brickList.set(i, null);
			}
			destroyBrick = false;
		}

		ballX += ballDx;
		ballY += ballDy;
	}

	public void destroyBricks(int index) {
		double brickX = 0, brickY = 0;
		switch (index) {
		case 0:
			brickX = coordinates.get(0);
			brickY = coordinates.get(1);
			break;
		case 1:
			brickX = coordinates.get(3);
			brickY = coordinates.get(1);
			break;
		case 2:
			brickX = coordinates.get(6);
			brickY = coordinates.get(1);
			break;
		case 3:
			brickX = coordinates.get(7);
			brickY = coordinates.get(1);
			break;
		case 4:
			brickX = coordinates.get(8);
			brickY = coordinates.get(1);
			break;
		case 5:
			brickX = coordinates.get(0);
			brickY = coordinates.get(2);
			break;
		case 6:
			brickX = coordinates.get(3);
			brickY = coordinates.get(2);
			break;
		case 7:
			brickX = coordinates.get(6);
			brickY = coordinates.get(2);
			break;
		case 8:
			brickX = coordinates.get(7);
			brickY = coordinates.get(2);
			break;
		case 9:
			brickX = coordinates.get(8);
			brickY = coordinates.get(2);
			break;
		case 10:
			brickX = coordinates.get(0);
			brickY = coordinates.get(4);
			break;
		case 11:
			brickX = coordinates.get(3);
			brickY = coordinates.get(4);
			break;
		case 12:
			brickX = coordinates.get(6);
			brickY = coordinates.get(4);
			break;
		case 13:
			brickX = coordinates.get(7);
			brickY = coordinates.get(4);
			break;
		case 14:
			brickX = coordinates.get(8);
			brickY = coordinates.get(4);
			break;
		case 15:
			brickX = coordinates.get(0);
			brickY = coordinates.get(5);
			break;
		case 16:
			brickX = coordinates.get(3);
			brickY = coordinates.get(5);
			break;
		case 17:
			brickX = coordinates.get(6);
			brickY = coordinates.get(5);
			break;
		case 18:
			brickX = coordinates.get(7);
			brickY = coordinates.get(5);
			break;
		case 19:
			brickX = coordinates.get(8);
			brickY = coordinates.get(5);
			break;
		}
		if (brickList.get(index) != null) {
			if (checkCollisionWithBrick(brickX, brickY)) {
				// System.out.println("collision");
				destroyBrick = true;
				// System.out.println(brickList.size());
			}
		}
	}

	public void updatePlatform() {
		if (moveLeft) {
			platformX -= platformDx;
		}

		if (moveRight) {
			platformX += platformDx;
		}
		if (platformX + 150 > getSize().width) {
			platformX = getSize().width - 150;
		}
		if (platformX < 0) {
			platformX = 0;
		}
	}

	private boolean checkCollisionWithBrick(double brickX, double brickY) {
		Rectangle2D ball = new Rectangle2D.Double(ballX - RADIUS, ballY
				- RADIUS, getBallSprite.getWidth(), getBallSprite.getHeight());
		Rectangle2D brick = new Rectangle2D.Double(brickX, brickY,
				getBrickSprite.getWidth(), getBrickSprite.getHeight());
		if (ball.intersects(brick)) {
			if (ballX + RADIUS > brickX
					&& RADIUS * 2 < brickX + getBrickSprite.getWidth()) {
				ballDy = -ballDy;
			} else {
				ballDx = -ballDx;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void run() {
		int flag = 0;
		while (true) {
			for (int i = 0; i < brickList.size(); ++i) {
				if (brickList.get(i) == null)
					flag++;
				else {
					flag = 0;
					break;
				}
			}
			updateBall();
			repaint();
			try {
				Thread.sleep(20);
			} catch (Exception e) {

			}
			if (lives == 0) {
				JOptionPane.showMessageDialog(this, "Game over", "Game over",
						JOptionPane.ERROR_MESSAGE);
				resetGame = true;
				break;
			} else if (flag != 0) {
				JOptionPane.showMessageDialog(this, "You Win", "You Win",
						JOptionPane.INFORMATION_MESSAGE);
				resetGame = true;
				break;
			}
			while(pause){
				try{
				Thread.sleep(1000);
				}catch(Exception exception){
					System.out.println("Unable to sleep!");
				}
			}
		}
		resetGame();
	}


	public void resetGame() {
		if (resetGame) {
			mainScreen.dispose();
			score=0;
			pause=false;
			lives = 3;
			ballX = 390;
			ballY = 504;
			platformX = 315;
			destroyBrick = false;
			resetGame = false;
			moveLeft = false;
			moveRight = false;
			ballDx = 5;
			ballDy = 5;
			platformDx = 20;
			brickList = new ArrayList<BufferedImage>();
			coordinates = new ArrayList<Integer>(6);
			thread = new Thread(this);
			resetGame = false;
			new InitGame();
		}
	}

	// @Override
	// Moving the platform
	public void keyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			moveLeft = true;
			updatePlatform();
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveRight = true;
			updatePlatform();
		}
		if (key.getKeyCode() == KeyEvent.VK_P
				|| key.getKeyCode() == KeyEvent.VK_P + 32) {
			pause=(pause==true?false:true);
		}
	}

	// @Override
	public void keyReleased(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			moveLeft = false;
			updatePlatform();
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveRight = false;
			updatePlatform();
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// new InitGame();
	}

}

class InitGame implements ActionListener {
	JFrame mainScreen;
	JButton startButton;

	InitGame() {
		mainScreen = new JFrame();
		mainScreen.setSize(800, 600);
		mainScreen.setLayout(null);
		mainScreen.setTitle("Ball Breaker V1.0");
		startButton = new JButton("Start Game");
		startButton.setBounds(mainScreen.getSize().width / 2 - 100, mainScreen
				.getSize().height / 2, 200, 70);
		mainScreen.add(startButton);
		startButton.addActionListener(this);
		mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainScreen.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GamePanel gamePanel = new GamePanel(mainScreen);
		mainScreen.add(gamePanel);
		mainScreen.remove(startButton);
	}

}

class Game {

	public static void main(String[] args) {
		// The main frame screen
		new InitGame();

	}

}
