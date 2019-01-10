import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {

    public static void main(String[] args) throws IOException {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) throws IOException {
        // Vasiod kod ovde
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String r;
        ListAnagrams la = new ListAnagrams();
        while ((r = br.readLine()) != null) {
            la.add(r);
        }
        la.print();

    }
}

class ListAnagrams {
    TreeMap<String, List<String>> tree;

    ListAnagrams() {
        tree = new TreeMap<>();
    }

    void add(String string) {
        String key = getAnagramKey(string);
        if (key == null) {
            List<String> list = new LinkedList<>();
            list.add(string);
            tree.put(string, list);
        } else {
            List<String> list = tree.get(key);
            list.add(string);
        }
    }

    String getAnagramKey(String string) {
        return tree.keySet().stream().filter(a -> a.length() == string.length() && compare(a, string)).findFirst().orElse(null);
    }
    void print(){
        tree.keySet().forEach(a->{
            List<String> list = tree.get(a);
            String s = list.stream().collect(Collectors.joining(" "));
            System.out.println(s);
        });
    }
    private boolean compare(String s1, String s2) {
        char[] firstString = s1.toCharArray();
        char[] secondString = s2.toCharArray();

        Arrays.sort(firstString);
        Arrays.sort(secondString);

        if (Arrays.equals(firstString, secondString)) return true;
        return false;
    }
}