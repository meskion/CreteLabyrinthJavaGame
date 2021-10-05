package com.labyrinth.maze;


import java.util.Arrays;
import java.util.Collections;

public class MazeGenerator {
	protected Coordinates coordinates;
	protected int[][] maze;

	public MazeGenerator() {

	}

	public MazeGenerator(Coordinates coordinates) {
		this.coordinates = coordinates;
		this.maze = new int[this.coordinates.getX()][this.coordinates.getY()];
		generateMaze(0, 0);
	}

	private void generateMaze(int cx, int cy) {
		DIR[] dirs = DIR.values();
		Collections.shuffle(Arrays.asList(dirs));
		Integer x = this.coordinates.getX();
		Integer y = this.coordinates.getY();

		for (DIR dir : dirs) {
			int nx = cx + dir.dx;
			int ny = cy + dir.dy;
			if (between(nx, x) && between(ny, y) && (this.maze[nx][ny] == 0)) {
				this.maze[cx][cy] |= dir.bit;
				this.maze[nx][ny] |= dir.opposite.bit;
				generateMaze(nx, ny);
			}
		}
	}

	private static boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}

	private enum DIR {
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
		private final int bit;
		private final int dx;
		private final int dy;
		private DIR opposite;

		// use the static initializer to resolve forward references
		static {
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}

		private DIR(int bit, int dx, int dy) {
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	};

}
