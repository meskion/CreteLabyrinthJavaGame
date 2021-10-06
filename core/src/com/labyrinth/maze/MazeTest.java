package com.labyrinth.maze;

import java.util.Arrays;

public class MazeTest {
	static int[][] maze;

	public static void main(String[] args) {
		MazeGenerator m = new MazeGenerator(10, 10);
		maze = m.maze;
		m.display();
		for (int[] r : maze) {
			System.err.println(Arrays.toString(r));

		}
	}

	
			

		
	

}
