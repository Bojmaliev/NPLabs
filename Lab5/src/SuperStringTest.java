
import java.util.*;

public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                }
            }
        }
    }

}

class SuperString {
    List<String> lists;
    List<String> queue;

    SuperString() {
        lists = new LinkedList<>();
        queue = new LinkedList<>();
    }
    void append(String s){
        lists.add(s);
        queue.add(0,s);
    }
    void insert(String s){
        lists.add(0, s);
        queue.add(0,s);
    }

    boolean contains(String s){
        return toString().contains(s);
    }
    void reverse(){
        Collections.reverse(lists);
        for(int i=0; i<lists.size(); i++){
            String sodrzina = lists.get(i);
            int indexInQueue = queue.indexOf(sodrzina);
            StringBuilder sb = new StringBuilder(sodrzina);

            String newRef = sb.reverse().toString();
            queue.set(indexInQueue, newRef);
            lists.set(i,newRef);
        }

    }
    void removeLast(int k){
        for(int i=0;i<k;i++) {
            lists.remove(queue.get(0));
            queue.remove(0);
        }
    }
    @Override
    public String toString() {
        return String.join("", lists);
    }
}