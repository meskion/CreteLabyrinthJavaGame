package com.labyrinth.maze;

public class Maze extends MazeGenerator {

	public Maze() {

	}

	public Maze(Coordinates coordinates) {
		super(coordinates);
	}

	public int[][] getMaze() {
		return maze;
	}

	public Coordinates getCoordinates() {
		return this.coordinates;
	}

	public Boolean outsideMazeLimits(Integer x, Integer y) {
		return x < 0 || y < 0 || x > getCoordinates().getX() - 1 || y > getCoordinates().getY() - 1;
	}

	public Boolean hasFloorAt(Integer x, Integer y) {
		return (maze[x][y] & 1) == 0;
	}

	public Boolean hasWallAt(Integer x, Integer y) {
		return (maze[x][y] & 8) == 0;
	}

//	public void display(Character... characters) {
//
//		int x = coordinates.getX();
//		int y = coordinates.getY();
//
//		for (int i = 0; i < y; i++) {
//			for (int j = 0; j < x; j++) {
//				System.out.print((maze[j][i] & 1) == 0 ? "+---" : "+   ");
//			}
//			System.out.println("+");
//			for (int j = 0; j < x; j++) {
//				String event = (maze[j][i] & 8) == 0 ? "| x " : "  x ";
//				if (j == x - 1 && i == y - 1) {
//					event = (maze[j][i] & 8) == 0 ? "| END" : " END";
//					System.out.print(event);
//				} else {
//					System.out.print(event.replace("x", Character.getSpace(new Coordinates(j, i), characters)));
//				}
//			}
//			System.out.println("|");
//		}
//		for (int j = 0; j < x; j++) {
//			System.out.print("+---");
//		}
//		System.out.println("+");
//	}

}