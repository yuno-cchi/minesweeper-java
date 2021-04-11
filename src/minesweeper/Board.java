package minesweeper;

import java.awt.Color; // colors
import javax.swing.BorderFactory; //button borders
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame; //draw window
import java.awt.GridLayout; //for field
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel; // draw field
import javax.swing.SwingUtilities;
import java.util.Random; // mine placement

@SuppressWarnings("serial")
public class Board extends JFrame {
	
	Tile[][] board = new Tile[10][10]; //assignment asked for 10x10 i think   
	private int NUM_BOMBS = 20; //windows 9x9 has 10 so think this is a good scale-up
	private int toClear = 100 - NUM_BOMBS; // number of tiles you have to open, used for win state
	
	JButton resetButton = new JButton();
	
	private boolean firstClick = true; // game initialises to a non-started state
	//handles first click logic and whether or not the timer is running
	
	private boolean gameOver = false; // toggles to true when user wins/loses 
	
	Random rand = new Random();  // RNG :)
	
	public int MineCount(int r, int c) { // counts number of tiles adjacent to opened tile, for sprite logic
		
		// lord forgive me.....
		// this is bad form and is really ugly, and people shouldn't do this
		// too bad!
		
		
		int count = 0;
		
		try {
			if (board[r-1][c-1].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r-1][c].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r-1][c+1].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r][c-1].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r][c+1].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r+1][c-1].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r+1][c].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		try {
			if (board[r+1][c+1].isBomb()) { count += 1; }
		} catch (ArrayIndexOutOfBoundsException e) {/*do nothing lmao*/}
		
		return count;
	}
	
	public void PlaceMines() { // randomly places mines throughout the board
		int placed = 0;
		
		// wow this looks ugly and inefficient
		// too bad!
		
		do { // while there are still bombs to place...
			
			for (int r = 0; r <= 9; r++) { 
				for (int c = 0; c <= 9; c++) { 
					
					if (placed == NUM_BOMBS) { break; }
					double randomDouble = rand.nextDouble();
					
					if (!board[r][c].isBomb() && (randomDouble < 0.05)) { // if the tile isn't currently a bomb...
						board[r][c].setBomb(true); // give the current tile a 5% chance of becoming a bomb
						placed += 1; // and increment the number of bombs placed by 1
						
					}
					
				}
			}
		} while (placed < NUM_BOMBS);
		
		System.out.println(String.format("%d mines placed", placed));
		
		 
	}
	
	public void YouWin() { // win state
		gameOver = true;
		resetButton.setIcon(new ImageIcon("src/resources/resetShades.png"));
		System.out.println("you WIN!!!");
	}
	
	public void YouLose() { // lose screen: shows loser where the mines they haven't set off/flagged are and any wrongly flagged tiles
		
		for (int r = 0; r <= 9; r++) {
			for (int c = 0; c <= 9; c++) { // for every tile on the board...
				
				if (!board[r][c].isBomb() && board[r][c].isFlagged()) { // tile is not a bomb, but is flagged
					board[r][c].setSprite(13); //draw the wrong flag sprite on it
					continue;
				}
				
				if (board[r][c].isBomb() && board[r][c].isStillHidden() && !board[r][c].isFlagged()) { //tile is a bomb and hasn't been opened yet and isn't flagged
					board[r][c].setSprite(12);
					continue;
				}
				
			}
			
		} // HEY YOU
		  // DONT PUT THE CODE FOR LOSING INTO THE FOR LOOP
		  // YOU BUMBLING IDIOT
		gameOver = true;
		resetButton.setIcon(new ImageIcon("src/resources/resetOuch.png"));
	}
	
	public void ReplaceThisMine(Tile t) { // first click will never be a mine
	
		System.out.println("attempting bomb replace...");
		
		boolean bombReplaced = false;
		t.setBomb(false);
		
		do {
			// this is going to make our time complexity O(n!)
			// too bad!
			
			board[t.getR()][t.getC()].setBomb(false);
			
			int randR = rand.nextInt(10); //random R coordinate between 0 and 9
			int randC = rand.nextInt(10); //random C coordinate between 0 and 9
			
			if (!board[randR][randC].isBomb()) { // if the random tile isn't a bomb, make it a bomb
				
				if (randR == t.getR() && randC == t.getC()) { continue; } //just so we dont re-place the bomb into the same tile
				board[randR][randC].setBomb(true);
				bombReplaced = true;
				break;
				
			} else {
				continue; // otherwise, repeat the loop
			}
			
		} while (!bombReplaced);
		
		firstClick = false;
		
	}
	
	public void Open(Tile t) { // open a tile! (left click)
		
		if (!gameOver) {
			if (!t.isFlagged() && t.isStillHidden()) { // if tile isn't flagged and hasn't already been opened...
				
				if (firstClick && t.isBomb()) { //if the user opens a bomb on their first click, change the position of this bomb
					ReplaceThisMine(t);
				} else {
					firstClick = false;
				}
				
				
				t.toggleHidden(); // we open the tile
				
				System.out.println(String.format("%d,%d,%s: %d mines adjacent. %d to clear", t.getR(), t.getC(), (t.isBomb()? " bomb" : " not bomb"), MineCount(t.getR(), t.getC()), toClear-1));
				// ^^ this prints stuff to console for debugging purposes
				
				
				// if the player hasn't won or lost
				t.setSprite(t.isBomb()? 9 : MineCount(t.getR(), t.getC())); // if tile is a bomb, draw a bomb, else, write no. of bombs adjacent
				
				
				if (t.isBomb()) {
					System.out.println("boom");
					YouLose(); 
				}
				
				if (!t.isBomb()) {
					toClear -= 1; // dont decrement toClear if user clicks a mine (prevents simultaneous win and loss)
				} 
				
				if (toClear == 0) {
					YouWin();
				}
				
				if (MineCount(t.getR(), t.getC()) == 0) { // if this tile has 0 adjacent mines
					FloodOpen(t); // recursive neighbour opening
				}
				
			}
		}
		
	}
	
	public void FloodOpen(Tile t) { // neighbour tile recursive opening algorithm for tiles with 0 bombs
		
		try {
			Open(board[t.getR()-1][t.getC()-1]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()-1][t.getC()]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()-1][t.getC()+1]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()][t.getC()-1]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()][t.getC()+1]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()+1][t.getC()-1]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()+1][t.getC()]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
		try {
			Open(board[t.getR()+1][t.getC()+1]);
		} catch (ArrayIndexOutOfBoundsException e) {/* do nothing lmao */}
		
	}
	
	public void Flag(Tile t) { // flag a tile! (right click)
		
		if (t.isStillHidden() && !gameOver) { //if the tile hasn't been opened yet... 
			t.toggleFlag(); //toggles flag
			
			t.setSprite(t.isFlagged()? 10: 11);
		}
	}
	
	public void newGame() { //make JFrame here
			
				this.setTitle("Minesweeper");
				this.setIconImage(new ImageIcon("src/resources/12.png").getImage());
				this.setSize(606,800);
				this.setResizable(false);
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //so it actually exits when you tell it to exit
				this.setLayout(null);
				this.setBackground(Color.LIGHT_GRAY);
				
				
				
				//add the field here
				JPanel field = new JPanel();
				field.setBackground(Color.WHITE);
				field.setBounds(20, 190, 550, 550);
				GridLayout grid = new GridLayout(10,10);
				
				//layout manager!
				field.setLayout(grid);
				
				//reset Button!
				MouseListener resetListener = new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {}
						
					@Override
					public void mouseReleased(MouseEvent e) {}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						if(!gameOver) {resetButton.setIcon(new ImageIcon("src/resources/resetSmileHELD.png"));}
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						if(!gameOver) {resetButton.setIcon(new ImageIcon("src/resources/resetSmile.png"));}
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						
						if (SwingUtilities.isLeftMouseButton(e)) { // on left click...
							ResetGame();
						}
					}
				};
				resetButton.setBounds(240, 40, 120, 120);
				resetButton.addMouseListener(resetListener);
				resetButton.setBorder(BorderFactory.createEmptyBorder());
				resetButton.setIcon(new ImageIcon("src/resources/resetSmile.png"));
				this.add(resetButton);
				
				
				//let's make buttons
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board[0].length; col++) {
						
						Tile t = new Tile(row, col);
						t.setBorder(BorderFactory.createEmptyBorder());
						field.add(t); //adds to the field
						board[row][col] = t; //adds the tile to the position matrix Tile[][] board so we can call it later
						t.setC(col);
						t.setR(row);
						
						MouseListener tileListener = new MouseListener() {
							
							@Override
							public void mouseClicked(MouseEvent e) {}
								
							@Override
							public void mouseReleased(MouseEvent e) {}
							
							@Override
							public void mouseEntered(MouseEvent e) {}
							
							@Override
							public void mouseExited(MouseEvent e) {}
							
							@Override
							public void mousePressed(MouseEvent e) {
								
								if (SwingUtilities.isLeftMouseButton(e)) { // on left click...
									Open(t);
								}
								
								if (SwingUtilities.isRightMouseButton(e)) { // on right click...
									Flag(t);
								}
							}
						};
						
						t.addMouseListener(tileListener);
					}
				}
				
				this.add(field);
				this.setVisible(true);	
				
				PlaceMines();	
	}
	
	@SuppressWarnings("unused")
	public void ResetGame() {  // resets game, used for reset button
		Board newGame = new Board();
		this.dispose();
	}
	
	public Board() { //draws the actual board
		newGame();
	}
	
}