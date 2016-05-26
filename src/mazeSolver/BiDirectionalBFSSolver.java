/*
 * Copyright (C) 2016 Ricky Wu.
 */
package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;

import static maze.Maze.HEX;
import static maze.Maze.NUM_DIR;

/**
 * Implements Bi-directional BFS maze solving algorithm. This solver performs
 * BFS searches starting at both the entrance and exits. When the two BFS fronts
 * first meet, the path from the entrance to the point they meet, and the path
 * from the exit to the meeting point forms the two halves of a shortest path
 * (in terms of cell visited) from entrance to exit. Combine these paths to get
 * the final path solution.
 */
public class BiDirectionalBFSSolver implements MazeSolver {

	/** The Constant HEAD. */
	private static final int HEAD = 0;
	
	/** The Constant TAIL. */
	private static final int TAIL = 1;
	
	/** The maze. */
	private Maze maze;
	
	/** The visited table of BFS from entrance. */
	private boolean hVisited[][];
	
	/** The visited table of BFS from exit. */
	private boolean tVisited[][];
	
	/** The solved flag. */
	private boolean solved = false;
	
	/** The step_counter. */
	private int step_counter = 0;

	/**
	 * Solve maze.
	 *
	 * @param maze the maze
	 */
	@Override
	public void solveMaze(Maze maze) {
		this.maze = maze;
		// headSet: temp array to store unvisited 
		// neighbors of current cell
		ArrayList<Cell> headSet = new ArrayList<>();
		// tailSet: temp array to store unvisited 
		// neighbors of current cell
		ArrayList<Cell> tailSet = new ArrayList<>();
		// uHead: arraylist to store cells which is visited 
		// by BFS from entrance
		ArrayList<Cell> uHead = new ArrayList<>();
		// uTail: arraylist to store cells which is visited 
		// by BFS from entrance
		ArrayList<Cell> uTail = new ArrayList<>();

		Cell ch = null;
		Cell ct = null;
		// Initialize visited[][]
		hVisited = new boolean[maze.sizeR][maze.sizeC];
		tVisited = new boolean[maze.sizeR][maze.sizeC];
		for(int i=0; i< maze.sizeR; i++) {
			for(int j=0 ; j<maze.sizeC; j++) {
				hVisited[i][j] = false;
				tVisited[i][j] = false;
			}
		}

		headSet.add(maze.entrance);
		tailSet.add(maze.exit);
		
		// Perform BFS from entrance and exit stage by stage
		while(headSet.size()!=0 && tailSet.size()!=0) {
			// Starts BFS from entrance
			if(headSet.size()!=0) {
				ch = headSet.remove(0);
				uHead.add(ch);
				if(ch == ct || uTail.contains(ch)) {
					solved = true;
					break;
				}
				setCellVisited(ch, HEAD);
				// Put its neighbors into temp array for next round
				for(int i=0; i<NUM_DIR; i++) {
					if(ch.wall[i] == null) continue;
					if(ch.wall[i].present) continue;
					Cell ne = ch.neigh[i];
					if(!isCellVisited(ne, HEAD)) {
						if (ne.tunnelTo != null) {
							// set ne point to the cell it tunnels to
							// instead of real neighbor of current cell
							setCellVisited(ne, HEAD);
							ne = ne.tunnelTo;
						}
						headSet.add(ne);

					}
				}
			}
			// Starts BFS from exit
			if(tailSet.size()!=0) {
				ct = tailSet.remove(0);
				uTail.add(ct);
				if(ct == ch || uHead.contains(ct)) {
					solved = true;
					break;
				}
				setCellVisited(ct, TAIL);
				// Put its neighbors into temp array for next round
				for(int i=0; i<NUM_DIR; i++) {
					if(ch.wall[i] == null) continue;
					if(ct.wall[i].present) continue;
					Cell ne = ct.neigh[i];
					if(!isCellVisited(ne, TAIL)) {
						// set ne point to the cell it tunnels to
						// instead of real neighbor of current cell
						if (ne.tunnelTo != null) {
							setCellVisited(ne, TAIL);
							ne = ne.tunnelTo;
						}
						tailSet.add(ne);
					}
				}
			}
		}
	} // end of solveMaze()

	/**
	 * Checks if is cell visited.
	 *
	 * @param cell the cell
	 * @param dir the dir
	 * @return true, if is cell visited
	 */
	private boolean isCellVisited(Cell cell, int dir) {
		boolean result;
		// Return true to skip it if cell is null
		if(cell == null) return true;

		int r = cell.r;
		int c = cell.c;
		if(maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			if(dir == HEAD)
				result = hVisited[r][c];
			else
				result = tVisited[r][c];
		} catch (Exception e){
			result = true;
		}

		return result;
	}

	/**
	 * Sets the cell visited.
	 *
	 * @param cell the cell
	 * @param dir the dir
	 */
	private void setCellVisited(Cell cell, int dir) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			// Set visited table according to whitch side if performing
			if(dir == HEAD)
				hVisited[r][c] = true;
			else
				tVisited[r][c] = true;
			maze.drawFtPrt(cell);
			System.out.println(dir+" "+r+" "+c);
			step_counter ++;
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * Checks if is solved.
	 *
	 * @return true, if is solved
	 */
	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return solved;
	} // end of isSolved()


	/**
	 * Cells explored.
	 *
	 * @return the count of cells visited
	 */
	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return step_counter;
	} // end of cellsExplored()

} // end of class BiDirectionalBFSSolver

