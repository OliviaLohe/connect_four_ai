package players;

import util.Node;

/**
 * A class that uses the MINIMAX algorithm to run an intelligent connect
 * four artificial intelligence.
 * 
 * @author Olivia Lohe, Nick Bigger
 *
 */
public class ComputerConnectFourPlayer implements ConnectFourPlayer {

	private static int maxDepth;

	/** 
	 * Constructor for the computer player.
	 * @param depth the number of plies to look ahead
	 * @param side -1 or 1, depending on which player this is
	 */
	public ComputerConnectFourPlayer(int depth, byte side) {
		this.maxDepth = depth;
	}

	/** 
	 * chooses the next move through alpha-beta pruning
	 * @param rack the current rack
	 * @return the column to play
	 */
	public int getNextPlay(byte[][] rack) {
		return alphaBeta(new Node(rack)); 
	}

	/** 
	 * This is our implementation of the MINIMAX algorithm
	 * with alpha-beta pruning
	 * @param state the current state of the rack
	 * @return the best action given the current rack
	 */
	public int alphaBeta(Node state) {
		Pair<Integer, Integer> best = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
    return best.action;
  }

	/** 
	 * Calculates the max possible value given by an action,
	 * based on the current position
	 * @param state a node in the search tree
	 * @param alpha the max possible value
	 * @param beta the minimum possible value
	 * @return a pair with the max value and the action leading there
	 */
	public Pair<Integer, Integer> maxValue(Node state, int alpha, int beta) {
	    if (state.cutoffTest(maxDepth)) {
	        return new Pair<Integer, Integer>(state.getEval(), state.getAction());
	    }

	    int value = Integer.MIN_VALUE;
	    int action = 0;
	    for (Node nextMove : state.generateActions()) {
	        // Compute the highest possible utility (for MAX) if MAX were to take this action
	        Pair<Integer, Integer> alternate = minValue(nextMove, alpha, beta);

	        if (alternate.value > value) {
	          value = alternate.value;
	          action = nextMove.getAction();      
	        }

	        //MIN is guaranteed a value of β elsewhere, so MIN would never allow the game to progress to this state
       	    // Return back...no need to keep checking actions
	        if (value >= beta) {
	        	return new Pair<Integer, Integer>(value, action);
	        }

	        if (value > alpha) {
	        	alpha = value;
	        }
	    }

	    return new Pair<Integer, Integer>(value, action);
	}

	/** 
	 * Calculates the minimum possible value given by an action,
	 * based on the current position
	 * @param state a node in the search tree
	 * @param alpha the max possible value
	 * @param beta the minimum possible value
	 * @return a pair with the minimum value and the action leading there
	 */
	public Pair<Integer, Integer> minValue(Node state, int alpha, int beta) {
	    if (state.cutoffTest(maxDepth)) {
	        return new Pair<Integer, Integer>(state.getEval(), state.getAction());
	    }

	    int value = Integer.MAX_VALUE;
	    int action = 0;

	    for (Node nextMove : state.generateActions()) {
	        // Compute the lowest possible utility (for MAX) if MIN were to take this action
	        Pair<Integer, Integer> alternate = maxValue(nextMove, alpha, beta);

	        if (alternate.value < value) {
	            value = alternate.value;
	            action = nextMove.getAction();
	        }

	        //MAX is guaranteed a value of α elsewhere, so MAX would never allow the game to progress to this state
        	//Return back...no need to keep checking actions
	        if (value <= alpha) {
	        	return new Pair<Integer, Integer>(value, action);
	        }

	        if (value < beta) {
	        	beta = value;
	        }
	    }

	    return new Pair<Integer, Integer>(value, action);
	}

	/**
 * A class used to represent two variables as coordinates, repurposed from Prof. Chambers'
 * PriorityQueue class.
 * 
 * @author alchambers
 * @version sp19
 *
 */
  private class Pair<X, Y> {
    public X value;
    public Y action;
    
    public Pair(X v, Y a) {
      value = v;
      action = a;
    }
  }
}
