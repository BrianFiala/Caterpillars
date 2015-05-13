/* File       : FoodPellet.java
 * Author     : Brian Fiala
 * Date       : 10/26/14
 * Description: An instance of this class is the goal object for the 
 *              CaterpillarGame. Each time it is consumed, a new location is 
 *              generated and the score is incremented by one.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;


public class FoodPellet {
   
   private Point numPoint = new Point(0, 0); // number square location
   private int numValue; // number square value
   private final static Font myFont = new Font("Courier", Font.BOLD, 8);
   
   public FoodPellet() {
      getNumPoint().x = (int) (Math.random() * 59); // 0-59
      getNumPoint().y = (int) (Math.random() * 40) + 1; // 1-40
      setNumValue(1);
   } // end constructor
   
   public Point getNumPoint() {
      return numPoint;
   }
   
   public void setNumPoint(Point numPoint) {
      this.numPoint = numPoint;
   }
   
   public int getNumValue() {
      return numValue;
   }
   
   public void setNumValue(int numValue) {
      this.numValue = numValue;
   }
   
   public void nextPellet(Caterpillar playerOne, Caterpillar playerTwo) {
      // create a possible new pellet location
      Point newPoint = new Point((int) (Math.random() * 59), 
            (int) (Math.random() * 40) + 1);
      
      // if/while new location is occupied, generate a new location
      while ((playerOne.inPosition(newPoint)) 
            || playerTwo.inPosition(newPoint)) {
         newPoint = new Point((int) (Math.random() * 59), 
               (int) (Math.random() * 40) + 1);
      }
      
      // update private instance data
      setNumPoint(newPoint);
      numValue++; // increment score value
   } // end nextPellet
   
   public void paint(Graphics g) {
      // draw the pellet
      g.setColor(Color.yellow);
      g.fillOval(4 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().x, 
            14 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().y,
            CaterpillarGame.SEGMENT_SIZE + 2, CaterpillarGame.SEGMENT_SIZE + 2);
      g.setColor(Color.black);
      g.drawOval(4 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().x, 
            14 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().y,
            CaterpillarGame.SEGMENT_SIZE + 2, CaterpillarGame.SEGMENT_SIZE + 2);
      // draw the score number inside the pellet
      g.setFont(myFont);
      g.setColor(Color.black);
      if (getNumValue() < 10) {
         g.drawString(Integer.toString(getNumValue()), 
               8 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().x, 
               23 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().y);
      }
      else {
         g.drawString(Integer.toString(getNumValue()), 
               6 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().x, 
               23 + CaterpillarGame.SEGMENT_SIZE * getNumPoint().y);
      }
   } // end paint
} // end class FoodPellet
