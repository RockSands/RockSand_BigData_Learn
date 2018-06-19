package hadoop.definitive.guide.chapter002.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 该写法可以避免,Java参数的动态参数 样例: hadoop -jar xxx.jar -Dmapreduce.job.reduces=2
 * hdfs://192.168.80.152:8020/gsod/2015/999999-96408-2015.op
 * hdfs://192.168.80.152:8020/output/ ToolRunner.run(driver,
 * args)方法会将常规参数抽取,在将剩余的参数传递给args中
 * 
 * @author Administrator
 *
 */
public class MaxTemperatureDriver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
	MaxTemperatureDriver driver = new MaxTemperatureDriver();
	ToolRunner.run(driver, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int run(String[] args) throws Exception {
	if (args.length != 2) {
	    System.err.println("参数数量不足!");
	    System.exit(-1);
	}
	/*
	 * 删除之前的output,避免异常
	 */
	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(conf);
	// 新建Job
	JobConf job = new JobConf(conf, MaxTemperatureDriver.class);
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
	job.setJarByClass(MaxTemperatureDriver.class);
	job.setJobName("Max Temperature");
	// job设定reduce进程数,如果修改为2,则返回目录的part应该为2个
	// job.setNumReduceTasks(2);
	// 设定这个Job的输入、输出文件
	FileInputFormat.setInputPaths(job, new Path(args[0]));
	FileOutputFormat.setOutputPath(job, outpath);

	// 定义Mapper和Reducer
	job.setMapperClass((Class<? extends Mapper>) MaxTemperatureMapper.class);
	job.setCombinerClass((Class<? extends Reducer>) MaxTemperatureReducer.class);
	job.setReducerClass((Class<? extends Reducer>) MaxTemperatureReducer.class);

	// 定义Job输出的类型
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(DoubleWritable.class);
	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(DoubleWritable.class);

	// 设置Job的Partitioner,将Map的Key统一发布到相同的Reduce线程,Hash为默认
	// job.setPartitionerClass(HashPartitioner.class);
	// 退出
	return 0;
    }

}
