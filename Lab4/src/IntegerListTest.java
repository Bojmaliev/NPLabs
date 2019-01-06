import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}

class IntegerList implements Cloneable {
    private List<Integer> list;

    IntegerList() {
        list = new ArrayList<>();
    }

    IntegerList(Integer... numbers) {
        list = new ArrayList<>();
        Collections.addAll(list, numbers);
    }

    void add(int el, int idx) {
        if (list.size() < idx) {
            for (int i = list.size(); i < idx; i++) list.add(i, 0);
            list.add(idx, el);
        } else {
            list.add(idx, el);
        }
    }

    int remove(int idx) {
        int el = list.get(idx);
        list.remove(idx);
        return el;
    }

    void set(int el, int idx) {
        list.set(idx, el);
    }

    int get(int idx) {
        return list.get(idx);
    }

    int size() {
        return list.size();
    }

    int count(int el) {
        return Collections.frequency(list, el);
    }

    void removeDuplicates() {
        List<Integer> temp = new ArrayList<>();
        ListIterator<Integer> li = list.listIterator(list.size());

        while (li.hasPrevious()) {
            int elem = li.previous();
            if (temp.indexOf(elem) == -1) {
                temp.add(0, elem);
            }
        }
        this.list = temp;
    }

    int sumFirst(int k) {
        boolean test = k < 0;
        if (test) throw new ArrayIndexOutOfBoundsException();
        return list
                .stream()
                .limit(k)
                .mapToInt(Integer::intValue)
                .sum();
    }

    int sumLast(int k) {
        if (k < 0) throw new ArrayIndexOutOfBoundsException();
        return list
                .stream()
                .skip(size() - k)
                .mapToInt(Integer::intValue)
                .sum();
    }

    void shiftRight(int idx, int k) {
        if (idx > size() || idx < 0) throw new ArrayIndexOutOfBoundsException();
        int newIndex = idx + k;
        int elem = list.get(idx);
        list.remove(idx);
        if (newIndex > size()) {
            list.add(newIndex % (size() + 1), elem);
        } else {
            list.add(newIndex, elem);
        }
    }
    void shiftLeft(int idx, int k){
        //System.out.print(idx+"**"+k+":");
        int n = idx;
        for(int i=0; i<k%size(); i++){
            int m;
            if(n == 0) {
                int elem = list.get(0);
                list.remove(0);
                list.add(size(), elem);
                n=size()-1;
            }
            else {
                m = n - 1;
                Collections.swap(list, m,n);
                n--;
            }

        }
    }

    IntegerList addValue(int value) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i : list) temp.add(i);
        return new IntegerList(temp.stream()
                .map(a -> a + value)
                .toArray(Integer[]::new));

    }

    @Override
    public String toString() {
        return list.toString();
    }
}