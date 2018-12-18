import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DailyTemperaturesTest {

    public static void main (String [] args) throws IOException {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}


class DailyTemperatures{
    ArrayList<DailyTemperature> niza;

    DailyTemperatures(){
        niza=new ArrayList<>();
    }
    void readTemperatures(InputStream inputStream) throws IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));
        String s;
        while(!((s = bf.readLine()).isEmpty()))
        {
            String [] pod=s.split(" ");
            int day=Integer.parseInt(pod[0]);
            ArrayList<Double> nova=new ArrayList<>();
            for(int i=1;i<pod.length-1;i++)
            {
                if(pod[i].endsWith("F"))
                {
                    String [] c=pod[i].split("F");
                    nova.add(FarenheitToCelsius(Double.parseDouble(c[0])));

                }
                else {
                    String []c =pod[i].split("C");
                    nova.add(Double.parseDouble(c[0]));
                }
            }
            niza.add(new DailyTemperature(day,nova));

        }

    }

    double FarenheitToCelsius(double far)
    {
        return (far-32)*5/9;
    }

    double CelsiusToFarenhait(double cel)
    {
        return (cel * 5/9)-32;
    }

    void writeDailyStats(OutputStream outputStream, char scale)
    {

        PrintWriter pw=new PrintWriter(outputStream);
        Collections.sort(niza);
        for(DailyTemperature d: niza)
        {
            pw.println(d.getDay()+": Count"+d.temp.size());
            pw.println("Min:"+d.getMin());
            pw.println("Max"+d.getMax());
            pw.println("Average"+d.getAverage());

        }
        pw.flush();

    }


}


class DailyTemperature implements Comparable{
    private int day;
    ArrayList<Double> temp;


    DailyTemperature(int day,ArrayList<Double> temp)
    {
        this.day=day;
        this.temp=temp;
    }

    int getDay(){
        return day;
    }

    double getMax(){
        double max=Double.MIN_VALUE;
        for(double c:temp)
        {
            if(c>max)
                max=c;
        }

        return max;
    }

    double getMin(){
        double min = Double.MAX_VALUE;
        for(double c:temp)
        {
            if(c<min)
                min=c;
        }

        return min;
    }

    double getAverage(){
        double suma=0;
        for(double c:temp)
        {
            suma+=c;
        }
        return suma/temp.size();
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(day, ((DailyTemperature)o).day);
    }
}