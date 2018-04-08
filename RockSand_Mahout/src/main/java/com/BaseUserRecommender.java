package com;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * 基于用户
 * 
 * @author Administrator
 *
 */
public class BaseUserRecommender {
	public static void main(String[] args) throws Exception {
		/*
		 * 构建数据:文件
		 */
		// 准备数据 这里是电影评分数据
		File file = new File("D:\\ratings\\running\\ratings.dat");
		// 将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
		DataModel dataModel = new FileDataModel(file);
		/*
		 * 构建数据:数据库
		 */
		// MysqlDataSource ds = new MysqlDataSource();
		// ds.setServerName("192.168.80.138");
		// ds.setDatabaseName("test");
		// ds.setPort(3306);
		// ds.setUser("root");
		// ds.setCharacterEncoding("utf-8");
		// ds.setPassword("123456");
		// ds.setDatabaseName("mahout");
		// DataModel dataModel = new MySQLJDBCDataModel(ds);
		// 计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
		UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);
		// 计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
		// 构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
		Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
		// 给用户ID等于5的用户推荐10部电影
		List<RecommendedItem> recommendedItemList = recommender.recommend(5, 100);
		// 打印推荐的结果
		System.out.println("使用基于用户的协同过滤算法");
		System.out.println("为用户5推荐10个商品");
		for (RecommendedItem recommendedItem : recommendedItemList) {
			System.out.println(recommendedItem);
		}

		// 给用户ID等于10的用户推荐10部电影
//		recommendedItemList = recommender.recommend(5, 10);
//		// 打印推荐的结果
//		System.out.println("使用基于用户的协同过滤算法");
//		System.out.println("为用户10推荐10个商品");
//		for (RecommendedItem recommendedItem : recommendedItemList) {
//			System.out.println(recommendedItem);
//		}
	}
}