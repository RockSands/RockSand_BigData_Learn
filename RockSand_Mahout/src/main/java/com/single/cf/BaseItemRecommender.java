package com.single.cf;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 * 基于物品
 * 
 * @author Administrator
 *
 */
public class BaseItemRecommender {
	public static void main(String[] args) throws Exception {
		// 准备数据 这里是电影评分数据
		File file = new File("D:\\ratings\\running\\ratings.dat");
		// 将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
		DataModel dataModel = new FileDataModel(file);
		// 计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
		ItemSimilarity itemSimilarity = new UncenteredCosineSimilarity(dataModel);
		// 构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于物品的协同过滤推荐
		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
		// 给用户ID等于5的用户推荐10个商品
		List<RecommendedItem> recommendedItemList = recommender.recommend(5, 100);
		// 打印推荐的结果
		System.out.println("使用基于物品的协同过滤算法");
		System.out.println("为用户5推荐100个相似的商品");
		for (RecommendedItem recommendedItem : recommendedItemList) {
			System.out.println(recommendedItem);
		}
//		FastIDSet idSet = dataModel.getItemIDsFromUser(5);
//		LongPrimitiveIterator it = idSet.iterator();
//		System.out.println("使用基于物品的协同过滤算法");
//		while (it.hasNext()) {
//			// recommendedItemList = recommender.recommendedBecause(5, it.next(), 2);
//			recommendedItemList = recommender.mostSimilarItems(it.next(), 2);
//			for (RecommendedItem recommendedItem : recommendedItemList) {
//				System.out.println(recommendedItem);
//			}
//		}
	}
}