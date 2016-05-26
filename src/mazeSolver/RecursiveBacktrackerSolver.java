/*
 * Copyright (C) 2016 Ricky Wu.
 */
package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static maze.Maze.HEX;
import static maze.Maze.NUM_DIR;

// TODO: Auto-generated Javadoc
/**
 * RecursiveBacktrackerSolver class:
 * 
 * This solver is basically a DFS. Starting at the entrance of the maze, the
 * solver will initially randomly choose an adjacent unvisited cell. It moves to
 * that cell, update its visit status, then selects another random unvisited
 * neighbour. It continues this process until it hits a deadend (no unvisited
 * neighbours), then it backtracks to a previous cell that has an unvisited
 * neighbour. Randomly select one of the unvisited neighbour and repeat process
 * until we reached the exit (this is always possible for a perfect maze). The
 * path from entrance to exit is the solution.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {

	/** The maze. */
	private Maze maze;
	
	/** The visited table. */
	private boolean visited[][];
	
	/** The solved flag. */
	private boolean solved = false;
	
	/** The step_counter: number of cell visited. */
	private int step_counter = 0;
	
	/** The rand. */
	private Random rand = new Random();


	/**
	 * Solve maze.
	 *
	 * @param maze the maze
	 */
	@Override
	public void solveMaze(Maze maze) {
		this.maze = maze;
		// Initialize visited table
		visited = new boolean[maze.sizeR][maze.sizeC];
		for(boolean[] r: visited) {
			for(boolean c: r) {
				c = false;
			}
		}
		
		// Recursively walkthrough cells from entrance
		walkThrough(this.maze.entrance);

	} // end of solveMaze()


	/**
	 * Checks if the maze is solved.
	 *
	 * @return true, if is solved
	 */
	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return solved;
	} // end if isSolved()


	/**
	 * Number of cells explored.
	 *
	 * @return the number of cells visited.
	 */
	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return step_counter;
	} // end of cellsExplored()

	/**
	 * Walk through.
	 * Recursive funtion for solving maze
	 * @param cell the cell
	 */
	private void walkThrough(Cell cell) {

		setCellVisited(cell);
		// Set cell reference point to the cell it tunnels to
		if (cell.tunnelTo != null) {
			cell = cell.tunnelTo;
			setCellVisited(cell);
		}
		// Randomly get a unvisited neighbor of cell
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

	/**
	 * Randomly chose neighbor.
	 *
	 * @param cell the cell
	 * @return the int
	 */
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

	/**
	 * Checks if is cell visited.
	 *
	 * @param cell the cell
	 * @return true, if is cell visited
	 */
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

	/**
	 * Sets the cell visited.
	 *
	 * @param cell the new cell visited
	 */
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
