package hadoop.definitive.guide.chapter002.weather;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 分析出每个月温度最高的三条记录
 * 
 * @author Administrator
 *
 */
public class RunJob {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		config.set("fs.defaultFS", "hdfs://192.168.80.152:8020");
		config.set("yarn.resourcemanager.hostname", "192.168.80.151");
		// config.set("mapred.jar", "C:\\Users\\Administrator\\Desktop\\wc.jar");
		// config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator",
		// ",");
		try {
			FileSystem fs = FileSystem.get(config);

			Job job = Job.getInstance(config);
			job.setJarByClass(RunJob.class);

			job.setJobName("weather");

			job.setMapperClass(WeatherMapper.class);
			job.setReducerClass(WeatherReducer.class);
			job.setMapOutputKeyClass(MyKey.class);
			job.setMapOutputValueClass(DoubleWritable.class);

			job.setPartitionerClass(MyPartitioner.class);
			job.setSortComparatorClass(MySort.class);
			job.setGroupingComparatorClass(MyGroup.class);
			// 开启3个Reduce线程
			job.setNumReduceTasks(3);
			// KeyValueTextInputFormat内部指定了输入的分隔符  分隔符为\t
			job.setInputFormatClass(KeyValueTextInputFormat.class);

			FileInputFormat.addInputPath(job, new Path("/usr/input/weather"));

			Path outpath = new Path("/usr/output/weather");
			if (fs.exists(outpath)) {
				fs.delete(outpath, true);
			}
			FileOutputFormat.setOutputPath(job, outpath);

			System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
