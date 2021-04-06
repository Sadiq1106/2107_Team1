//By Sadiq
//country-covid1.csv
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class recoveryRateMapper extends Mapper<LongWritable,Text, Text, IntWritable> {
	Text countryName = new Text();
	IntWritable one = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable,Text,Text,IntWritable>.Context context)
			throws IOException, InterruptedException{
		String[] parts = value.toString().split(",");
		
		String name;
		double recovery =Double.parseDouble(parts[3]);
		double confirmed = Double.parseDouble(parts[1]);
		
		int percentage = (int)((recovery/confirmed)*100);
		IntWritable recoveryRate = new IntWritable(percentage);
		System.out.println(percentage);
		if(parts[0]!=null) {
			name = parts[0].trim();	
			if(name!=null && !name.isEmpty()) {
				countryName.set(name);	
				
					context.write(countryName,recoveryRate);
				
				
			}
		}
	}
}
