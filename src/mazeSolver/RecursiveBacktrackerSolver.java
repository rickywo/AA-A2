package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static maze.Maze.HEX;
import static maze.Maze.NUM_DIR;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {

	private Maze maze;
	private boolean visited[][];
	private boolean solved = false;
	private int step_counter = 0;
	private Random rand = new Random();


	@Override
	public void solveMaze(Maze maze) {
		this.maze = maze;
		visited = new boolean[maze.sizeR][maze.sizeC];
		for(boolean[] r: visited) {
			for(boolean c: r) {
				c = false;
			}
		}

		walkThrough(this.maze.entrance);

	} // end of solveMaze()


	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return solved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return step_counter;
	} // end of cellsExplored()

	private void walkThrough(Cell cell) {

		setCellVisited(cell);
		int dir = randomlyChoseNeighbor(cell);
		Cell neigh;
		while (dir != -1) {
			neigh = cell.neigh[dir];
			// Assign true to solved, if find a cell is exit of the maze
			if(neigh.equals(this.maze.exit)) {
				setCellVisited(this.maze.exit);
				solved = true;
				return;
			}
			walkThrough(neigh);
			// if maze solved return to caller
			if(solved) return;
			dir = randomlyChoseNeighbor(cell);

		}
	}

	private int randomlyChoseNeighbor(Cell cell) {
		List<Integer> neighsDir = new ArrayList<>();
		for (int k = 0; k < NUM_DIR; k++) {
			Cell c = cell.neigh[k];
			if(c != null && !isCellVisited(c) && !cell.wall[k].present) {
				neighsDir.add(k);
			}
		}
		int neighDir = -1;
		if(neighsDir.size() > 0)
			neighDir = neighsDir.get(rand.nextInt(neighsDir.size()));

		return neighDir;
	}

	private boolean isCellVisited(Cell cell) {
		boolean result;
		int r = cell.r;
		int c = cell.c;
		if(maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			result = visited[r][c];
		} catch (Exception e){
			result = true;
		}

		return result;
	}

	private void setCellVisited(Cell cell) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			visited[r][c] = true;
			maze.drawFtPrt(cell);
			step_counter ++;
		} catch (Exception e){
			e.printStackTrace();
		}
	}

} // end of class RecursiveBackTrackerSolver
