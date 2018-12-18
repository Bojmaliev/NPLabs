import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Bruno {
    public static void main(String[] args) {
        int [] niza = new int[]{12,7,2,8,5};

        Arrays.sort(niza);
        int [] nova = new int[niza.length];
        for(int i=0; i<niza.length; i++) nova[i] = niza[niza.length-1-i];

        for(int i:nova) System.out.println(i);
    }
}
