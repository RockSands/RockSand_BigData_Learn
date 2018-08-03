package hbase.com.hdfs.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;

public class Driver{

    public static void main(String[] arg0) throws Exception {
        // TODO Auto-generated method stub
        Configuration config =new Configuration();
        config.set("fs.defaultFS", "hdfs://slave1:8020");
        config.set("yarn.resourcemanager.hostname", "master");
        config.set("mapred.job.tracker", "192.168.57.4:9001");
        config.set("ha.zookeeper.quorum","master,slave1,slave2");
        Job job = new Job(config,"Hbase");
        job.setJarByClass(Driver.class);
        FileSystem fs =FileSystem.get(config);
        Path in = new Path("/user/test/test.txt");
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, in);
        
        job.setMapperClass(MapperClass.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        
        TableMapReduceUtil.initTableReducerJob("t_user", ReducerClass.class, job,null,null,null,null,false);
        
      boolean f =  job.waitForCompletion(true);
        if(f){
            System.out.println("mr 执行成功！");
        }


//        try {
//            Job job =Job.getInstance(config);
//            job.setJobName("wc");
//            FileSystem fs =FileSystem.get(config);
//            job.setJarByClass(RunJob.class);
//            job.setMapperClass(WordCountMapper.class);
//            job.setReducerClass(WordCountReducer.class);
//
//            job.setMapOutputKeyClass(Text.class);
//            job.setMapOutputValueClass(IntWritable.class);
//
//            job.setNumReduceTasks(1);
//
//            //mapreduce 输入文件
//            FileInputFormat.setInputPaths(job, new Path("/usr/input/wc.txt"));
//
//            //设置mapreduce的输出文件目录，该目录不能存在，自动创建
//            Path outpath =new Path("/user/output/wc");
//            if(fs.exists(outpath)){
//                fs.delete(outpath, true);
//            }
//            FileOutputFormat.setOutputPath(job, outpath);
//
//            boolean f= job.waitForCompletion(true);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
    
}
