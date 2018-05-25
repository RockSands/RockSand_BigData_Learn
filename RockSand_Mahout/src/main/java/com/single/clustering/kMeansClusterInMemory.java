package com.single.clustering;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.iterator.DistanceMeasureCluster;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

/**
 * Mahout单机内存暂时无法实现
 * 
 * 基于内存的K均值聚类算法实现 参考: http://www.dataguru.cn/thread-322086-1-1.html
 * 
 * @author Administrator
 *
 */
public class kMeansClusterInMemory {
	// 创建一个二维点集的向量组
	public static final double[][] points = { { 1, 1 }, { 2, 1 }, { 1, 2 }, { 2, 2 }, { 3, 3 }, { 8, 8 }, { 9, 8 },
			{ 8, 9 }, { 9, 9 }, { 5, 5 }, { 5, 6 }, { 6, 6 } };

	public static List<Vector> getPointVectors(double[][] raw) {
		List<Vector> points = new ArrayList<Vector>();
		for (int i = 0; i < raw.length; i++) {
			double[] fr = raw[i];
			// 这里选择创建 RandomAccessSparseVector
			Vector vec = new RandomAccessSparseVector(fr.length);
			// 将数据存放在创建的 Vector 中
			vec.assign(fr);
			points.add(vec);
		}
		return points;
	}

	// 基于内存的 K 均值聚类算法实现
	public static void kMeansClusterInMemoryKMeans() {
		// 指定需要聚类的个数，这里选择 2 类
		int k = 2;
		// 指定 K 均值聚类算法的最大迭代次数
		int maxIter = 3;
		// 指定 K 均值聚类算法的最大距离阈值
		double distanceThreshold = 0.01;
		// 声明一个计算距离的方法，这里选择了欧几里德距离
		DistanceMeasure measure = new EuclideanDistanceMeasure();
		// 这里构建向量集，使用的是清单 1 里的二维点集
		List<Vector> pointVectors = getPointVectors(points);
		// 从点集向量中随机的选择 k 个作为簇的中心
		List<Vector> randomPoints = RandomSeedGenerator.chooseRandomPoints(pointVectors, k);
		// 基于前面选中的中心构建簇
		List<DistanceMeasureCluster> clusters = new ArrayList<DistanceMeasureCluster>();
		int clusterId = 0;
		for (Vector v : randomPoints) {
			clusters.add(new DistanceMeasureCluster(v, clusterId++, measure));
		}
		// 调用 KMeansClusterer.clusterPoints 方法执行 K 均值聚类
		List<List<DistanceMeasureCluster>> finalClusters = KMeansClusterer.clusterPoints(pointVectors, clusters,
				measure, maxIter, distanceThreshold);

		// 打印最终的聚类结果
		for (DistanceMeasureCluster cluster : finalClusters.get(finalClusters.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId() + " center: " + cluster.getCenter().asFormatString());
			System.out.println("       Points: " + cluster.getNumObservations());
		}
	}

}
