import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class F1Race{
    private ArrayList<F1Racer> racers;
    F1Race(){
        racers = new ArrayList<>();
    }
    void readResults(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while(!(line = br.readLine()).isEmpty()){
            String[] parts = line.split(" ");
            F1Racer f1Racer = new F1Racer(parts[0], parts[1], parts[2], parts[3]);
            racers.add(f1Racer);
        }
    }
    void printSorted(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        Collections.sort(racers);
        int i=1;
        for(F1Racer r : racers) {
            pw.println(String.format("%d. %s", i, r));
            i++;
        }
        pw.flush();
    }
}
class F1Racer implements Comparable<F1Racer>{
    private String name;
    private ArrayList<Time> times;

    public F1Racer(String name, String ...times){
        this.name = name;
        this.times = new ArrayList<>();
        for(String s: times)
            this.times.add(new Time(s));
    }
    public Time getQuickestTime(){
        Collections.sort(times);
        return times.get(0);
    }

    @Override
    public int compareTo(F1Racer o) {
        if(getQuickestTime().compareTo(o.getQuickestTime()) == 0){
            return name.compareTo(o.name);
        }
        return getQuickestTime().compareTo(o.getQuickestTime());
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, getQuickestTime());
    }
}
class Time implements Comparable<Time>{
    int mm;
    int ss;
    int nnn;
    Time(String time){
        String parts[] = time.split(":");
        mm = Integer.parseInt(parts[0]);
        ss = Integer.parseInt(parts[1]);
        nnn = Integer.parseInt(parts[2]);
    }

    @Override
    public int compareTo(Time o) {
        if(mm != o.mm){
            return Integer.compare(mm, o.mm);
        }else if(ss != o.ss){
            return Integer.compare(ss, o.ss);
        }else if(nnn != o.nnn)
            return Integer.compare(nnn, o.nnn);
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d:%d:%d", mm,ss,nnn);
    }
}