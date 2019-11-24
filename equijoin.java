
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class equijoin{
	public static class EquijoinMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		private Text kjoin = new Text();
		private Text tuples = new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			// Read the  line and Split
			String valueString = value.toString();
			String[] linesplit = valueString.split(",");
	    	// gets the joining key and tuple
			String keyjoin = linesplit[1];
			//sets key and value
			kjoin.set(keyjoin);
			tuples.set(valueString);
			output.collect(kjoin, tuples);
		}
	}
	
	public static class EquijoinReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text,Text> output, Reporter reporter) throws IOException {
			List<String> Table1 = new ArrayList<String>();
			List<String> Table2 = new ArrayList<String>();
			boolean flag = true;
			String tablename = "";
			Text result = new Text();
			String out_line = new String();
			
			while(values.hasNext())
			{
				String valueString = values.next().toString();
				String[] linesplit = valueString.split(",");
				if(flag == true)
				{
					tablename = linesplit[0];
					flag = false;
				}
				//checks for the joining key and add it separately in a table
				if(linesplit[0].equals(tablename))
				{
					Table1.add(valueString);
				}
				else
				{
					Table2.add(valueString);
				}	
			}
			
			//Clears the key-value if doesnt match
			if(Table1.size() == 0 || Table2.size() == 0)
			{
				key.clear();
			}
			else
			{
				//Adds value of Table1 to Table2
				for(int i = 0; i < Table1.size(); i++)
				{
					for(int j = 0; j < Table2.size(); j++)
					{
						out_line = Table1.get(i) + "," + Table2.get(j);
						result.set(out_line);
						output.collect(new Text(""), result);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		JobClient my_client = new JobClient();
		// Create a configuration object for the job
		JobConf job_conf = new JobConf(equijoin.class);

		// Set a name of the Job
		job_conf.setJobName("Equijoin");

		// Specify data type of output key and value
		job_conf.setOutputKeyClass(Text.class);
		job_conf.setOutputValueClass(Text.class);

		// Specify names of Mapper and Reducer Class
		job_conf.setMapperClass(EquijoinMapper.class);
		job_conf.setReducerClass(EquijoinReducer.class);

		// Specify formats of the data type of Input and output
		job_conf.setInputFormat(TextInputFormat.class);
		job_conf.setOutputFormat(TextOutputFormat.class);

		// Set input and output directories using command line arguments, 
		//arg[0] = name of input directory on HDFS, and arg[1] =  name of output directory to be created to store the output file.
		
		FileInputFormat.setInputPaths(job_conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(job_conf, new Path(args[1]));

		my_client.setConf(job_conf);
		try {
			// Run the job
			JobClient.runJob(job_conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
