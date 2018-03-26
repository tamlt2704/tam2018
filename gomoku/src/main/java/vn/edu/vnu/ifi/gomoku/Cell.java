package vn.edu.vnu.ifi.gomoku;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;



public class Cell extends JButton implements ActionListener {

	public Pos pos; 
	public int state;
	static final int EMPTY = 0;
	static final int WHITE = 1;//0
	static final int BLACK = 2;//x
	
	public Cell() {
		pos = new Pos();
	}
	
	public Cell (int x, int y, int state) {
		pos = new Pos(x, y);
		this.state = state;
	}
	
	public void setValue(int x, int y, int state)
	{
		pos.x = x;
		pos.y = y;
		this.state = state;
	}
	
	public void setIcon(int state) {
		Image img = null;
		try {
			switch (state) {		
			case EMPTY:				
				  img = ImageIO.read(getClass().getResource("/images/empty.jpeg"));
				break;
			case WHITE:
				  img = ImageIO.read(getClass().getResource("/images/o.jpeg"));
				break;
			case BLACK:
				  img = ImageIO.read(getClass().getResource("/images/x.jpeg"));
				break;
			default:
				img = ImageIO.read(getClass().getResource("/images/empty.jpeg"));
				break;
			}
			
			setMargin(new Insets(0, 0, 0, 0));
			// to add a different background
			setBackground( Color.WHITE);
			// to remove the border
			//setBorder(null);			
			
		    if (img!=null)
		    	setIcon(new ImageIcon(img));
		    
		  } catch (IOException ex) {
		  }
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Button click");
	}
}
