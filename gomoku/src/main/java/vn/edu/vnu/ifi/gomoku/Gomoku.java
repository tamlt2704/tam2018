package vn.edu.vnu.ifi.gomoku;

import java.awt.Button;
import java.io.ObjectOutputStream.PutField;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Gomoku {

	/**
	 * @param args
	 */
	public static String fileName;
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		
		fileName = "Gomoku.txt";//args[0];
		System.out.println("File Name = "+fileName);
		Game game = new Game();
		game.start();
	}
}
