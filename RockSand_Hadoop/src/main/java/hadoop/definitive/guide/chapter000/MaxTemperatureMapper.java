package hadoop.definitive.guide.chapter000;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Hadoop权威指南样例代码 - 温度最大计算的mapper
 * 
 * Mapper是Hadoop的Mapper接口,四个参数分别定义了:输入Key,输入Value,输出Text,输出Value
 * 
 * @author Administrator
 *
 */
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
	/*
	 * key,输入Key Value,输入Value Context,Hadoop的环境变量
	 * 
	 * 逻辑为,读取温度的Txt的每一行,截取温度与时间. 并以时间为Key,温度为Value写入传递至Reducer函数. Mapper的作用为分组
	 */
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] lineArr = null;
		double airTemperature;
		String year = "";
		if (!line.startsWith("STN")) {
			lineArr = line.split("\\s+");
			year = lineArr[2].substring(0, 4);
			if (lineArr[3].matches("([1-9]+[0-9]*|0)(\\.[\\d]+)?")) {
				airTemperature = Double.parseDouble(lineArr[3]);
				// 写入Context后,Reducer会获取
				if (airTemperature < 100) {
					context.write(new Text(year), new DoubleWritable(airTemperature));
				}
			}
		}
	}
}
