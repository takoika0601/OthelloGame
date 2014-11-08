package jp.co.shinkai;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Othello extends JFrame {
	public Othello() {
		setTitle("ÉIÉZÉçÉQÅ[ÉÄ");
		setResizable(false);

		Container contentPane = getContentPane();

		InfoPanel infoPanel = new InfoPanel();
		contentPane.add(infoPanel, BorderLayout.NORTH);

		MainBoard board = new MainBoard(infoPanel);
		contentPane.add(board, BorderLayout.CENTER);

		pack();
	}

	public static void main(String[] arg) {
		Othello frame = new Othello();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
