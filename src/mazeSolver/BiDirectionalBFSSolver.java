package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;

import static maze.Maze.HEX;
import static maze.Maze.NUM_DIR;

/** 
 * Implements Bi-directional BFS maze solving algorithm.
 */
public class BiDirectionalBFSSolver implements MazeSolver {

	private static final int HEAD = 0;
	private static final int TAIL = 1;
	private Maze maze;
	private boolean hVisited[][];
	private boolean tVisited[][];
	private boolean solved = false;
	private int step_counter = 0;

	@Override
	public void solveMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze = maze;
		ArrayList<Cell> headSet = new ArrayList<>();
		ArrayList<Cell> tailSet = new ArrayList<>();
		ArrayList<Cell> uHead = new ArrayList<>();
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
		while(headSet.size()!=0 && tailSet.size()!=0) {
			System.out.println(step_counter);
			if(headSet.size()!=0) {
				ch = headSet.remove(0);
				uHead.add(ch);
				if(ch == ct || uTail.contains(ch)) {
					solved = true;
					break;
				}
				setCellVisited(ch, HEAD);
				for(int i=0; i<NUM_DIR; i++) {
					if(ch.wall[i] == null) continue;
					if(ch.wall[i].present) continue;
					Cell ne = ch.neigh[i];
					if(!isCellVisited(ne, HEAD)) {
						if (ne.tunnelTo != null) {
							setCellVisited(ne, HEAD);
							ne = ne.tunnelTo;
						}
						headSet.add(ne);

					}
				}
			}
			if(tailSet.size()!=0) {
				ct = tailSet.remove(0);
				uTail.add(ct);
				if(ct == ch || uHead.contains(ct)) {
					solved = true;
					break;
				}
				setCellVisited(ct, TAIL);
				for(int i=0; i<NUM_DIR; i++) {
					if(ch.wall[i] == null) continue;
					if(ct.wall[i].present) continue;
					Cell ne = ct.neigh[i];
					if(!isCellVisited(ne, TAIL)) {
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

	private void setCellVisited(Cell cell, int dir) {
		int r = cell.r;
		int c = cell.c;
		if(maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
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


	@Override
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return solved;
	} // end of isSolved()


	@Override
	public int cellsExplored() {
		// TODO Auto-generated method stub
		return step_counter;
	} // end of cellsExplored()

} // end of class BiDirectionalBFSSolver

