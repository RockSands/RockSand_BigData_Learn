package hadoop.definitive.guide.chapter001.reader;

import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * 使用流读取数据样例二
 * 
 * 推荐此方法
 * 
 * @author Administrator
 * 参数: /gsod
 */
public class FileSystemReader {

    public static void main(String[] args) {
	String uri = args[0];
	Configuration conf = new Configuration();
	conf.set("fs.defaultFS", "hdfs://192.168.80.152:8020");
	conf.set("yarn.resourcemanager.hostname", "192.168.80.151");
	FileSystem fs = null;
	InputStream in = null;
	try {
	    fs = FileSystem.get(URI.create(uri), conf);
	    in = fs.open(new Path("hdfs://192.168.80.152:8020" + uri));
	    IOUtils.copyBytes(in, System.out, 4096, false);
	} catch (Exception ex) {
	    IOUtils.closeStream(in);
	}
    }

}
