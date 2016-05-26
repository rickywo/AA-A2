/*
 * Copyright (C) 2016 Ricky Wu.
 */
package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

import static maze.Maze.*;

/**
 * The Class KruskalGenerator:
 * This generator is based on Kruskal’s algorithm for computing minimum spanning trees 
 * (hence the name of this generator). Starting with a maze where all walls are present, 
 * i.e., between every cell is a wall, it uses the following procedure to 
 * generate a maze: 
 * 1. For each pair of adjacent cells, construct an edge that links these two cells. 
 *    Put all possible edges into a set. Maintain a set of trees. 
 *    Initially this set of trees contains a set of singleton tree, 
 *    where each tree has one of the cells (its index) as it only vertex.
 * 2. Select a random edge in the set. If the edge joins two disjoint trees in 
 *    the tree set, join the trees. 
 *    If not, disgard the edge. When a selected edge joins two trees, 
 *    carve a path between the two corresponding cells. 
 * 3. Repeat step 2 until the set of edges is empty. When this occurs, 
 *    then the maze will be a perfect one.
 */
public class KruskalGenerator implements MazeGenerator {

	/** The maze reference. */
	private Maze maze;

	/**
	 * Generate maze.
	 *
	 * @param maze the maze passed from main function
	 */
	@Override
	public void generateMaze(Maze maze) {
		this.maze = maze;
		/* Create a list of edges */
		ArrayList<Path> edges = new ArrayList<>();
		ArrayList<Cell> cells = new ArrayList<>();
		// Add cells and edges into arraylist
		for (int i = 0; i < maze.sizeR; i ++) {
			int initC = 0, sizeC = maze.sizeC;
			if(maze.type == HEX) {
				initC = (i + 1) / 2;
				sizeC = sizeC + (i + 1) / 2;
			}
			for (int j = initC; j < sizeC; j ++) {
				Cell current = maze.map[i][j];
				// Add cell into cells arraylist
				cells.add(current);
				for(int k = 0; k < 3; k ++) {
					Cell ne = current.neigh[k];
					// If neighbor is in the maze
					if(isIn(ne)) {
						// If there is a tunnel from neighbor to some cell
						if (ne.tunnelTo != null) {
							ne = ne.tunnelTo;
						}
						// Add edge from cell to where its neighbor tunnel to
						edges.add(new Path(current, ne));
					}
				}
			}
		}
		// Initialize a Disjoint set instance
		DisjointSet djSet = new DisjointSet(cells);

		// Shuffle edges in the arraylist for randomly retrieving edge
		Collections.shuffle(edges);

		for (Path e : edges) {
			Cell u = e.getU();
			Cell v = e.getV();
			if (djSet.find(u) != djSet.find(v)) {
				// cell v and u are not in the same set 
				
				for(int k = 0; k < NUM_DIR; k ++) {
					// Find the wall shared by u and v
					Cell ne = u.neigh[k];
					// Skip if neighbor is not in the maze
					if(!isIn(ne)) continue;
					// carve the wall if find a neighbor of u equal to v
					// or neighbor equal to the cell tunnel from v
					if(ne == v || ne == v.tunnelTo) {
						u.wall[k].present = false;
					}
				}
				// Join two sets
				djSet.union(u, v);
			}
		}

	} // end of generateMaze()

	/**
	 * Checks if cell is in the maze.
	 *
	 * @param r the row index of cell
	 * @param c the column index of cell
	 * @return true, if the cell is in the maze
	 */
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


	/**
	 * The Class Path.
	 */
	protected class Path {
		
		/** The cell u and cell v of the path */
		private Cell u, v;

		/**
		 * Instantiates a new path connects two cell u and v
		 *
		 * @param u the u cell
		 * @param v the v cell
		 */
		public Path(Cell u, Cell v) {
			this.u = u;
			this.v = v;
		}

		/**
		 * Gets the u cell
		 *
		 * @return the u
		 */
		public Cell getU() {
			return u;
		}

		/**
		 * Gets the v cell.
		 *
		 * @return the v
		 */
		public Cell getV() {
			return v;
		}
	}

	/**
	 * The Class Node.
	 */
	protected class Node {

		/** The num of childs. */
		int numOfChilds = 0;
		
		/** The cell. */
		Cell cell;
		
		/** The parent node. */
		Node parent = null;

		/**
		 * Instantiates a new node.
		 *
		 * @param c the cell stored in node
		 * @param n the number of child of this node
		 * @param p the parent node
		 */
		public Node(Cell c, int n, Node p) {
			cell = c;
			numOfChilds = n;
			parent = p;
		}

	}

	/**
	 * The Class DisjointSet.
	 */
	protected class DisjointSet {

		/** The num of nodes it stores. */
		int numOfNodes = 0;
		
		/** The num of sets it stores.  */
		int numOfSets = 0;

		/** The map to store nodes indexed by row number and column number . */
		private Map<Integer, Map<Integer, Node>> mapR = new HashMap<>();

		/**
		 * Instantiates a new disjoint set.
		 *
		 * @param cells the arraylist of cells
		 */
		public DisjointSet(List<Cell> cells) {
			makeSets(cells);
		}

		/**
		 * Make single sets.
		 * Join 2 cells with tunnel in between if maze type if TUNNEL
		 *
		 * @param cells the arraylist of cells
		 */
		private void makeSets(List<Cell> cells) {
			for(Cell c: cells) {
				makeSet(c);
			}
			// Union Tunnel cells
			for(Cell c: cells) {
				Cell linkedCell = c.tunnelTo;
				if(linkedCell!=null && find(c) != find(linkedCell)) {
						union(c, linkedCell);
				}
			}
		}

		/**
		 * Make set. Put cell into Map with its row and column indexs,
		 *
		 * @param c the cell passed in
		 */
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

		/**
		 * Find the root node of a set of a passed in cell
		 *
		 * @param c the c
		 * @return the node
		 */
		public Node find(Cell c) {
			Map<Integer, Node> mapC = mapR.get(c.r);
			Node current = mapC.get(c.c);
			while (current.parent != null)
				current = current.parent;

			Node root = current;
			return root;
		}

		/**
		 * Union. To join 2 sets, attaches small tree to a
		 * taller tree with more nodes it has.
		 *
		 * @param a the a
		 * @param b the b
		 */
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
			// Decrease set count.
			this.numOfSets --;
		}


	}

} // end of class KruskalGenerator
