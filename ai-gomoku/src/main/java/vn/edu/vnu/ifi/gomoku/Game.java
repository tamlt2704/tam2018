package vn.edu.vnu.ifi.gomoku;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream.PutField;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class Game extends JFrame {

	public JPanel gameTable;
	public JPanel gameBoard;
	public JLabel labelName;
	
	public JLabel nameOfColor;
	public JLabel lastPosition;
	
	public static JLabel labelNumberOfTour;
	public static JButton buttonPlay;
	public static JCheckBox checkBox; 
	
	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	static final int BOARDSIZE = 20;
	static final int PIECESIZE = 20;
	static final int GAMEBOARDSIZE = 600; // size of JFrame
	static final int MAXPIECENUM = BOARDSIZE * BOARDSIZE;
	public static int stepOfPlayer;
	public static  int numberOfTour = 0;

	public static Computer computer;

	public Game() {
		init();
		draw();
	}

	public void init() {
		
		computer = new Computer(BOARDSIZE, this);
		computer.Init();
		// petit.mechant: 16/5/2013 init gui
	
		this.setTitle("Game Gomoku - La Thanh Tam- P17 IFI ");
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// init game board: name + button
		gameBoard = new JPanel(new GridLayout(3, 1));
		labelName = new JLabel("La Thanh Tam - P17 - IFI ");
		labelNumberOfTour = new JLabel("Number of Tour: 0");
		checkBox = new JCheckBox("Play with computer");
		buttonPlay = new JButton("Play");
		
		nameOfColor  = new JLabel("My color is: "); ;
		lastPosition = new JLabel("Last position: ");
		
		gameBoard.add(labelName);
		gameBoard.add(checkBox);
		gameBoard.add(labelNumberOfTour);		
		gameBoard.add(buttonPlay);
		gameBoard.add(nameOfColor);
		gameBoard.add(lastPosition);
		
	
		buttonPlay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				thinkAndPlay();
			}
		});

		// init game table 20x20
		gameTable = new JPanel();
		gameTable.setLayout(new GridLayout(BOARDSIZE, BOARDSIZE));
		// add 20x20 cell empty
		for (int x = 0; x < BOARDSIZE; x++)
			for (int y = 0; y < BOARDSIZE; y++) {
				gameTable.add(computer.Cells[x][y]);
			}

		// add gameBoard and game table to Frame
		

		add(gameBoard, BorderLayout.PAGE_START);
		add(gameTable, BorderLayout.CENTER);
		setSize(GAMEBOARDSIZE, GAMEBOARDSIZE);
		setMinimumSize(new Dimension(GAMEBOARDSIZE, GAMEBOARDSIZE));
		pack();
		// setVisible(true);
		// petit.mechant:: 16/5/2013
	}
	
	public static void thinkAndPlay() {
		if (computer.GameState != computer.GS_END) {
			// TODO Auto-generated method stub
			labelNumberOfTour.setText("Button click");

			computer.readStatusFromFile();
			if (computer.GameState != computer.GS_END) {
				computer.Play();

				computer.saveStatusToFile();
				System.out.println("Save to file ok");
				computer.isMyTurn = false;
				numberOfTour++;
				labelNumberOfTour.setText("Number of Tour:"
						+ Integer.toString(numberOfTour));
				draw();

				computer.alertUser();
			}
		}
		else if(computer.GameState == computer.GS_END ){
			System.out.println("Game over");				
			buttonPlay.setText("Replay");
			//computer.Init();
			if (!computer.isInitForReplay)
				computer.InitForReplay();
			computer.showReplay();
		}
	}
	public void start() {
		setVisible(true);
	}

	public static void update() {
		computer.readStatusFromFile();
		for (int x = 0; x < BOARDSIZE; x++)
			for (int y = 0; y < BOARDSIZE; y++) {
				computer.Cells[x][y].repaint();
			}
	}
	
	public static  void draw() {
		update();
//		repaint();
	}	
}
