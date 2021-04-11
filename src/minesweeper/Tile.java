package minesweeper;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial") //shut up about serial IDs eclipse

public class Tile extends JButton {
	
	ImageIcon[] sprites = new ImageIcon[14];
	
	private int r, c; //position -- row and column values
	private boolean isBomb; //is this tile a bomb?
	private boolean isFlagged; // has this tile been flagged?
	private boolean stillHidden; // has the user clicked on this tile yet? if so, flase
	
	public Tile(int r, int c) { //constructor
		this.r = r;
		this.c = c;
		this.stillHidden = true;
		
		
		for (int i = 0; i < 14; i++) {
			sprites[i] = new ImageIcon(String.format("src/resources/%d.png", i));
		}
		
		this.setIcon(sprites[11]);
		
	}
	
	public void setSprite(int num) {
		
		/*
		 * sprite codes:
		 * 0: uncovered, no bombs
		 * 1-8: n bombs (not that kind of n bomb)
		 * 9: bomb, exploded
		 * 10: flag
		 * 11: covered
		 * 12: bomb, solved (for loss screen)
		 * 13: wrongly marked bomb (for loss screen)
		*/
		
		this.setIcon(sprites[num]);
		
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}
	
	public boolean isBomb() {
		return isBomb;
	}

	public void setBomb(boolean isBomb) {
		this.isBomb = isBomb;
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
	
	public void toggleFlag() {
		isFlagged = !isFlagged;
	}
	
	public boolean isStillHidden() {
		return stillHidden;
	}
	
	public void toggleHidden() {
		stillHidden = !stillHidden; //toggles stillHidden
	}
	
	
}
