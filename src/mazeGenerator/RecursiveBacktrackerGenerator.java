package mazeGenerator;

import maze.Cell;
import maze.HexMaze;
import maze.Maze;

import java.util.*;

import static maze.Maze.*;


public class RecursiveBacktrackerGenerator implements MazeGenerator {


	private Maze maze;
	private Random rand = new Random();
	private boolean visited[][];
	// Random direction
	private Integer[] dir_arr = {0 ,1 ,2, 3, 4, 5};
	private List<Integer> rand_dir = Arrays.asList(dir_arr);

	@Override
	public void generateMaze(Maze maze) {
		this.maze = maze;
		visited = new boolean[maze.sizeR][maze.sizeC];
		for(boolean[] r: visited) {
			for(boolean c: r) {
				c = false;
			}
		}

		int rr = 0;
		int	rc = 0;
		while(!isIn(rr, rc)) {
			rr = rand.nextInt(maze.sizeR);
			if (maze.type == HEX)
				rc = rand.nextInt(maze.sizeC + (maze.sizeR + 1) / 2);
			else
				rc = rand.nextInt(maze.sizeC);
		}

		Cell randCell = maze.map[rr][rc];
		carvePassage(randCell);

	} // end of generateMaze()

	private void carvePassage(Cell cell) {

		setCellVisited(cell);
		if (cell.tunnelTo != null) {
			cell = cell.tunnelTo;
			setCellVisited(cell);
		}
		int dir = randomlyChoseNeighbor(cell);
		Cell neigh;
		while (dir != -1) {
			neigh = cell.neigh[dir];
			cell.wall[dir].present = false;
			carvePassage(neigh);
			dir = randomlyChoseNeighbor(cell);

		}
	}

	private int randomlyChoseNeighbor(Cell cell) {

		//if(!isIn(cell)) return -1;

		List<Integer> neighsDir = new ArrayList<>();
		Collections.shuffle(rand_dir);
		// Go through random order neighbors of a random cell picked in new cell set
		for(int dir: rand_dir) {
			Cell ne = cell.neigh[dir];
			if(ne != null && !isCellVisited(ne)) {
				neighsDir.add(dir);
			}
		}
		int neighDir = -1;
		if(neighsDir.size() > 0)
			neighDir = neighsDir.get(rand.nextInt(neighsDir.size()));

		return neighDir;
	}

	protected boolean isIn(int r, int c) {
		boolean result = r >= 0 && r < maze.sizeR && c >= 0 && c < maze.sizeC;
		if(maze.type == HEX)
			result = r >= 0 && r < maze.sizeR && c >= (r + 1) / 2 && c < maze.sizeC + (r + 1) / 2;

		return result;
	} // end of isIn()


	/**
	 * Check whether the cell is in the maze.
	 * @param cell The cell being checked.
	 * @return True if in the maze. Otherwise false.
	 */
	protected boolean isIn(Cell cell) {
		if (cell == null)
			return false;
		return isIn(cell.r, cell.c);
	} // end of isIn()

	private boolean isCellVisited(Cell cell) {
		boolean result;
		// Return true to skip it if cell is null
		if(!isIn(cell)) return true;

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

		if(!isIn(cell)) return;

		int r = cell.r;
		int c = cell.c;
		if(maze.type == HEX) {
			c = c - (r + 1) / 2;
		}
		try {
			visited[r][c] = true;
		} catch (Exception e){
			e.printStackTrace();
		}
	}

} // end of class RecursiveBacktrackerGenerator
