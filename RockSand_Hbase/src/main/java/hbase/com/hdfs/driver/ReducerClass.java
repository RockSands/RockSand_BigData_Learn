package hbase.com.hdfs.driver;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;

public class ReducerClass extends TableReducer<Text,Text,ImmutableBytesWritable>{
    public void reduce(Text key,Iterable<Text> values,Context context){
        String k = key.toString();
        StringBuffer str=null;
        for(Text value: values){
            str.append(value.toString());
        }
        String v = new String(str); 
        Put putrow = new Put(k.getBytes());
        putrow.add("cf1".getBytes(), "name".getBytes(), v.getBytes());
    }
}
