/* File       : Caterpillar.java
 * Author     : Brian Fiala
 * Date       : 10/26/14
 * Description: These are the player objects for the class CaterpillarGame.
 *              Each one keeps track of the position of each of their body
 *              segments, the commands in their queue, and the direction they
 *              are going.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class Caterpillar {
   private Color color;
   private Point position;
   private char direction = 'E';
   private int score;
   private Queue<Point> body = new LinkedList<Point>();
   private Queue<Character> commands = new LinkedList<Character>();
   private int counter; // for tracking the number of moves between 
   private final int COUNTER_START = 20;
   private final int playerNum;
   
   public Caterpillar(Color c, Point sp, int player) {
      playerNum = player;
      setScore(0);
      counter = COUNTER_START;
      color = c;
      for (int i = 0; i < 10; i++) {
         position = new Point(sp.x + i, sp.y);
         body.add(position);
      }
   } // end constructor
   
   public void setDirection(char d) {
      commands.add(new Character(d));
   } // end setDirection
   
   // returns a number if game is over: 1: player one lost, 2: player two lost
   // 3: player one quit, 4: player two quit
   public int move(CaterpillarGame game, FoodPellet yummy) {
      // first see if we should change direction
      if (commands.size() > 0) { // if empty, keep moving in same direction
         Character c = (Character) commands.remove();
         direction = c.charValue(); // set new direction
         if (direction == 'Z') {
            if (playerNum == 1) return 3; // player one quit
            else return 4; // player two quit
         }
      }
      
      // decrement counter
      counter--;
      
      // then find new position
      Point np = newPosition();
      if (game.canMove(np)) { // checks with world to see if space is occupied
         body.add(np); // in all cases, move forward (add segment) 
         position = np; // update position instance variable
         
         // check if caterpillar player got the pellet
         if (np.x == yummy.getNumPoint().x && np.y == yummy.getNumPoint().y) {
            atePellet(yummy); // increments score and resets counter
            // since ate pellet, do not remove a segment (equiv to adding)
            yummy.nextPellet(game.getPlayerOne(), game.getPlayerTwo());
            return 0;
         }
         else { // player can move, but did not get pellet
            body.remove(); // remove old body segment as normal
         }
      }
      
      // regardless if player could move, beyond this, player did not get pellet
      if (counter <= 0) { // counter expired
         counter = COUNTER_START; // reset counter
         // always at least one seg left, don't need to check if empty
         body.remove(); 
         if (body.isEmpty()) { // case no more segments = game over
            return playerNum; // playerNum lost
         }
      }
      
      // if method reaches this point, game continues, no one lost or quit
      return 0;
   } // end move
   
   private Point newPosition() {
      int x = position.x;
      int y = position.y;
      if (direction == 'E') x++;
      else if (direction == 'W') x--;
      else if (direction == 'N') y--;
      else if (direction == 'S') y++;
      
      // added this line for keyboard quit functionality
      else if (direction == 'Z') System.exit(0);
      
      return new Point(x, y);
   } // end newPosition
   
   public boolean inPosition(Point np) {
      Iterator<Point> iter = body.iterator();
      while (iter.hasNext()) {
         Point location = iter.next();
         if (np.equals(location)) return true;
      }
      return false;
   } // end inPosition
   
   // 
   public void atePellet(FoodPellet yummy) {
      setScore(getScore() + yummy.getNumValue());
      counter = COUNTER_START; // reset counter
   }
   
   public void paint(Graphics g) {
      g.setColor(color);
      Iterator<Point> iter = body.iterator();
      
      //iterator stuff
      while (iter.hasNext()) {
         Point p = iter.next();
         g.setColor(color);
         g.fillOval(5 + CaterpillarGame.SEGMENT_SIZE * p.x,
               15 + CaterpillarGame.SEGMENT_SIZE * p.y,
               CaterpillarGame.SEGMENT_SIZE, CaterpillarGame.SEGMENT_SIZE);
         g.setColor(Color.black);
         g.drawOval(5 + CaterpillarGame.SEGMENT_SIZE * p.x,
               15 + CaterpillarGame.SEGMENT_SIZE * p.y,
               CaterpillarGame.SEGMENT_SIZE, CaterpillarGame.SEGMENT_SIZE);
      }
   } // end paint
   
   public int getScore() {
      return score;
   }
   
   public void setScore(int score) {
      if (score >= 0) this.score = score;
   }
} // end class Caterpillar
