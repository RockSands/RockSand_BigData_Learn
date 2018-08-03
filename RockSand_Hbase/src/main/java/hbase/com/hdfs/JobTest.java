package hbase.com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import hbase.com.hdfs.driver.ReducerClass;

/**
 * Created by root on 2016/3/5 0005.
 */
public class JobTest {
    public static void main(String[] args) throws Exception {
        Configuration config =new Configuration();
        config.set("fs.defaultFS", "hdfs://master:8020");
        config.set("yarn.resourcemanager.hostname", "master");
        config.set("mapred.job.tracker", "192.168.57.4:9001");
        config.set("ha.zookeeper.quorum","master,slave1,slave2");
        Job job = new Job(config,"Hbase");
        job.setJarByClass(JobTest.class);
        FileSystem fs =FileSystem.get(config);
        Path in = new Path("/user/test/test.txt");
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, in);

        job.setMapperClass(HbaseMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        TableMapReduceUtil.initTableReducerJob("t_user", ReducerClass.class, job,null,null,null,null,false);

        boolean f =  job.waitForCompletion(true);
        if(f){
            System.out.println("mr 执行成功！");
        }

    }
}
