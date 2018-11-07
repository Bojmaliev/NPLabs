import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class IntegerArrayTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        IntegerArray ia = null;
        switch (s) {
            case "testSimpleMethods":
                ia = new IntegerArray(generateRandomArray(scanner.nextInt()));
                testSimpleMethods(ia);
                break;
            case "testConcat":
                testConcat(scanner);
                break;
            case "testEquals":
                testEquals(scanner);
                break;
            case "testSorting":
                testSorting(scanner);
                break;
            case "testReading":
                testReading(new ByteArrayInputStream(scanner.nextLine().getBytes()));
                break;
            case "testImmutability":
                int a[] = generateRandomArray(scanner.nextInt());
                ia = new IntegerArray(a);
                testSimpleMethods(ia);
                testSimpleMethods(ia);
                IntegerArray sorted_ia = ia.getSorted();
                testSimpleMethods(ia);
                testSimpleMethods(sorted_ia);
                sorted_ia.getSorted();
                testSimpleMethods(sorted_ia);
                testSimpleMethods(ia);
                a[0] += 2;
                testSimpleMethods(ia);
                ia = ArrayReader.readIntegerArray(new ByteArrayInputStream(integerArrayToString(ia).getBytes()));
                testSimpleMethods(ia);
                break;
        }
        scanner.close();
    }

    static void testReading(InputStream in) {
        IntegerArray read = ArrayReader.readIntegerArray(in);
        System.out.println(read);
    }

    static void testSorting(Scanner scanner) {
        int[] a = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        System.out.println(ia.getSorted());
    }

    static void testEquals(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        int[] c = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        IntegerArray ib = new IntegerArray(b);
        IntegerArray ic = new IntegerArray(c);
        System.out.println(ia.equals(ib));
        System.out.println(ia.equals(ic));
        System.out.println(ib.equals(ic));
    }

    static void testConcat(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        IntegerArray array1 = new IntegerArray(a);
        IntegerArray array2 = new IntegerArray(b);
        IntegerArray concatenated = array1.concat(array2);
        System.out.println(concatenated);
    }

    static void testSimpleMethods(IntegerArray ia) {
        System.out.print(integerArrayToString(ia));
        System.out.println(ia);
        System.out.println(ia.sum());
        System.out.printf("%.2f\n", ia.average());
    }


    static String integerArrayToString(IntegerArray ia) {
        StringBuilder sb = new StringBuilder();
        sb.append(ia.length()).append('\n');
        for (int i = 0; i < ia.length(); ++i)
            sb.append(ia.getElementAt(i)).append(' ');
        sb.append('\n');
        return sb.toString();
    }

    static int[] readArray(Scanner scanner) {
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
        }
        return a;
    }


    static int[] generateRandomArray(int k) {
        Random rnd = new Random(k);
        int n = rnd.nextInt(8) + 2;
        int a[] = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = rnd.nextInt(20) - 5;
        }
        return a;
    }

}

final class IntegerArray{
    private int[] niza;
    IntegerArray(int a[]){
        niza = a.clone();
    }
    int length(){
        return niza.length;
    }
    int getElementAt(int i){
        return niza[i];
    }
    int sum(){
        return Arrays.stream(niza).reduce((left, right) -> left+right).getAsInt();
    }
    double average(){
        return sum()/(double)length();
    }
    IntegerArray getSorted(){
        int[] nova = niza.clone();
        Arrays.sort(nova);
        return new IntegerArray(nova);
    }
    IntegerArray concat(IntegerArray ia){
        int newArr[] = new int[ia.length()+this.length()];
        int i;
        for(i=0; i<this.length(); i++){
            newArr[i] = this.niza[i];
        }
        for(;i<this.length()+ia.length(); i++){
            newArr[i] = ia.getElementAt(i-this.length());
        }
        return new IntegerArray(newArr);
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
            for(int i=0; i<this.length(); i++){
                String delimer = ", ";
                if(i==this.length()-1)delimer="";
                toReturn.append(getElementAt(i)+delimer);
            }
        return "["+toReturn.toString()+"]";
    }

    @Override
    public boolean equals(Object obj) {
        return Arrays.equals(this.niza, ((IntegerArray)obj).niza);
    }
}

class ArrayReader{
    public static IntegerArray readIntegerArray(InputStream input){
        Scanner in = new Scanner(input);
        int n = in.nextInt();
        int nova[] = new int[n];

        for(int i=0; i<n; i++){
            nova[i]  = in.nextInt();
        }

        return new IntegerArray(nova);
    }
}