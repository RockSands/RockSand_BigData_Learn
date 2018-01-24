package hadoop.definitive.guide.chapter001.reader;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

/**
 * 使用流读取数据样例一
 * 
 * 不推荐此方法,该方法需要修改URL的配置.
 * @author Administrator
 *
 */
public class URIReader {
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}

	public static void main(String[] args) {
		InputStream in = null;
		try {
			in = new URL(args[0]).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
		} catch (Exception ex) {
			IOUtils.closeStream(in);
		}
	}

}
