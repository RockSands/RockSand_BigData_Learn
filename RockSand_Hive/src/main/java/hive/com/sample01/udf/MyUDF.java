package hive.com.sample01.udf;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.serde2.io.TimestampWritable;
import org.apache.hadoop.io.Text;

/**
 * 自定义UDF
 * 
 * 使用说明: 1.导出Jar包 2.发送到hive服务器 3.
 * 
 * 将出入的 时间字符串 时间格式 转换成 时间戳输出
 * 
 * @author Administrator
 *
 */
public class MyUDF extends UDF {

	public Timestamp evaluate(final Text date, final Text fmt) throws ParseException {
		/*
		 * Null判断必须添加,否则肯定报错
		 */
		if (date == null || fmt == null) {
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(fmt.toString());
		Date parse = sd.parse(date.toString());
		TimestampWritable tw = new TimestampWritable();
		tw.setTime(parse.getTime());
		return tw.getTimestamp();
	}
}
