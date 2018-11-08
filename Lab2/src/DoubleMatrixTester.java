import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
final class DoubleMatrix {
    private double matrix[][];
    private int m;
    private int n;
    DoubleMatrix(double a[], int m, int n) throws Exception{
        this.m=m;
        this.n=n;
        if(m*n > a.length)throw new InsufficientElementsException();
        this.matrix = new double[m][n];

        double[] newMatrix = Arrays.copyOfRange(a, a.length-m*n, a.length);
        IntStream.range(0,m).forEach(i->IntStream.range(0,n).forEach(j-> this.matrix[i][j] = newMatrix[i*n+j]));

    }
    String getDimensions(){
        return String.format("[%d x %d]", m,n);
    }
    int rows(){
        return m;
    }
    int columns(){
        return n;
    }
    double maxElementAtRow(int row) throws Exception{
        if(row > 0 && row <= m){
            return Arrays.stream(this.matrix[row-1]).max().getAsDouble();
        }
        throw new InvalidRowNumberException();


    }
    double maxElementAtColumn(int column) throws Exception{
        if(column > 0 && column <= n){
            return IntStream.range(0,m).mapToDouble(i-> this.matrix[i][column-1]).max().getAsDouble();
        }
        throw new InvalidColumnNumberException();
    }
    double sum(){
        return Arrays.stream(this.matrix)
                .mapToDouble(row -> Arrays.stream(row).sum())
                .sum();
    }
    double [] toSortedArray(){
        double [] niza = Arrays.stream(this.matrix).flatMapToDouble(Arrays::stream).toArray();
        Arrays.sort(niza);

        return IntStream.range(0,niza.length).mapToDouble(i-> niza[niza.length-1-i]).toArray();
    }
    double[][] getMatrix(){return matrix;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<matrix.length; i++){
           for(int j=0; j<matrix[i].length; j++){
               sb.append(String.format("%.2f",matrix[i][j]));
               if(j < matrix[i].length-1)sb.append("\t");
           }
            if(i < matrix.length-1)sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(!(obj.getClass() == getClass())) return false;
        DoubleMatrix dm = (DoubleMatrix)obj;
        return m == dm.m && n==dm.n && Arrays.deepEquals(matrix, dm.matrix);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(matrix);
        return result;
    }
}
class MatrixReader{
    public static DoubleMatrix read(InputStream inputStream) throws Exception{
        Scanner sc = new Scanner(inputStream);
        int m = sc.nextInt();
        int n  = sc.nextInt();
        double matrix[] = new double[m*n];
        IntStream.range(0,m*n).forEach(i->matrix[i] = sc.nextDouble());
        return new DoubleMatrix(matrix, m, n);
    }
}
class InsufficientElementsException extends Exception{
    InsufficientElementsException(){
        super("Insufficient number of elements");
    }
}
class InvalidRowNumberException extends  Exception{
    InvalidRowNumberException(){
        super("Invalid row number");
    }
}
class InvalidColumnNumberException extends Exception{
    InvalidColumnNumberException(){
        super("Invalid column number");
    }
}