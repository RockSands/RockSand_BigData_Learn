package hadoop.definitive.guide.chapter002.com;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 分组
 * 按照  年-月 分组
 * @author Administrator
 *
 */
public class MyGroup extends WritableComparator{

	public MyGroup(){
		super(MyKey.class,true);
	}
	
	public int compare(WritableComparable a, WritableComparable b) {
		MyKey k1 =(MyKey) a;
		MyKey k2 =(MyKey) b;
		int r1 =Integer.compare(k1.getYear(), k2.getYear());
		if(r1==0){
			return Integer.compare(k1.getMonth(), k2.getMonth());
		}else{
			return r1;
		}
		
	}
}
