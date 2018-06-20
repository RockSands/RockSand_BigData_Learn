package hadoop.definitive.guide.chapter002.com;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

//key：每行第一个隔开符左边为key，右边为value
public class WeatherMapper extends Mapper<Text, Text, MyKey, DoubleWritable> {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	NullWritable v = NullWritable.get();

	protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		try {
			Date date = sdf.parse(key.toString());
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);

			double hot = Double.parseDouble(value.toString().substring(0, value.toString().lastIndexOf("c")));
			MyKey k = new MyKey();
			k.setYear(year);
			k.setMonth(month);
			k.setHot(hot);
			context.write(k, new DoubleWritable(hot));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
