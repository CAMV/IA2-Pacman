import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.internal.Ghost;


/* 
 * This class will carry out all the features, weights and related functions.
 * As of right now, the features will consist of:
 * 1) The distance to the closest pellet
 * 2) The distance to the closest ghost
 * 3) The distance to the fruit ( if any)
 * 4) The inverse squared of the pellet distance
 * */


public class Features {
	
	private static final int NUM_FEATURES = 4;

	public float[] weights;
	
	//Constructor w
	public Features() {
		weights = new float[NUM_FEATURES+1]; 		//+1 because of the threshold (W0)
		//Initialize the weights on random values
		for (int j = 0; j < NUM_FEATURES+1; j ++) {
			weights[j] = (float) Math.random();
		}
		
	}
	
	
	//FIRST FEATURE
	//Inverse(?) of the closest pellet's distance
	public float closestPellet(Game game) {
		
		int currentNodeIndex=game.getPacmanCurrentNodeIndex();
		
		//get all active pills
		int[] activePills=game.getActivePillsIndices();
		
		//get the closest pill, and its distance
		int closest = game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,activePills,DM.PATH);
		float distance = (float) game.getDistance(activePills[closest], currentNodeIndex, DM.PATH);
		
		return 1/(1+distance);
	}
	
	//SECOND FEATURE
	//Distance to the closest power pellet
	public float closestEnergiser(Game game) {
		
		int currentNodeIndex=game.getPacmanCurrentNodeIndex();
		
		//get all active power pills
		int[] activePowerPills=game.getActivePowerPillsIndices();
		
		//get the closest pill, and its distance
		int closest = game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,activePowerPills,DM.PATH);
		float distance = (float) game.getDistance(activePowerPills[closest], currentNodeIndex, DM.PATH);
		
		return 1/(1+distance);
	}
	
	//THIRD FEATURE
	//squared inverse distance to the pellet
	public float squarePellet(Game game) {
		
		int currentNodeIndex=game.getPacmanCurrentNodeIndex();
		
		//get all active pills
		int[] activePills=game.getActivePillsIndices();
		
		//get the closest pill, and its distance
		int closest = game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,activePills,DM.PATH);
		float distance = (float) game.getDistance(activePills[closest], currentNodeIndex, DM.PATH);
		
		return 1/(1+distance*distance);
	}
	
	//FOURTH FEATURE
	//distance to the nearest ghost
	public float closestGhost(Game game) {
		
		int currentNodeIndex=game.getPacmanCurrentNodeIndex();
		
		//get each ghost
		int ghostsIndices[] = new int[4];
		int i = 0;
		for(GHOST ghost : GHOST.values()) {
			ghostsIndices[i] = game.getGhostCurrentNodeIndex(ghost);
			i++;
		}
		
		int closest = game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,ghostsIndices,DM.PATH);
		float distance = (float) game.getDistance(ghostsIndices[closest], currentNodeIndex, DM.PATH);
		
		return (float) 1/(1+distance);
	}
	
	float featureFunction(Game game, int i) {
		switch (i) {
		case 0:
			return 1.0f;
		case 1:
			return closestPellet(game);
		case 2:
			return closestEnergiser(game);
		case 3:
			return squarePellet(game);
		case 4:
			return closestGhost(game);
		default:
			return 1.0f;
		
		}
	}
	
	float getStateValue(Game game) {
		float val = 0.0f;
		
		for (int i = 0; i < NUM_FEATURES+1; i++) {
			val += featureFunction(game, i)*weights[i];
		}
		
		return val;
	}
	
	//public float closestGhost;
	//public float fruitDistance;
	//public float energiserDistance;
	//public float squarepellet;
	
}
