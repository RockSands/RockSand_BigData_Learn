package com.single.cf;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class BaseUserBooleanRecommender {
	public static void main(String[] args) throws Exception {
		// 准备数据 这里是电影评分数据
		File file = new File("D:\\ratings\\running\\ratings.dat");
		DataModel dataModel = new GenericBooleanPrefDataModel(
				GenericBooleanPrefDataModel.toDataMap(new FileDataModel(file)));
		// 计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
		UserSimilarity similarity = new CityBlockSimilarity(dataModel);
		UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(50, similarity, dataModel);
		Recommender recommender = new GenericBooleanPrefUserBasedRecommender(dataModel, userNeighborhood, similarity);
		// 给用户ID等于5的用户推荐10部电影
		List<RecommendedItem> recommendedItemList = recommender.recommend(14, 20);
		// 打印推荐的结果
		System.out.println("使用基于用户的协同过滤算法");
		System.out.println("为用户5推荐10个商品");
		for (RecommendedItem recommendedItem : recommendedItemList) {
			System.out.println(recommendedItem);
		}

		// // 给用户ID等于10的用户推荐10部电影
		// recommendedItemList = recommender.recommend(5, 10);
		// // 打印推荐的结果
		// System.out.println("使用基于用户的协同过滤算法");
		// System.out.println("为用户10推荐10个商品");
		// for (RecommendedItem recommendedItem : recommendedItemList) {
		// System.out.println(recommendedItem);
		// }
	}
}
