package hadoop.definitive.guide.chapter001.writer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * 输入
 * @author Administrator
 *
 */
public class FileSystemWriter {
	public static void main(String[] args) {
		String readerUri = args[0];
		String writerUri = args[1];
		Configuration conf = new Configuration();
		FileSystem fs = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			fs = FileSystem.get(URI.create(readerUri),conf);
			in = fs.open(new Path(readerUri));
			// 创建输出流, 第二个参数定义是否覆盖
			out = fs.create(new Path(writerUri),true);
			IOUtils.copyBytes(in, out, 4096, false);
		} catch (Exception ex) {
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
	}
}
