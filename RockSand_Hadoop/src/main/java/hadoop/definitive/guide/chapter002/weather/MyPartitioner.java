package hadoop.definitive.guide.chapter002.weather;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

/**
 * 分区函数
 * 没调记录都会调用,返回值确定哪个Reduce执行,所以逻辑简单即可
 * @author Administrator
 *
 */
public class MyPartitioner extends HashPartitioner<MyKey, DoubleWritable>{

	//执行时间越短越好
	public int getPartition(MyKey key, DoubleWritable value, int numReduceTasks) {
		return (key.getYear()-1949)%numReduceTasks;
	}
}
