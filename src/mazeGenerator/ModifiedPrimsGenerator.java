/*
 * Copyright (C) 2016 Ricky Wu.
 */
package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

import static maze.Maze.HEX;

/**
 * The Class ModifiedPrimsGenerator: This generator is based on Prim’s algorithm
 * for computing minimum spanning tree. We used the modified version of it.
 * Starting with a maze where all walls are present, i.e., between every cell is
 * a wall, it uses the following procedure to generate a maze: 1. Pick a random
 * starting cell and add it to set Z (initially Z is empty, after addition it
 * contains just the starting cell). Put all neighbouring cells of starting cell
 * into the frontier set F. 2. Randomly select a cell c from the frontier set
 * and remove it from F. Randomly select a cell b that is in Z and adjecent to
 * the cell c. Carve a path between c and b. 3. Add cell c to the set Z. Add the
 * neighbours of cell c to the frontier set Z. 4. Repeat step 2 until Z includes
 * every cell in the maze. At the end of the process, we would have generated a
 * perfect maze.
 */
public class ModifiedPrimsGenerator implements MazeGenerator {

	/** The cell set. */
	private List<Cell> cellSet;

	/** The cell set new. */
	private List<Cell> cellSetNew;

	/** The dir_arr. */
	// Random direction
	private Integer[] dir_arr = { 0, 1, 2, 3, 4, 5 };

	/** The rand_dir. */
	private List<Integer> rand_dir = Arrays.asList(dir_arr);

	/**
	 * Generate maze.
	 *
	 * @param maze
	 *            the maze
	 */
	@Override
	public void generateMaze(Maze maze) {
		cellSet = new ArrayList<>();
		cellSetNew = new ArrayList<>();

		for (int i = 0; i < maze.sizeR; i ++) {
			int initC = 0, sizeC = maze.sizeC;
			if(maze.type == HEX) {
				initC = (i + 1) / 2;
				sizeC = sizeC + (i + 1) / 2;
			}
			for (int j = initC; j < sizeC; j ++) {
				Cell current = maze.map[i][j];
				cellSet.add(current);
			}
		}
		Collections.shuffle(cellSet);
		// Initial a cell in new cell set
		Cell randCell = cellSet.remove(0);
		cellSetNew.add(randCell);

		do {

			Collections.shuffle(cellSetNew);
			// Pick a random cell in new cell set
			boolean found = false;
			// Indicator for finding a neighbor not in original cell set
			for(Cell temp: cellSetNew) {
				// Shuffle direction array
				Collections.shuffle(rand_dir);
				// Go through random order neighbors of a random cell picked in new cell set
				for(int dir: rand_dir) {
					Cell ne = temp.neigh[dir];
					// If this neighbor can be found in cellset
					if(cellSet.contains(ne)) {
						// remove it from cellSet then add it to new cell set
						cellSet.remove(ne);
						cellSetNew.add(ne);
						temp.wall[dir].present = false;
						found = true;
						break;
					}
				}
				//
				if(found)
					break;
			}

		} while(cellSet.size() != 0);
	} // end of generateMaze()
} // end of class ModifiedPrimsGenerator
