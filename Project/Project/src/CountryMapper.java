//By Astra
//using tweets.csv
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class CountryMapper extends Mapper<LongWritable,Text, Text, IntWritable> {
	Text sentimentValue = new Text();
	IntWritable one = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable,Text,Text,IntWritable>.Context context)
			throws IOException, InterruptedException{
		String[] parts = value.toString().split(",");
		System.out.println(parts[2]);
		String sentiment;
		if(parts[2]!=null) {
			sentiment = parts[2].trim();
			if(sentiment!=null && !sentiment.isEmpty()) {
				sentimentValue.set(sentiment);
				context.write(sentimentValue,one);
			}
		}
	}
}
