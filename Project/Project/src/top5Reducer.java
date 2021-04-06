import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class top5Reducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	IntWritable totalIW = new IntWritable();
	TreeMap<Integer,String> map=new TreeMap<Integer,String>();    
	@Override 
	protected void reduce(Text key, Iterable<IntWritable>values,Reducer<Text,IntWritable,Text,IntWritable>.Context context)
	throws IOException, InterruptedException{
		int total = 0;
		for(IntWritable value:values) {
			total += value.get();
		}
		map.put(total, key.toString());
		if(map.size()>5){
			map.remove(map.firstKey());
		}	
	}
	@Override 
	protected void cleanup(Context context) throws IOException,InterruptedException{
		 for (Map.Entry<Integer, String> entry : map.entrySet())  
	        { 
	  
	            int count = entry.getKey(); 
	            String name = entry.getValue(); 
	  
	            context.write(new Text(name), new IntWritable(count)); 
	        } 
	}

}
