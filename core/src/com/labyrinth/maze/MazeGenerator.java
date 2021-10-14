package com.labyrinth.maze;

import java.util.Arrays;
import java.util.Collections;

/**
 * recursive backtracking algorithm shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 * 
 * Algoritmo de generación de laberintos tomado de la URL anterior.
 */
public class MazeGenerator {
	private final int x;
	private final int y;
	public final int[][] maze;

	/**
	 * Constructor que genera un laberinto rectangular con lados x e y
	 * 
	 * @param x
	 * @param y
	 */
	public MazeGenerator(int x, int y) {
		this.x = x;
		this.y = y;
		maze = new int[this.x][this.y];
		generateMaze(0, 0);
	}

	/**
	 * Muestra por consola el laberinto generado.
	 */
	public void display() {
		for (int i = 0; i < y; i++) {
			// draw the north edge
			for (int j = 0; j < x; j++) {
				System.out.print((maze[j][i] & 1) == 0 ? "+---" : "+   ");
			}
			System.out.println("+");
			// draw the west edge
			for (int j = 0; j < x; j++) {
				System.out.print((maze[j][i] & 8) == 0 ? "|   " : "    ");
			}
			System.out.println("|");
		}
		// draw the bottom line
		for (int j = 0; j < x; j++) {
			System.out.print("+---");
		}
		System.out.println("+");
	}

	/**
	 * Metodo de generación recursiva del laberinto. Se guarda una matriz de
	 * enumerados DIR 
	 * 
	 * @param cx
	 * @param cy
	 */
	private void generateMaze(int cx, int cy) {
		DIR[] dirs = DIR.values();
		Collections.shuffle(Arrays.asList(dirs));
		for (DIR dir : dirs) {
			int nx = cx + dir.dx;
			int ny = cy + dir.dy;
			if (between(nx, x) && between(ny, y) && (maze[nx][ny] == 0)) {
				maze[cx][cy] |= dir.bit;
				maze[nx][ny] |= dir.opposite.bit;
				generateMaze(nx, ny);
			}
		}
	}

	/**
	 * Comprueba que un numero este entre 0 y el limite superior dado
	 * 
	 * @param v
	 * @param upper
	 * @return
	 */
	private static boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}

	/**
	 * Enumerado que representa una celda y si las paredes en cada dirección existen
	 * o no(se pueden atravesar). Se representan en un Byte (4bits) las direcciones bloquedeas
	 * de esa posición.
	 * 
	 *
	 */
	public enum DIR {
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
	/**
	 * Clase main para hacer pruebas
	 * @param args
	 */
	public static void main(String[] args) {
		int x = args.length >= 1 ? (Integer.parseInt(args[0])) : 8;
		int y = args.length == 2 ? (Integer.parseInt(args[1])) : 8;
		MazeGenerator maze = new MazeGenerator(x, y);
		maze.display();
	}

}
