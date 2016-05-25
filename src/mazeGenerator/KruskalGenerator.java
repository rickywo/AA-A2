package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

import static maze.Maze.*;

public class KruskalGenerator implements MazeGenerator {

	private Maze maze;

	@Override
	public void generateMaze(Maze maze) {
		this.maze = maze;
		/* Create a list of edges */
		ArrayList<Path> edges = new ArrayList<>();
		ArrayList<Cell> cells = new ArrayList<>();

		// TODO Auto-generated method stub
		for (int i = 0; i < maze.sizeR; i ++) {
			int initC = 0, sizeC = maze.sizeC;
			if(maze.type == HEX) {
				initC = (i + 1) / 2;
				sizeC = sizeC + (i + 1) / 2;
			}
			for (int j = initC; j < sizeC; j ++) {
				Cell current = maze.map[i][j];
				cells.add(current);
				for(int k = 0; k < 3; k ++) {
					Cell ne = current.neigh[k];
					if(isIn(ne)) {
						edges.add(new Path(current, ne));
					}
				}
			}
		}

		DisjointSet djSet = new DisjointSet(cells);

		Collections.shuffle(edges);

		for (Path e : edges) {
			Cell u = e.getU();
			Cell v = e.getV();
			if (djSet.find(u) != djSet.find(v)) {
        /* Vertices v and u are not in the same component */

				for(int k = 0; k < NUM_DIR; k ++) {
					if(u.neigh[k] == v) {
						u.wall[k].present = false;
					}
				}
        /* Union them in the tree */
				djSet.union(u, v);
			}
		}

	} // end of generateMaze()

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


	protected class Path {
		private Cell u, v;

		public Path(Cell u, Cell v) {
			this.u = u;
			this.v = v;
		}

		public Cell getU() {
			return u;
		}

		public Cell getV() {
			return v;
		}
	}

	protected class Node {

		int numOfChilds = 0;
		Cell cell;
		Node parent = null;

		public Node(Cell c, int n, Node p) {
			cell = c;
			numOfChilds = n;
			parent = p;
		}

	}

	protected class DisjointSet {

		int numOfNodes = 0;
		int numOfSets = 0;

		private Map<Integer, Map<Integer, Node>> mapR = new HashMap<>();

		public DisjointSet(List<Cell> cells) {
			makeSets(cells);
		}

		private void makeSets(List<Cell> cells) {
			for(Cell c: cells) {
				makeSet(c);
			}
		}

		private void makeSet(Cell c) {
			Node n = new Node(c, 0, null);
			Map<Integer, Node> mapC = mapR.get(c.r);
			if(mapC == null) {
				mapC = new HashMap<>();
				mapC.put(c.c, n);
				mapR.put(c.r, mapC);
			} else {
				mapC.put(c.c, n);
			}
			
			this.numOfSets ++;
			this.numOfNodes ++;
		}

		public Node find(Cell c) {
			//Node current = this.roots.get(id);
			Map<Integer, Node> mapC = mapR.get(c.r);
			Node current = mapC.get(c.c);
			while (current.parent != null)
				current = current.parent;

			Node root = current;
			return root;
		}

		public void union(Cell a, Cell b) {
			Node aTreeRoot = find(a);
			Node bTreeRoot = find(b);

			if (aTreeRoot.equals(bTreeRoot) ) return;

			if (aTreeRoot.numOfChilds < bTreeRoot.numOfChilds) {
				aTreeRoot.parent = bTreeRoot;
			} else if (aTreeRoot.numOfChilds > bTreeRoot.numOfChilds) {
				bTreeRoot.parent = aTreeRoot;
			} else {
				bTreeRoot.parent = aTreeRoot;
				aTreeRoot.numOfChilds += bTreeRoot.numOfChilds;
			}

			this.numOfSets --;
		}


	}

} // end of class KruskalGenerator
