package hbase.com.hdfs;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperClass extends Mapper<LongWritable,Text,Text,Text>{
        public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            String[] items = value.toString().split(",");
            String k = items[0];
            String v = items[1];         
            context.write(new Text(k), new Text(v));
    }

}
