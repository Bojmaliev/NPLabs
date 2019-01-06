import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        IntegerList il = new IntegerList();
        il.add(1, 0);
        il.add(2, 1);
        il.add(3, 2);
        il.add(4, 3);
        System.out.println(il);
        il.shiftRight(2,3);
        System.out.println(il);
    }
}
