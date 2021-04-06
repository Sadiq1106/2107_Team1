import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class GDPVaccineAnalysis {
	public static void main(String[] args) throws Exception
    {
        Configuration conf = new Configuration();
  
  
        Job job = Job.getInstance(conf, "vaccineGDP");
        job.setJarByClass(Analysis.class);
  
        job.setMapperClass(gdpvaccinationdata.class);
        job.setReducerClass(gdpvaccinationreducer.class);
  
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(LongWritable.class);
  
    	job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		Path inputPath = new Path("hdfs://localhost:9000/user/phamvanvung/project/input/vaccinationdata.csv");
		Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/project/output/gdpvaccination/" + new Date().getTime());//use run-time as output folder
	
//		Path inputPath = new Path("hdfs://hadoop-master:9000/user/ict1902699/project/input/tweets.csv");
//		Path outputPath = new Path("hdfs://hadoop-master:9000/user/ict1902699/project/output/"
//		 + new Date().getTime());//use run-time as output folder
  
        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
  
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
