package hadoop.definitive.guide.chapter000;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Hadoop权威指南样例代码 - 温度最大计算的mapper
 * 
 * Mapper是Hadoop的Mapper接口,四个参数分别定义了:输入Key,输入Value,输出Key,输出Value
 * 
 * Mapper的方法中run方法是入口方法
 * 
 * @author Administrator
 *
 */
public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
	
	private long startTime;

	/**
	 * 在任务启动前执行一次(仅一次),可以实现来完成一些预备动作
	 */
	protected void setup(Context context) throws IOException, InterruptedException {
		/*
		 * Hadoop对于输入文件切割的对象
		 */
		InputSplit split = context.getInputSplit();
		// 切割长度
		long length = split.getLength();
		// 获取本地地址
		InetAddress address = InetAddress.getLocalHost();
		// 获取主机名称
		String hostName = address.getHostName();
		// 获取IP值
		String ip = address.getHostAddress();
		startTime = System.currentTimeMillis();
		System.out.println("MaxTemperatureMapper excute local" + hostName + ":" + ip + " split:" + length);
	}

	/**
	 * 在任务完成后执行一次(仅一次),可以实现来完成一些清理动作
	 */
	protected void cleanup(Context context) throws IOException, InterruptedException {
		System.out.println("MaxTemperatureMapper excute finished:" + (System.currentTimeMillis() - startTime)/1000);
	}

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
