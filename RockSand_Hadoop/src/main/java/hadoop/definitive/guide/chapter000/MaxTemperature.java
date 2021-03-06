package hadoop.definitive.guide.chapter000;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 温度运行Main函数
 * 
 * @author Administrator
 *
 */
public class MaxTemperature {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		if (args.length != 2) {
			System.err.println("参数数量不足!");
			System.exit(-1);
		}
		/*
		 * 删除之前的output,避免异常
		 */
		Configuration conf = new Configuration();
		// 必须是active的Hadoop地址,standby无法执行
		FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.80.152:8020"), conf);
		// 新建Job
		Job job = Job.getInstance(conf);
		// 删除output
		Path outpath = new Path("/output");
		System.out.println(fs.exists(outpath));
		if (fs.exists(outpath)) {
			fs.delete(outpath, true);
		}
		System.out.println(fs.exists(outpath));
		// 删除tmp
		Path tmppath = new Path("/tmp");
		fs.deleteOnExit(tmppath);

		// 任务为本身,Hadoop会将任务打包成Jar
		job.setJarByClass(MaxTemperature.class);
		job.setJobName("Max Temperature");
		// job设定reduce进程数,如果修改为2,则返回目录的part应该为2个
		job.setNumReduceTasks(2);
		// 设定这个Job的输入文件
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// 设定这个Job的输出文件
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		// 定义Mapper和Reducer
		job.setMapperClass(MaxTemperatureMapper.class);
		job.setCombinerClass(MaxTemperatureReducer.class);
		job.setReducerClass(MaxTemperatureReducer.class);

		// 定义Job输出的类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DoubleWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		// 退出
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
