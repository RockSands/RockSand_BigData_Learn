package hadoop.definitive.guide.chapter002.friends;

import org.apache.hadoop.io.Text;

/**
 * 表名是朋友
 * @author Administrator
 *
 */
public class Fof extends Text{

	public Fof(){
		super();
	}
	
	public Fof(String a,String b){
		super(getFof(a, b));
	}

	/*
	 * 朋友关系  A与B是朋友  B与A是朋友.  所以逻辑上回出现2次
	 * 为了减少这种重复,所以下方返回一个值.  可以在group上识别出这种情况
	 */
	public static String getFof(String a,String b){
		int r =a.compareTo(b);
		if(r<0){
			return a+"\t"+b;
		}else{
			return b+"\t"+a;
		}
	}
}
