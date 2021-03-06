package jp.co.shinkai;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class MainBoard extends JPanel implements MouseListener {
	// マスの数
	private static final int MASU = 8;
	// 一マスあたりの大きさ
	private static final int GS = 32;
	// 盤面の大きさ
	private static final int WIDTH = GS * MASU;
	private static final int HEIGHT = WIDTH;
	// 空白マス
	private static final int BLANK = 0;
	// 黒石
	private static final int BLACK_STONE = 1;
	// 白石
	private static final int WHITE_STONE = -1;
	// スリープタイム
	private static final int SLEEP_TIME = 500;
	// 終了時の石の数
	private static final int END_NUMBER = 60;

	// ゲーム状態
	private static final int START = 0;
	private static final int PLAY = 1;
	private static final int YOU_WIN = 2;
	private static final int YOU_LOSE = 3;
	private static final int DRAW_GANE = 4;

	// 盤面
	private int[][] board = new int[MASU][MASU];
	// 白の番かどうか
	private boolean flagForWhite;
	// 打たれた石の数
	private int putNumber;
	// 石を打つ音
	private AudioClip kachi;
	// 石を打てない場所に置こうとした場合の音
	private AudioClip buu;

	// ゲーム状態
	private int gameState;

	// 情報パネルへの参照
	private InfoPanel infoPanel;

	public MainBoard(InfoPanel infoPanel) {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.infoPanel = infoPanel;

		// 盤面のロード
		initBoard();

		kachi = Applet.newAudioClip(getClass().getResource("kachi.wav"));
		buu = Applet.newAudioClip(getClass().getResource("buu.wav"));

		addMouseListener(this);

		gameState = START;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 盤面を描く
		drawBoard(g);

		switch (gameState) {
		case START:
			drawTextContering(g, "OTHELLO");
			break;
		case PLAY:
			// 石を描く
			drawStone(g);

			// 盤上の石の数を数える
			Counter counter = countStone();
			// ラベルに表示する
			infoPanel.setBlackLabel(counter.blackCount);
			infoPanel.setWhiteLabel(counter.whiteCount);
			break;
		case YOU_WIN:
			drawTextContering(g, "YOU WIN!");
			break;
		case YOU_LOSE:
			drawTextContering(g, "YOU LOSE");
			break;
		case DRAW_GANE:
			drawTextContering(g, "DRAW");
			break;
		}

	}

	private void initBoard() {
		for (int y = 0; y < MASU; y++) {
			for (int x = 0; x < MASU; x++) {
				board[y][x] = BLANK;
			}
		}

		board[3][3] = board[4][4] = WHITE_STONE;
		board[3][4] = board[4][3] = BLACK_STONE;

		flagForWhite = false;
		putNumber = 0;
	}

	private void drawBoard(Graphics g) {
		g.setColor(new Color(0, 128, 128));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		for (int y = 0; y < MASU; y++) {
			for (int x = 0; x < MASU; x++) {
				g.setColor(Color.BLACK);
				g.drawRect(x * GS, y * GS, GS, GS);
			}
		}

		g.setColor(Color.black);

		for (int i = 0; i < MASU; i++) {
			g.drawLine(i * GS, 1, i * GS, HEIGHT);
		}

		for (int i = 0; i < MASU; i++) {
			g.drawLine(0, i * GS, WIDTH, i * GS);
		}

		g.drawRect(0, 0, WIDTH, HEIGHT);
	}

	private void drawStone(Graphics g) {
		for (int y = 0; y < MASU; y++) {
			for (int x = 0; x < MASU; x++) {
				if (board[y][x] == BLANK) {
					continue;
				} else if (board[y][x] == BLACK_STONE) {
					g.setColor(Color.BLACK);
				} else if (board[y][x] == WHITE_STONE) {
					g.setColor(Color.WHITE);
				}
				g.fillOval(x * GS + 3, y * GS + 3, GS - 6, GS - 6);
			}
		}
	}

	private void putDownStone(int x, int y) {
		int stone;

		if (flagForWhite) {
			stone = WHITE_STONE;
		} else {
			stone = BLACK_STONE;
		}

		putNumber++;
		board[y][x] = stone;
		kachi.play();

		update(getGraphics());
		sleep();
	}

	private boolean canPutDown(int x, int y) {
		if (x >= MASU || y >= MASU) {
			return false;
		}
		if (board[y][x] != 0) {
			return false;
		}
		if (canPutDown(x, y, 1, 0)) {
			return true;
		}
		if (canPutDown(x, y, 0, 1)) {
			return true;
		}
		if (canPutDown(x, y, -1, 0)) {
			return true;
		}
		if (canPutDown(x, y, 0, -1)) {
			return true;
		}
		if (canPutDown(x, y, 1, 1)) {
			return true;
		}
		if (canPutDown(x, y, -1, -1)) {
			return true;
		}
		if (canPutDown(x, y, 1, -1)) {
			return true;
		}
		if (canPutDown(x, y, -1, 1)) {
			return true;
		}
		return false;
	}

	private boolean canPutDown(int x, int y, int vecX, int vecY) {
		int putStone;

		if (flagForWhite) {
			putStone = WHITE_STONE;
		} else {
			putStone = BLACK_STONE;
		}

		x += vecX;
		y += vecY;

		if (x < 0 || x >= MASU || y < 0 || y >= MASU) {
			return false;
		}
		if (board[y][x] == putStone) {
			return false;
		}
		if (board[y][x] == BLANK) {
			return false;
		}

		x += vecX;
		y += vecY;

		while (x >= 0 && x < MASU && y >= 0 && y < MASU) {
			if (board[y][x] == BLANK) {
				return false;
			}
			if (board[y][x] == putStone) {
				return true;
			}
			x += vecX;
			y += vecY;
		}

		return false;
	}

	private void reverse(int x, int y) {
		if (canPutDown(x, y, 1, 0)) {
			reverse(x, y, 1, 0);
		}
		if (canPutDown(x, y, 0, 1)) {
			reverse(x, y, 0, 1);
			;
		}
		if (canPutDown(x, y, -1, 0)) {
			reverse(x, y, -1, 0);
			;
		}
		if (canPutDown(x, y, 0, -1)) {
			reverse(x, y, 0, -1);
			;
		}
		if (canPutDown(x, y, 1, 1)) {
			reverse(x, y, 1, 1);
			;
		}
		if (canPutDown(x, y, -1, -1)) {
			reverse(x, y, -1, -1);
			;
		}
		if (canPutDown(x, y, 1, -1)) {
			reverse(x, y, 1, -1);
			;
		}
		if (canPutDown(x, y, -1, 1)) {
			reverse(x, y, -1, 1);
		}
	}

	private void reverse(int x, int y, int vecX, int vecY) {
		int putStone;

		if (flagForWhite) {
			putStone = WHITE_STONE;
		} else {
			putStone = BLACK_STONE;
		}

		x += vecX;
		y += vecY;

		while (board[y][x] != putStone) {
			board[y][x] = putStone;
			kachi.play();

			update(getGraphics());
			sleep();

			x += vecX;
			y += vecY;
		}
	}

	private void sleep() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void drawTextContering(Graphics g, String s) {
		Font font = new Font("SansSerif", Font.BOLD, 20);
		g.setFont(font);
		FontMetrics fMetrics = g.getFontMetrics();
		g.setColor(Color.YELLOW);
		g.drawString(s, WIDTH / 2 - fMetrics.stringWidth(s) / 2, HEIGHT / 2
				+ fMetrics.getDescent());
	}

	private void endGame() {
		if (putNumber == END_NUMBER) {
			Counter counter;
			counter = countStone();

			if (counter.blackCount > 32) {
				gameState = YOU_WIN;
			} else if (counter.blackCount < 32) {
				gameState = YOU_LOSE;
			} else {
				gameState = DRAW_GANE;
			}
		}
	}

	private Counter countStone() {
		Counter counter = new Counter();

		for (int y = 0; y < MASU; y++) {
			for (int x = 0; x < MASU; x++) {
				if (board[y][x] == BLACK_STONE) {
					counter.blackCount++;
				} else if (board[y][x] == WHITE_STONE) {
					counter.whiteCount++;
				}
			}
		}
		return counter;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (gameState) {
		case START:
			gameState = PLAY;
			break;
		case PLAY:
			int x = e.getX() / GS;
			int y = e.getY() / GS;

			if (canPutDown(x, y)) {
				putDownStone(x, y);
				reverse(x, y);

				flagForWhite = !flagForWhite;
			} else {
				buu.play();
			}

			endGame();
			break;
		case DRAW_GANE:
			gameState = START;
			initBoard();
			break;
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ
	}

	private class Counter {
		public int blackCount;
		public int whiteCount;

		public Counter() {
			blackCount = 0;
			whiteCount = 0;
		}
	}
}
