package com.single.cf.filter;

import java.util.Collection;

import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.common.LongPair;

/**
 * https://blog.csdn.net/panguoyuan/article/details/43524507?utm_source=copy
 *
 */
public class FilterItemRescorer implements Rescorer<LongPair> {

	private Collection<Long> excludeItemID;

	public FilterItemRescorer(Collection<Long> excludeItemID) {
		this.excludeItemID = excludeItemID;
	}

	@Override
	public double rescore(LongPair thing, double originalScore) {
		return isFiltered(thing) ? Double.NaN : originalScore;
	}

	/*
	 * 此处LongPair的First和Second都是ItemID
	 * 
	 * 如果First被过滤(excludeItemID.contains(thing.getFirst())), 则无法获取该ItemID的推荐商品
	 * 如果Second被过滤(excludeItemID.contains(thing.getSecond())),则所有商品不会推荐该ItemID
	 */
	@Override
	public boolean isFiltered(LongPair thing) {
		return excludeItemID.contains(thing.getFirst()) || excludeItemID.contains(thing.getSecond());
	}
}
