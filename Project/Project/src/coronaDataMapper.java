//By Qiao Xin
//using dailycoronadata.csv
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class coronaDataMapper extends Mapper<LongWritable,Text, Text, IntWritable> {
	Text countryName = new Text();
	IntWritable one = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable,Text,Text,IntWritable>.Context context)
			throws IOException, InterruptedException{
		String[] parts = value.toString().split(",");
		
		String name;
	

		int foo;
		try {
		   foo = Integer.parseInt(parts[2]);
		}
		catch (NumberFormatException e)
		{
		   foo = 0;
		}
		System.out.println(foo);
		IntWritable cases = new IntWritable(foo);
		String a = parts[0];
		a = a.replace("Country","");
		if(parts[0]!=null) {
			name = parts[0].trim();	
			if(name!=null && !name.isEmpty()) {
				countryName.set(name);	
					context.write(countryName,cases);
			}
		}
	}
}
