package hadoop.definitive.guide.chapter003.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PageRankMapper extends Mapper<Text, Text, Text, Text> {
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
	// 获取环境变量,运行次数
	int runCount = context.getConfiguration().getInt("runCount", 1);
	String page = key.toString();
	Node node = null;
	if (runCount == 1) {// 第一次的时候,需要给第一个元素初始值,这里给的1.  这个值随意填写因为算法会将这个数收敛至一个值.
	    node = Node.fromMR("1.0" + "\t" + value.toString());
	} else {
	    node = Node.fromMR(value.toString());
	}
	// 将自己的分数  以及指向页面写入
	// 这种写法 生成的格式与  input的格式一致,  所以可以递归
	context.write(new Text(page), new Text(node.toString()));// A:1.0 B D
	if (node.containsAdjacentNodes()) {// 如果存在
	    double outValue = node.getPageRank() / node.getAdjacentNodeNames().length;
	    for (int i = 0; i < node.getAdjacentNodeNames().length; i++) {
		String outPage = node.getAdjacentNodeNames()[i];
		// 将指向页面在  该页面获取的分数 写入
		context.write(new Text(outPage), new Text(outValue + ""));// B:0.5 D:0.5
	    }
	}
    }
}
