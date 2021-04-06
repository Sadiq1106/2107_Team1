//By Linus
//Using country-covid1
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class top5 extends Mapper<LongWritable,Text, Text, IntWritable> {
	Text regionValue = new Text();
	IntWritable one = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable,Text,Text,IntWritable>.Context context)
			throws IOException, InterruptedException{
		String[] parts = value.toString().split(",");
		System.out.println(parts[0]);
		String region;
		IntWritable total = new IntWritable(Integer.parseInt(parts[1]));
	
		if(parts[14]!=null) {
			region = parts[0].trim();	
			if(region!=null && !region.isEmpty()) {
				regionValue.set(region);	
				if(total != null)
				{
					IntWritable test = total;
					context.write(regionValue,test);
				}
				
			}
		}
	}
}
