package util;

import java.util.ArrayList;

/**
 * This class creates a node object for appropriate use in the miniMax with alpha-beta 
 * pruning algorithm.
 * 
 * @author nbigger, olohe
 *
 */

public class Node {

  private static final int NUM_COLS = 7, NUM_ROWS = 6;

  private byte[][] rack;
  private int action;
  private int depth;
  private int eval;

  /**
   * Creates a new Node object from an initial state
   * 
   * @param s the initial state of the rack being created
   */
  public Node(byte[][] s) {
    rack = s;
    action = -1;
    eval = eval(rack);
    depth = 0;
  }

  /**
   * Creates a new Node object and evaluates the current state
   * 
   * @param s the initial state of the rack being created
   * @param a the action that got to the current node from the previous state
   * @param d the depth of the current node
   */
  public Node(byte[][] s, int a, int d) {
    rack = s;
    action = a;
    eval = eval(rack);
    depth = d;
  }

  /**
   * Returns the action that resulted in the current state
   * @return  The column that was last played
   */
  public int getAction() {
    return this.action;
  }

   /**
   * Returns the evaluation of the rack based on token positions
   * @return  A number corresponding to how "good" or "bad" a board is
   */
  public int getEval() {
    return this.eval;
  }

 /**
   * Is this the end of the game? If not, have we hit the depth limit?
   * @return true if we've reached the max depth or a finished game
   */
  public boolean cutoffTest(int maxDepth) {
    if (terminalTest(this.rack)) { return true; }
    if (this.depth >= maxDepth) { return true; }

    return false;
  }

  /**
   * Returns the list of new nodes that could result from the current state
   * @return An array list containing possible nodes generated from
   *         legal columns to be played
   */
  public ArrayList<Node> generateActions() {
    ArrayList<Node> states = new ArrayList<>();

    for(int col = 0; col < this.rack[0].length; col++) {
      byte[][] nextMove = dropToken(col, copyRack(this.rack));
      if(nextMove != null) {
        states.add(new Node(nextMove, col, this.depth+1));
      }
    }

    return states;
  }


  /*--------------------------------------------------------------*/

  /**
   * Returns the evaluation of the current rack by searching all combinations of four spaces
   * @return An int corresponding to how "good" or "bad" a rack is
   */
  private int eval(byte[][] rack) {
    return checkHorizontal(rack) + checkVertical(rack) + checkDiagonal(rack);
  }

    /**
   * Returns the result of dropping a token in a column
   * @author alchambers
   * @return The rack that results from dropping a token in a column
   */
  private static byte[][] dropToken(int column, byte[][] rack) {
    if (!isColumnPlayable(column, rack)){
      return null;
    }

    // determine how far it drops down
    int row = 0;
    for (row = 0; row < rack.length-1; row++) {
      if(rack[row+1][column] != 0){
        break;
      }
    }

    rack[row][column] = 1;

    return rack;
  }

  /**
  * Checks if the given column can be played on
  * @author alchambers
  */
  private static boolean isColumnPlayable(int column, byte[][] rack) {
    return rack[0][column] == 0;
  }

   /**
   * Returns true if the state is the goal state and false otherwise
   * @param rack the current rack
   * @return True if the state is the goal, false otherwise
   */
  private boolean terminalTest(byte[][] rack) {
    int score = eval(rack);
    return score > 95000 || score < -95000;
  }

   /**
   * Returns a utility value of all horizontal groups of four spaces
   * @param rack the current rack
   * @return the total utility of all horizontal groups of four
   */
  private int checkHorizontal(byte[][] rack) {
    int utility = 0;
    int numCompTok = 0;
    int numOppTok = 0;

    for (int i = 0 ; i < NUM_ROWS ; i++) {
      for (int j = 0 ; j < 4 ; j++) {
        if (rack[i][j] == 1) { numCompTok++; }
        if (rack[i][j+1] == 1) { numCompTok++; }
        if (rack[i][j+2] == 1) { numCompTok++; }
        if (rack[i][j+3] == 1) { numCompTok++; }

        if (rack[i][j] == -1) { numOppTok++; }
        if (rack[i][j+1] == -1) { numOppTok++; }
        if (rack[i][j+2] == -1) { numOppTok++; }
        if (rack[i][j+3] == -1) { numOppTok++; }

        utility += calcUtility(numCompTok, numOppTok);

        numCompTok = 0;
        numOppTok = 0;
      }
    }

    return utility;
  }

  /**
   * Returns a utility value of all vertical groups of four spaces
   * @param rack the current rack
   * @return the total utility of all vertical groups of four
   */
  private int checkVertical(byte[][] rack) {
    int utility = 0;
    int numCompTok = 0;
    int numOppTok = 0;

    for (int i = 0 ; i < NUM_COLS ; i++) {
      for (int j = 0 ; j < 3 ; j++) {
        if (rack[j][i] == 1) { numCompTok++; }
        if (rack[j+1][i] == 1) { numCompTok++; }
        if (rack[j+2][i] == 1) { numCompTok++; }
        if (rack[j+3][i] == 1) { numCompTok++; }

        if (rack[j][i] == -1) { numOppTok++; }
        if (rack[j+1][i] == -1) { numOppTok++; }
        if (rack[j+2][i] == -1) { numOppTok++; }
        if (rack[j+3][i] == -1) { numOppTok++; }

        utility += calcUtility(numCompTok, numOppTok);

        numCompTok = 0;
        numOppTok = 0;
      }
    }

    return utility;
  }

  /**
   * Returns a utility value of all diagonal groups of four spaces
   * @param rack the current rack
   * @return the total utility of all diagonal groups of four
   */
  private int checkDiagonal(byte[][] rack) {
    int utility = 0;
    int numCompTok = 0;
    int numOppTok = 0;

    for (int i = 0 ; i < 4 ; i++) {
      for (int j = 0 ; j < 3 ; j++) {
        if (rack[j][i+3] == 1) { numCompTok++; }
        if (rack[j+1][i+2] == 1) { numCompTok++; }
        if (rack[j+2][i+1] == 1) { numCompTok++; }
        if (rack[j+3][i] == 1) { numCompTok++; }

        if (rack[j][i+3] == -1) { numOppTok++; }
        if (rack[j+1][i+2] == -1) { numOppTok++; }
        if (rack[j+2][i+1] == -1) { numOppTok++; }
        if (rack[j+3][i] == -1) { numOppTok++; }

        utility += calcUtility(numCompTok, numOppTok);

        numCompTok = 0;
        numOppTok = 0;
      }
    }

    for (int i = 0 ; i < 4 ; i++) {
      for (int j = 5 ; j > 2 ; j--) {
        if (rack[j][i+3] == 1) { numCompTok++; }
        if (rack[j-1][i+2] == 1) { numCompTok++; }
        if (rack[j-2][i+1] == 1) { numCompTok++; }
        if (rack[j-3][i] == 1) { numCompTok++; }

        if (rack[j][i+3] == -1) { numOppTok++; }
        if (rack[j-1][i+2] == -1) { numOppTok++; }
        if (rack[j-2][i+1] == -1) { numOppTok++; }
        if (rack[j-3][i] == -1) { numOppTok++; }

        utility += calcUtility(numCompTok, numOppTok);

        numCompTok = 0;
        numOppTok = 0;
      }
    }

    return utility;
  }

  /**
   * Returns the utility based on the number of our and opponents' tokens present
   * in a section of four tokens
   * @return an int representing the utility
   */
  private int calcUtility(int numCompTok, int numOppTok) {
    int utility = 0;

    if (numOppTok == 0) {
      switch (numCompTok) {
        case 4:
          utility += 1000000; // a very high number
        case 3:
          utility += 100;
        case 2:
          utility += 10;
        case 1:
          utility += 1;
      }
    }

    if (numCompTok == 0) {
      switch (numOppTok) {
        case 4:
          utility -= 1000000; // a very low number
        case 3:
          utility -= 100;
        case 2:
          utility -= 10;
        case 1:
          utility -= 1;
      }
    }

    return utility;
  }

  /**
  * makes a copy of the rack, to prevent players from accessing it directly
  * @author alchambers
  */
  private byte[][] copyRack(byte[][] rack) {
    byte[][] copy = new byte[rack.length][rack[0].length];
    for (int i=0; i<rack.length; i++) {
      System.arraycopy(rack[i], 0, copy[i], 0, rack[i].length);
    }
    return copy;
  }
}