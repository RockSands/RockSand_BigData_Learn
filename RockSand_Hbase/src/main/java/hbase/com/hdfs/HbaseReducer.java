package hbase.com.hdfs;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Created by root on 2016/3/5 0005.
 */
public class HbaseReducer extends TableReducer<Text,Text,ImmutableBytesWritable> {
    public void reduce(Text key, Iterable<Text> values, Reducer.Context context){
        String k = key.toString();
        StringBuffer str= new StringBuffer();
        for(Text value: values){
            str.append(value.toString()).append(",");
        }
        if(str.length()>0){
            str.deleteCharAt(str.length()-1);
        }
        String v = new String(str);
        Put putrow = new Put(k.getBytes());
        putrow.addColumn("cf1".getBytes(), "name".getBytes(), v.getBytes());
    }
}
