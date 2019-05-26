import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Hw3 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Hw3");
        job.setJarByClass(Hw3.class);

        String runType = args[0];

        if(runType.equals("game"))
        {
            job.setMapperClass(Game.GameMapper.class);
            job.setCombinerClass(Game.GameReducer.class);
            job.setReducerClass(Game.GameReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        else if(runType.equals("point"))
        {
            job.setMapperClass(Point.PointMapper.class);
            job.setCombinerClass(Point.PointReducer.class);
            job.setReducerClass(Point.PointReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }
        else if(runType.equals("avg"))
        {
            job.setMapperClass(Avg.AvgMapper.class);
            job.setReducerClass(Avg.AvgReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
        }

        else if(runType.equals("stat"))
        {
            job.setNumReduceTasks(3);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            job.setMapperClass(Stat.StatMapper.class);
            job.setReducerClass(Stat.StatReducer.class);
            job.setPartitionerClass(Stat.StatPartitioner.class);

            job.setInputFormatClass(TextInputFormat.class);
            job.setOutputFormatClass(TextOutputFormat.class);

        }


        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}