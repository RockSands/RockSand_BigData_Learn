package hadoop.definitive.guide.chapter003.pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 仿照google的pageRank计算各个页面的评分
 * 
 * 
 * @author Administrator
 *
 */
public class RunJob {

    public static void main(String[] args) {
	Configuration config = new Configuration();
	config.set("fs.defaultFS", "hdfs://192.168.80.152:8020");
	config.set("yarn.resourcemanager.hostname", "192.168.80.151");
	double d = 0.001;//误差, 当两次计算的误差在0.001内表示收敛,不再进行计算
	int i = 0;//计数器
	while (true) {// 重复调用Hadoop进行计算,  直到计算的值收敛为止
	    i++;
	    try {
		config.setInt("runCount", i);//设置Job的环境变量
		FileSystem fs = FileSystem.get(config);
		Job job = Job.getInstance(config);
		job.setJarByClass(RunJob.class);
		job.setJobName("pr" + i);
		job.setMapperClass(PageRankMapper.class);
		job.setReducerClass(PageRankReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		Path inputPath = new Path("/usr/input/pagerank.txt");
		if (i > 1) {
		    inputPath = new Path("/usr/output/pr" + (i - 1));
		}
		FileInputFormat.addInputPath(job, inputPath);

		Path outpath = new Path("/usr/output/pr" + i);
		if (fs.exists(outpath)) {
		    fs.delete(outpath, true);
		}
		FileOutputFormat.setOutputPath(job, outpath);

		boolean f = job.waitForCompletion(true);
		if (f) {
		    System.out.println("success.");
		    long sum = job.getCounters().findCounter(Mycounter.my).getValue();
		    System.out.println(sum);
		    double avgd = sum / 4000.0;
		    if (avgd < d) {
			break;
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
