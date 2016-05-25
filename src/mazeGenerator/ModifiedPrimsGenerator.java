package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

import static maze.Maze.HEX;

public class ModifiedPrimsGenerator implements MazeGenerator {

	private List<Cell> cellSet;
	private List<Cell> cellSetNew;
	// Random direction
	private Integer[] dir_arr = {0 ,1 ,2, 3, 4, 5};
	private List<Integer> rand_dir = Arrays.asList(dir_arr);

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
