package hadoop.definitive.guide.chapter002.driver;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Hadoop权威指南样例代码 - 温度最大计算的Reducer
 * 
 * Mapper是Hadoop的Reducer接口,四个参数分别定义了:输入Key,输入Value,输出Text,输出Value
 * @author Administrator
 *
 */
public class MaxTemperatureReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
	/* 
	 * Key:Mapper的输出Key,Reduce的输入,样例为Year
	 * Value:Mapper的输出Value,Reduce的输入,样例的温度,一个Key存在多个Value,所有可以变量Iterable
	 * 
	 * Reducer的作用为计算(这里的样例计算为获取最大值)
	 */
	@Override
	public void reduce(Text key , Iterable<DoubleWritable> value,Context context) throws IOException, InterruptedException {
		double maxTemperature = Double.MIN_VALUE;
		for(DoubleWritable doubleWritable : value){
			maxTemperature = Math.max(maxTemperature, doubleWritable.get());
		}
		// 输出当年的温度最大值
		context.write(key, new DoubleWritable(maxTemperature));
	}
}
