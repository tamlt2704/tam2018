package vn.edu.vnu.ifi.gomoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

public class Computer {
	public Cell Cells[][];
	static String fileName = "C:\\IFI\\gomoku.txt";
	// static final String fileName = "Gomokugomoku.txt";
	private FileInputStream fileInputStream;
	private DataInputStream dataInputStream;
	private BufferedReader bufferedReader;
	private FileOutputStream fileOutputStream;
	public int numberOfSpices; // max = 400 = 20x20

	public int BOARDSIZE = 20;
	static int MAX_OF_SPECIES = 0;
	public int color = 0;
	public int enemyColor = 0;
	public int Score;

	static final int THINK_OK = 0;
	static final int THINK_FILLED = 1;
	static final int THINK_WIN = 2;
	static final int LIMIT_TOUR = 3;
	static final int THINK_LOSE = 4;

	public boolean limitTour = false;

	public boolean Space;
	public boolean isMyTurn = false;

	public boolean isBoardFilled = false;
	public boolean isWin = false;
	public boolean isLose = false;
	public boolean hasChooseColor = false;
	public boolean isInitForReplay = false;

	public int GameState;
	public final int GS_END = 0;
	public final int GS_PLAYUSER = 1;
	public final int GS_PLAYCOM = 2;
	public final int GS_READY = 3;
	public final int GS_INIT = 4;

	int Dx[];
	int Dy[];

	static final int D_UP = 0;
	static final int D_UPRIGHT = 1;
	static final int D_RIGHT = 2;
	static final int D_DOWNRIGHT = 3;
	static final int D_DOWN = 4;
	static final int D_DOWNLEFT = 5;
	static final int D_LEFT = 6;
	static final int D_UPLEFT = 7;

	public int Area[][];
	static final int AREASIZE = 3;

	public static boolean playWithComputer = false;
	static final int NOT_DANGEREURS = 5;

	public Game gameControl;

	// for replay after the match
	public int saveStatus[][] = new int[20][20];
	public int count = 0;

	public Computer(int boardSize) {
		BOARDSIZE = boardSize;
		MAX_OF_SPECIES = BOARDSIZE * BOARDSIZE;
	}

	public Computer(int boardSize, Game game) {

		BOARDSIZE = boardSize;
		MAX_OF_SPECIES = BOARDSIZE * BOARDSIZE;
		gameControl = game;

	}

	public void Init() {
		fileName = Gomoku.fileName;
		// gameControl = game;
		Dx = new int[8];
		Dy = new int[8];

		Dx[0] = 0;
		Dy[0] = -1;
		Dx[1] = 1;
		Dy[1] = -1;
		Dx[2] = 1;
		Dy[2] = 0;
		Dx[3] = 1;
		Dy[3] = 1;
		Dx[4] = 0;
		Dy[4] = 1;
		Dx[5] = -1;
		Dy[5] = 1;
		Dx[6] = -1;
		Dy[6] = 0;
		Dx[7] = -1;
		Dy[7] = -1;

		Area = new int[BOARDSIZE][BOARDSIZE];

		Cells = new Cell[BOARDSIZE][BOARDSIZE];
		for (int i = 0; i < BOARDSIZE; i++) {
			for (int j = 0; j < BOARDSIZE; j++) {

				saveStatus[i][j] = 0;

				Cells[i][j] = new Cell(i, j, Cell.EMPTY);
				// if(gameControl.checkBox.isSelected() == true)
				Cells[i][j].addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {

						if (gameControl.checkBox.isSelected()) {
							// TODO Auto-generated method stub
							System.out.println("Butoon click");
							Cell tmp = (Cell) e.getSource();
							PutMessage("Enermy color1 = " + enemyColor);
							Put(enemyColor, tmp.pos.x, tmp.pos.y);

							saveStatusToFile();
							gameControl.draw();
							
//							if(Computer.playWithComputer == true)
								Game.thinkAndPlay();
						}
					}
				});
				Cells[i][j].setIcon(Cells[i][j].state);
				Area[i][j] = 0;
			}
		}

		readStatusFromFile();

		GameState = GS_INIT;
	}
	
	public void readStatusFromFile() {
		numberOfSpices = 0;
		// TODO Auto-generated method stub
		// read file here
		
		try {			
			fileInputStream = new FileInputStream(fileName);
			dataInputStream = new DataInputStream(fileInputStream);
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			int i = 0, j = 0, state = 0;
			String line;
			int x1, x2, y1, y2;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				j = 0;
				while (st.hasMoreTokens()) {

					state = Integer.parseInt(st.nextToken());

					Cells[i][j].state = state;
					Cells[i][j].setIcon(state);
					Cells[i][j].repaint();

					if (Cells[i][j].state != Cell.EMPTY)
						saveStatus[i][j]++;

					x1 = (i - AREASIZE < 0) ? 0 : i - AREASIZE;
					x2 = (i + AREASIZE >= BOARDSIZE) ? BOARDSIZE - 1 : i
							+ AREASIZE;
					y1 = (j - AREASIZE < 0) ? 0 : j - AREASIZE;
					y2 = (j + AREASIZE >= BOARDSIZE) ? BOARDSIZE - 1 : j
							+ AREASIZE;
					for (; x1 <= x2; x1++) {
						for (int y = y1; y <= y2; y++) {
							Area[x1][y]++;
						}
					}

					if (state > 0)
						numberOfSpices++;
					j++;
				}
				i++;
				// System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(gameControl, "Error when read file");
			GameState = GS_END;
		}

		// close all
		try {
			fileInputStream.close();
			dataInputStream.close();
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Game state ready");
		GameState = GS_READY;
	}

	public void saveStatusToFile() {
		// PutMessage("When write")
		try {
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);

			for (int i = 0; i < BOARDSIZE; i++) {
				for (int j = 0; j < BOARDSIZE; j++) {

					out.write(Cells[i][j].state + " ");
					// System.out.print(Cells[i][j].state + " ");
				}
				out.write("\n");
				// System.out.println("\n");
			}
			out.close();
			fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void resetFile() {
		try {
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);

			for (int i = 0; i < BOARDSIZE; i++) {
				for (int j = 0; j < BOARDSIZE; j++) {
					out.write("0 ");
				}
				out.write("\n");
			}
			out.close();
			fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getMyColor() {
		numberOfSpices = 0;
		readStatusFromFile();
		color = (numberOfSpices == 0) ? Cell.BLACK : Cell.WHITE;
		enemyColor = (color == Cell.BLACK) ? Cell.WHITE : Cell.BLACK;
	}

	public void Play() {

		if (!hasChooseColor) {
			getMyColor();
			hasChooseColor = true;
			if (gameControl != null)
				if (gameControl.nameOfColor != null) {
					if (color == Cell.BLACK)
						gameControl.nameOfColor.setText("My color is: X ");
					else
						gameControl.nameOfColor.setText("My color is: O ");
				}
		}

		Pos pos = new Pos();

		PutMessage("Computer now thinking...");

		switch (Think(color, pos)) {

		case THINK_OK:
			PutMessage("It's now your turn.");
			break;

		case THINK_FILLED:

			PutMessage("Board is filled ! Draw game.");
			// GameState = GS_END;
			isBoardFilled = true;
			break;

		case THINK_WIN:

			PutMessage("I Win !!!");
			isWin = true;

			break;
		}
		// PutMessage("Last location"+pos.x+ "::"+pos.y);
		if (gameControl != null)
			if (gameControl.lastPosition != null) {
				gameControl.lastPosition.setText("Last location:(" + pos.x
						+ ":" + pos.y + ")");
			}
	}

	int Replay[];
	Pos ReplayPos[];

	public void InitForReplay() {
		PutMessage("Replay match");
		count = 0;
		int tmp;

		Replay = new int[400];
		ReplayPos = new Pos[400];

		int colorTmp = Cell.BLACK;
		// resetFile();

		for (int i = 0; i < BOARDSIZE; i++) {
			for (int j = 0; j < BOARDSIZE; j++) {
				if (saveStatus[i][j] != 0) {
					Replay[count] = saveStatus[i][j];
					ReplayPos[count] = new Pos(i, j);
					// System.out.print(saveStatus[i][j] +" ");
					count++;
					Cells[i][j].state = Cell.EMPTY;
					Cells[i][j].setIcon(Cell.EMPTY);
					Cells[i][j].repaint();
				}
			}
			PutMessage(" ");
		}

		PutMessage("Finish init count = " + count);

		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				if (Replay[i] < Replay[j]) {
					// swap
					Replay[i] += Replay[j];
					Replay[j] = Replay[i] - Replay[j];
					Replay[i] = Replay[i] - Replay[j];
					// swap
					ReplayPos[i].x += ReplayPos[j].x;
					ReplayPos[j].x = ReplayPos[i].x - ReplayPos[j].x;
					ReplayPos[i].x = ReplayPos[i].x - ReplayPos[j].x;
					// swap
					ReplayPos[i].y += ReplayPos[j].y;
					ReplayPos[j].y = ReplayPos[i].y - ReplayPos[j].y;
					ReplayPos[i].y = ReplayPos[i].y - ReplayPos[j].y;
				}
			}
		}

		if (gameControl != null) {
			// gameControl.update();
			gameControl.repaint();
		}

		PutMessage("Finish sort count = " + count);
		for (int i = 0; i < count; i++) {
			PutMessage(ReplayPos[i].x + "  " + ReplayPos[i].y);
		}
		isInitForReplay = true;
	}

	int count2 = 0;
	int colorTmp = Cell.BLACK;

	public void showReplay() {
		if (count2 >= count)
			return;
		Cells[ReplayPos[count2].x][ReplayPos[count2].y].state = colorTmp;
		Cells[ReplayPos[count2].x][ReplayPos[count2].y].setIcon(colorTmp);
		Cells[ReplayPos[count2].x][ReplayPos[count2].y].repaint();
		// get opposite
		colorTmp = (colorTmp == Cell.BLACK) ? Cell.WHITE : Cell.BLACK;
		if (gameControl != null)
			if (gameControl.lastPosition != null)
				gameControl.lastPosition
						.setText("Last position is (" + ReplayPos[count2].x
								+ "," + ReplayPos[count2].y + ")");

		try {
			Thread.sleep(1000);

			if (gameControl != null) {
				PutMessage("Update");
				// gameControl.update();
				Cells[ReplayPos[count2].x][ReplayPos[count2].y].repaint();
				gameControl.invalidate();
				gameControl.validate();
				gameControl.repaint();
			}
			// System.s
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		count2++;
	}

	public void alertUser() {

		if (isLose == true) {
			JOptionPane.showMessageDialog(gameControl, "You are lose");
			GameState = GS_END;
			if (gameControl != null)
				if (gameControl.buttonPlay != null)
					gameControl.buttonPlay.setText("Replay");
		}

		if (gameControl.numberOfTour >= 100) {
			JOptionPane.showMessageDialog(gameControl, "Limit number of tour");
			GameState = GS_END;
			if (gameControl != null)
				if (gameControl.buttonPlay != null)
					gameControl.buttonPlay.setText("Replay");
		}

		if (isWin == true) {
			JOptionPane.showMessageDialog(gameControl, "I'm win");
			GameState = GS_END;
			if (gameControl != null)
				if (gameControl.buttonPlay != null)
					gameControl.buttonPlay.setText("Replay");
		}
		if (isBoardFilled == true) {
			JOptionPane.showMessageDialog(gameControl, "Board is filled, Stop");
			GameState = GS_END;
		}

	}

	public static void PutMessage(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}

	public void Put(int color, int x, int y) {
		if (Cells[x][y].state == Cell.EMPTY) {
			Cells[x][y].state = color;
			numberOfSpices++;

			int x1, x2, y1, y2;
			x1 = (x - AREASIZE < 0) ? 0 : x - AREASIZE;
			x2 = (x + AREASIZE >= BOARDSIZE) ? BOARDSIZE - 1 : x + AREASIZE;
			y1 = (y - AREASIZE < 0) ? 0 : y - AREASIZE;
			y2 = (y + AREASIZE >= BOARDSIZE) ? BOARDSIZE - 1 : y + AREASIZE;
			for (; x1 <= x2; x1++) {
				for (y = y1; y <= y2; y++) {
					Area[x1][y]++;
				}
			}
		}
	}

	public void remove(int x, int y) {

		if (Cells[x][y].state != Cell.EMPTY) {

			Cells[x][y].state = Cell.EMPTY;
			numberOfSpices--;

			int x1, x2, y1, y2;
			x1 = (x - AREASIZE < 0) ? 0 : x - AREASIZE;
			x2 = (x + AREASIZE >= BOARDSIZE) ? BOARDSIZE - 1 : x + AREASIZE;
			y1 = (y - AREASIZE < 0) ? 0 : y - AREASIZE;
			y2 = (y + AREASIZE >= BOARDSIZE) ? BOARDSIZE - 1 : y + AREASIZE;
			for (; x1 <= x2; x1++) {
				for (y = y1; y <= y2; y++) {
					Area[x1][y]--;
				}
			}
		}

	}

	public int Think(int color, Pos Best) {

		if (gameControl.numberOfTour >= 100)
			return LIMIT_TOUR;

		int x, y;
		int BestScore = 0;
		int eBestScore = 0;
		int eBestX = 0;
		int eBestY = 0;
		int ecolor = (color == Cell.WHITE) ? Cell.BLACK : Cell.WHITE;
		Best.x = 0;
		Best.y = 0;

		if (numberOfSpices == 0) {
			PutMessage("I'm the first ");
			Best.x = BOARDSIZE / 2;
			Best.y = BOARDSIZE / 2;

			Put(color, Best.x, Best.y);
			return THINK_OK;
		}

		for (x = 0; x < BOARDSIZE; x++) {
			for (y = 0; y < BOARDSIZE; y++) {
				if (Cells[x][y].state == Cell.EMPTY && Area[x][y] > 0) {
					Put(color, x, y);

					if (GetScore(color, x, y) == NOT_DANGEREURS) {
						if (Score > BestScore) {
							BestScore = Score;
							Best.x = x;
							Best.y = y;
						}
					}
					remove(x, y);

					Put(ecolor, x, y);
					if (GetScore(ecolor, x, y) == NOT_DANGEREURS) {
						if (Score > eBestScore) {
							eBestScore = Score;
							eBestX = x;
							eBestY = y;
						}
					}
					remove(x, y);
				}
			}
		}

		if (BestScore >= SC_WIN) {
			Put(color, Best.x, Best.y);
			return THINK_WIN;
		}

		if (eBestScore >= SC_WIN) {
			Best.x = eBestX;
			Best.y = eBestY;
		} else if (eBestScore >= SC_WIN2) {
			if (BestScore < SC_WIN2) {
				Best.x = eBestX;
				Best.y = eBestY;
			}
		} else {
			if (eBestScore > BestScore) {
				Best.x = eBestX;
				Best.y = eBestY;
			}
		}

		Put(color, Best.x, Best.y);

		eBestScore = 0;
		for (x = 0; x < BOARDSIZE; x++) {
			for (y = 0; y < BOARDSIZE; y++) {
				if (Cells[x][y].state == ecolor) {
					GetScore(ecolor, x, y);
					if (eBestScore < Score) {
						eBestScore = Score;
					}

				}
			}
		}
		if (eBestScore >= SC_WIN) {
			System.out.println("You lose");
			isLose = true;
			return THINK_LOSE;
		}

		if (numberOfSpices == MAX_OF_SPECIES)
			return THINK_FILLED;

		return THINK_OK;
	}

	final int SC_WIN = 10000000;
	final int SC_WIN2 = 1000000;

	int GetScore(int color, int x, int y) {

		int d1, d2;
		int check2open = 0;
		int check3open = 0;
		int check3close = 0;
		int check4open = 0;
		int check4close = 0;
		int check5open = 0;
		int check5close = 0;
		// int check5 = 0;
		int check6 = 0;
		int checklong = 0;
		int num = 0;
		boolean space1;

		Score = 0;

		for (d1 = D_UP, d2 = D_DOWN; d1 <= D_DOWNRIGHT; d1++, d2++) {

			num = GetSequence(color, x, y, d1);

			space1 = Space;

			num += GetSequence(color, x, y, d2) - 1;

			switch (num) {
			case 2:
				if (space1 && Space)
					check2open++;
				break;

			case 3:
				if (space1 && Space)
					check3open++;
				else if (space1 || Space)
					check3close++;
				break;

			case 4:
				if (space1 && Space)
					check4open++;
				else if (space1 || Space)
					check4close++;
				break;
			case 5:
				if (space1 && Space)
					check5open++;
				else if (space1 || Space)
					check5close++;
				break;
			case 6:
				check6++;
				break;

			default:
				if (num > 6)
					checklong++;

				break;
			}
		}

		// System.out.println("Une ligne 6 ++"+checklong + "::"+check6);
		if (check6 + checklong > 0) {
			Score = SC_WIN;
		} else if (check5open > 0) {
			// System.out.println("Exist une line avec 5 open");
			Score = SC_WIN2;
		} else if (check5close >= 2) {
			Score = SC_WIN;
		} else if (check5close > 0) {
			Score = SC_WIN2;
		} else if (check4open > 0) {
			// System.out.println("Exist check 4 open");
			Score = 90000;
		} else if (check4close >= 2) {
			Score = 60000;
		} else if ((check4open + check5close) > 0) {
			// XXXX 0XXXX //XXX
			Score = SC_WIN2;
		} else if (check3open >= 2) {
			Score = 30000;
		} else if (check4close > 0) {
			Score = 40000;
		} else if (check3open + check3close >= 2) {
			Score = 1200;
		} else if (check3open > 0) {
			Score = 1000 * check3open;
		} else if (check3close >= 2) {
			Score = 120;
		} else if (check2open > 0) {
			Score = 100 * check2open;
		} else if (x == 7 && y == 7) {
			Score = 10;
		} else {
			Score = 1;
		}
		return NOT_DANGEREURS;
	}

	public int GetSequence(int color, int x, int y, int direction) {

		int num = 0;
		int dx = Dx[direction];
		int dy = Dy[direction];

		Space = false;

		while (Cells[x][y].state == color) {
			num++;
			x += dx;
			y += dy;
			if (x < 0 || x >= BOARDSIZE || y < 0 || y >= BOARDSIZE)
				break;
			if (Cells[x][y].state == Cell.EMPTY) {
				Space = true;
				break;
			}
		}
		return num;
	}

	int num2open, num3open, num3close, num4open, num4close, num5;

}
