import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Stat {

    public static class StatMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        private Text win = new Text("HOME_WIN");
        private Text lose = new Text("AWAY_WIN");
        private Text tie =  new Text("DRAW");


        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                String sWord = word.toString();

                if (sWord.length() > 0) {
                    if (!Pattern.matches(".*[a-z]+.*", sWord)) {
                        System.out.println(sWord);
                        if(Integer.parseInt(sWord) == 0)
                        {
                            context.write(tie, one);
                        }
                        else if(Integer.parseInt(sWord) == 1)
                        {
                            context.write(win, one);
                        }
                        else if(Integer.parseInt(sWord) == 2)
                        {
                            context.write(lose, one);
                        }
                    }
                }

            }
        }
    }

    public static class StatPartitioner extends Partitioner<Text,IntWritable> {
        public int getPartition(Text key, IntWritable value, int numReduceTasks){
            if(numReduceTasks==0)
                return 0;
            if(key.equals(new Text("DRAW")))
                return 0;
            if(key.equals(new Text("HOME_WIN")))
                return 1;
            if(key.equals(new Text("AWAY_WIN")))
                return 2;
            else
                return 3;
        }
    }

    public static class StatReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
}
