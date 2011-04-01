package gr.uom.java.ast.util.math;

import static gr.uom.java.ast.util.math.DoubleArray.deleteColumns;
import static gr.uom.java.ast.util.math.DoubleArray.deleteRows;
import static gr.uom.java.ast.util.math.DoubleArray.insertColumns;
import static gr.uom.java.ast.util.math.DoubleArray.insertRows;

import gr.uom.java.distance.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Hierarchical extends Clustering {
	
	private HashSet<Cluster> clusterSet;
	
	public Hierarchical(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
		this.clusterSet = new HashSet<Cluster>();
	}

	public HashSet<Cluster> clustering(ArrayList<Entity> entities) {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		for(Entity entity : entities) {
			Cluster cluster = new Cluster();
			cluster.addEntity(entity);
			clusters.add(cluster);
		}
		while(clusters.size()>2) {
			double minVal = 2.0;
			int minRow = 0;
			int minCol = 1;
			for(int i=1; i<distanceMatrix.length;i++) {
				for(int j=0;j<i;j++) {
					if (distanceMatrix[i][j] < minVal) {
						minVal = distanceMatrix[i][j];
						minRow = i;
						minCol = j;
					}
				}
			}
			
			
			if(minRow < minCol) {
				clusters.get(minRow).addEntities(clusters.get(minCol).getEntities());
				double[] newDistances = new double[distanceMatrix.length-1];
				for(int i=0;i<distanceMatrix.length;i++) {
						if (i != minCol) {
							if (i != minRow) {
								if (distanceMatrix[minRow][i] < distanceMatrix[minCol][i]) {
									if (i > minCol) {
										newDistances[i - 1] = distanceMatrix[minRow][i];
									} else {
										newDistances[i] = distanceMatrix[minRow][i];
									}
								} else {
									if (i > minCol) {
										newDistances[i - 1] = distanceMatrix[minCol][i];
									} else {
										newDistances[i] = distanceMatrix[minCol][i];
									}
								}
							}
							else {
								newDistances[i] = 0.0;
							}
						}
						
				}
				
				distanceMatrix = deleteRows(distanceMatrix, minRow, minCol);
				distanceMatrix = deleteColumns(distanceMatrix, minCol);
				distanceMatrix = insertRows(distanceMatrix, minRow, newDistances);
				distanceMatrix = deleteColumns(distanceMatrix, minRow);
				distanceMatrix = insertColumns(distanceMatrix, minRow, newDistances);
				clusters.remove(minCol);
			}
			else {
				clusters.get(minCol).addEntities(clusters.get(minRow).getEntities());
				double[] newDistances = new double[distanceMatrix.length-1];
				for(int i=0;i<distanceMatrix.length;i++) {
					if (i != minRow) {
						if (i != minCol) {
							if (distanceMatrix[minRow][i] < distanceMatrix[minCol][i]) {
								if (i > minRow) {
									newDistances[i - 1] = distanceMatrix[minRow][i];
								} else {
									newDistances[i] = distanceMatrix[minRow][i];
								}
							} else {
								if (i > minRow) {
									newDistances[i - 1] = distanceMatrix[minCol][i];
								} else {
									newDistances[i] = distanceMatrix[minCol][i];
								}
							}
						}
						else {
							newDistances[i] = 0.0;
						}
					}
					
				}
				distanceMatrix = deleteRows(distanceMatrix, minRow, minCol);
				distanceMatrix = deleteColumns(distanceMatrix, minRow);
				distanceMatrix = insertRows(distanceMatrix, minCol, newDistances);
				distanceMatrix = deleteColumns(distanceMatrix, minCol);
				distanceMatrix = insertColumns(distanceMatrix, minCol, newDistances);
				clusters.remove(minRow);
			}
			for(Cluster cluster : clusters) {
				if(cluster.getEntities().size() > 1) {
					Cluster c = new Cluster(cluster.getEntities());
					clusterSet.add(c);
				}
			}
		}
		return clusterSet;
	}

}
