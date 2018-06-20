package hadoop.definitive.guide.chapter002.com;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WeatherReducer extends Reducer<MyKey, DoubleWritable, Text, NullWritable> {
	protected void reduce(MyKey arg0, Iterable<DoubleWritable> arg1, Context arg2)
			throws IOException, InterruptedException {
		int i = 0;
		for (DoubleWritable v : arg1) {
			i++;
			String msg = arg0.getYear() + "\t" + arg0.getMonth() + "\t" + v.get();
			arg2.write(new Text(msg), NullWritable.get());
			if (i == 3) {
				break;
			}
		}
	}

}
