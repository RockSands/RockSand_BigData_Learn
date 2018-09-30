package com.single.cf.filter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.LongPair;

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
		// 获取所有商品
		long itemID;
		List<RecommendedItem> recommendedItems;
		Rescorer<LongPair> rescorer = new FilterItemRescorer(Arrays.asList(608L));
		for (LongPrimitiveIterator it = dataModel.getItemIDs(); it.hasNext();) {
			itemID = it.next();
			recommendedItems = recommender.mostSimilarItems(itemID, 30, rescorer);
			for (RecommendedItem item : recommendedItems) {
				System.out.println("==商品[" + itemID + "]===>" + item.getItemID());
			}
			break;
		}
	}
}
