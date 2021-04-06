import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class gdpvaccinationdata extends Mapper<LongWritable,Text, Text, IntWritable> {
	//For vaccination data
	Text country = new Text();
	IntWritable one = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable,Text,Text,IntWritable>.Context context)
			throws IOException, InterruptedException{
		String[] parts = value.toString().split(",");
		double vac =Double.parseDouble(parts[2]);
		double population = Double.parseDouble(parts[3]);
		
		int percentage = (int)((vac/population)*100);
		
		//System.out.println((vac/population) *100);
		String countryName;
		IntWritable test = new IntWritable(percentage);
		
		if(parts[4]!=null) {
			countryName = parts[4].trim();
			if(countryName!=null && !countryName.isEmpty()) {
				country.set(parts[1]);
				context.write(country,test);
			}
		}
	}
}
