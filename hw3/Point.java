import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class Point {

    public static class PointMapper
            extends Mapper<Object, Text, Text, IntWritable>{

        private final static IntWritable zero = new IntWritable(0);
        private final static IntWritable one = new IntWritable(1);
        private final static IntWritable three = new IntWritable(3);

        private Text team1 = new Text();
        private Text team2 = new Text();
        private Text res = new Text();



        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                try{
                    team1.set(itr.nextToken());
                    team2.set(itr.nextToken());
                    res.set(itr.nextToken());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }

                System.out.println(team1.toString());
                System.out.println(team2.toString());
                System.out.println(res.toString());

                if(Integer.parseInt(res.toString()) == 0)
                {
                    context.write(team1, one);
                    context.write(team2, one);
                }
                else if(Integer.parseInt(res.toString()) == 1)
                {
                    context.write(team1, three);
                    context.write(team2, zero);
                }
                else if(Integer.parseInt(res.toString()) == 2)
                {
                    context.write(team1, zero);
                    context.write(team2, three);
                }

            }
        }
    }

    public static class PointReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
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
