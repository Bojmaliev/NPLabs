import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int niza[] = new int[]{1,2,3};
        int nova[] = Arrays.copyOf(niza, 5);
        for(int i:nova) System.out.println(i);
    }
}
