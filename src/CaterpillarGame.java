/* File       : CaterpillarGame.java
 * Author     : Brian Fiala
 * Date       : 10/26/14
 * Attribution: some code used with permission from Professor Mousallam,
 *              CS1C Data Structures and Algorithms, Foothill Community College
 * Description: CaterpillarGame is a java game wherein two players competitively
 *              control caterpillars with the goal of eating food pellets to 
 *              sustain their lives and increase their scores. 
 * Controls   : Player One:
 *                         'a' move left
 *                         's' move down
 *                         'd' move right
 *                         'w' move up
 *                         'q' quit
 *              Player Two:
 *                         'j' move left
 *                         'k' move down
 *                         'l' move right
 *                         'i' move up
 *                         'p' quit
 * Note       : Game ends when one player loses all of its body segments, or 
 *              when a player quits
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;


public class CaterpillarGame extends Frame {
   // default serialUID
   private static final long serialVersionUID = 1L;
   
   final static int BOARD_WIDTH = 60;
   final static int BOARD_HEIGHT = 40;
   final static int SEGMENT_SIZE = 10;
   final static Font scoreFont = new Font("Lucida Grande", Font.BOLD, 12);
   final static Font gameOverFont = new Font("Lucida Grande", Font.BOLD, 24);
   
   // player objects
   private Caterpillar playerOne;
   private Caterpillar playerTwo;
   
   // goal object: FoodPellet
   private FoodPellet yummy = new FoodPellet();
   
   private int playerLost = 0; // stores game ended information
   
   public static void main(String[] args) {
      CaterpillarGame world;
      boolean playAgain = true;
      while(playAgain) {
         world = new CaterpillarGame();
         world.setVisible(true);
         playAgain = world.run();
      }
   } // end main
   
   public CaterpillarGame() {
      playerOne = new Caterpillar(Color.blue, new Point(20, 10), 1);
      playerTwo = new Caterpillar(Color.red, new Point(20, 30), 2);
      setSize((BOARD_WIDTH + 1) * SEGMENT_SIZE, 
            BOARD_HEIGHT * SEGMENT_SIZE + 50);
      setTitle("Caterpillar Game");
      setLocationRelativeTo(null);
      addKeyListener(new KeyReader());
      addWindowListener(new CloseQuit());
   } // end constructor
   
   public boolean run() { // returns true if the users want to begin a new game
      while (playerLost == 0) { // will keep running until a player has lost
         movePieces(); // movePieces changes playerLost when the game has ended
         repaint();
         try {
            Thread.sleep(100);
         }
         catch (Exception e) {}
      }
      
      // When program reaches this line, someone has lost or quit. 
      // Display results and ask if they would like to play again
      String message = "";
      if (playerLost == 1) { // player one lost
         message = "Player One has run out of body segments!" + 
               "\nPlayer Two Wins! Score: " + playerTwo.getScore() +
               "\nPlayer One's score: " + playerOne.getScore() + 
               "\nWould you like to play again?";
      }
      else if (playerLost == 2) { // player two lost
         message = "Player Two has run out of body segments!" + 
               "\nPlayer One Wins! Score: " + playerOne.getScore() +
               "\nPlayer Two's score: " + playerTwo.getScore() + 
               "\nWould you like to play again?";
      }
      else if (playerLost == 3) { // player one quit
         message = "Player One has quit!" + 
               "\nPlayer Two Wins! Score: " + playerTwo.getScore() +
               "\nPlayer One's score: " + playerOne.getScore() + 
               "\nWould you like to play again?";
      }
      else  { // player two quit
         message = "Player Two has quit!" + 
               "\nPlayer One Wins! Score: " + playerOne.getScore() +
               "\nPlayer Two's score: " + playerTwo.getScore() + 
               "\nWould you like to play again?";
      }
      
      // return result controls whether a new game is begun or if the game exits
      return (JOptionPane.showConfirmDialog(null, message, "Game Over!",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
   } // end run
   
   public void paint(Graphics g) {
      // draw the limit line below which is displayed the score
      g.setColor(Color.black);
      g.setFont(scoreFont);
      g.drawLine(0, BOARD_HEIGHT * SEGMENT_SIZE + 30, 
            (BOARD_WIDTH + 1) * SEGMENT_SIZE, BOARD_HEIGHT * SEGMENT_SIZE + 30);
      
      // display playerOne score
      g.drawString("Player One score: " + 
            Integer.toString(playerOne.getScore()), 
            10 * SEGMENT_SIZE, BOARD_HEIGHT * SEGMENT_SIZE + 45);
      // display playerTwo score
      g.drawString("Player Two score: " + 
            Integer.toString(playerTwo.getScore()), 
            35 * SEGMENT_SIZE, BOARD_HEIGHT * SEGMENT_SIZE + 45);
      
      // draw the FoodPellet
      yummy.paint(g);
      
      // draw the caterpillars
      playerOne.paint(g);
      playerTwo.paint(g);
   } // end paint
   
   public void movePieces() {
      // move method in Caterpillar increments score for eating a pellet
      // and if so, calls yummy to generate a new pellet 
      // playerLost controls end-game flow: becomes non-zero when a player 
      // loses or quits
      playerLost = playerOne.move(this, yummy); 
      if (playerLost == 0) { // if player one has not lost, move playerTwo
         playerLost = playerTwo.move(this, yummy); // and see if they lost
      }
   } // end movePieces
   
   public boolean canMove(Point np) {
      int x = np.x;
      int y = np.y;
      
      // test playing board
      if ((x <= -1) || (y <= 0)) return false;
      if ((x >= BOARD_WIDTH) || (y >= BOARD_HEIGHT +1)) return false;
      
      // test caterpillars
      if (getPlayerOne().inPosition(np)) return false;
      if (getPlayerTwo().inPosition(np)) return false;
      
      // otherwise, ok: safe square
      return true;
   } // end canMove
   
   public Caterpillar getPlayerOne() {
      return playerOne;
   }
   
   public Caterpillar getPlayerTwo() {
      return playerTwo;
   }
   
   private class KeyReader extends KeyAdapter {
      public void keyPressed(KeyEvent e) {
         char c = e.getKeyChar();
         switch(c) {
            case 'q': getPlayerOne().setDirection('Z'); break;
            case 'a': getPlayerOne().setDirection('W'); break;
            case 'd': getPlayerOne().setDirection('E'); break;
            case 'w': getPlayerOne().setDirection('N'); break;
            case 's': getPlayerOne().setDirection('S'); break;
            
            case 'p': getPlayerTwo().setDirection('Z'); break;
            case 'j': getPlayerTwo().setDirection('W'); break;
            case 'l': getPlayerTwo().setDirection('E'); break;
            case 'i': getPlayerTwo().setDirection('N'); break;
            case 'k': getPlayerTwo().setDirection('S'); break;
         }
      } // end keyPressed
   } // end inner class KeyReader
   
   private class CloseQuit implements WindowListener {
      @Override
      public void windowOpened(WindowEvent e) { }
      
      @Override
      public void windowClosing(WindowEvent e) {
         System.exit(0); // just quit
      }
      
      @Override
      public void windowClosed(WindowEvent e) {
         System.exit(0); // just quit
      }
      
      @Override
      public void windowIconified(WindowEvent e) { 
         System.exit(0); // just quit
      }
      
      @Override
      public void windowDeiconified(WindowEvent e) { }
      
      @Override
      public void windowActivated(WindowEvent e) { }
      
      @Override
      public void windowDeactivated(WindowEvent e) { }
   } // end inner class CloseQuit
   
} // end class CatepillarGame
