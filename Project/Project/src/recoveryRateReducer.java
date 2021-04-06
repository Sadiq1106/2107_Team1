//By Sadiq
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class recoveryRateReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	IntWritable totalIW = new IntWritable();  
	@Override 
	protected void reduce(Text key, Iterable<IntWritable>values,Reducer<Text,IntWritable,Text,IntWritable>.Context context)
	throws IOException, InterruptedException{
		int total = 0;
		for(IntWritable value:values) {
			total += value.get();
		}
		totalIW.set(total);
		context.write(key,totalIW);
	}

}
