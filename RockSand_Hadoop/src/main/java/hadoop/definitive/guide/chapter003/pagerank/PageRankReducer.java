package hadoop.definitive.guide.chapter003.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PageRankReducer extends Reducer<Text, Text, Text, Text> {
    protected void reduce(Text arg0, Iterable<Text> arg1, Context arg2) throws IOException, InterruptedException {
	double sum = 0.0;
	Node sourceNode = null;
	for (Text i : arg1) {
	    Node node = Node.fromMR(i.toString());
	    if (node.containsAdjacentNodes()) {// 表示标准格式,  即位被其他页面指向加权的页面
		sourceNode = node;
	    } else {
		sum = sum + node.getPageRank();// 表示由其他页面指向,进行了加分
	    }
	}
	// 4表示页面的个数,正常是由其他MapReduce计算得出
	double newPR = (0.15 / 4) + (0.85 * sum);
	System.out.println("*********** new pageRank value is " + newPR);

	// 把新的pr值和计算之前的pr比较
	double d = newPR - sourceNode.getPageRank();

	int j = (int) (d * 1000.0);
	j = Math.abs(j);
	System.out.println(j + "___________");
	// Hadoop的计数器
	arg2.getCounter(Mycounter.my).increment(j);

	sourceNode.setPageRank(newPR);
	arg2.write(arg0, new Text(sourceNode.toString()));
    }
}
