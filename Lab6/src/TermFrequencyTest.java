import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws IOException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}


class TermFrequency {
    TreeSet<String> stopWords;
    TreeMap<String, Integer> words;

    TermFrequency(InputStream inputStream, String[] stopWords) throws IOException {
        this.stopWords = new TreeSet<>();
        this.stopWords.addAll(Arrays.asList(stopWords));
        this.words = new TreeMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = br.readLine()) != null) {
            validateLine(line);
        }
    }

    void validateLine(String line) {
        line = line.toLowerCase();
        String[] words = line.split(" ");
        for(String s : words){
            s = s.replaceAll("[.,]$","");
            if(s.length() > 0){
              if(!stopWords.contains(s)){
                  addWord(s);
                  //System.out.print(s+"     ");
              }
            }
        }

    }

    void addWord(String word) {
        if(words.containsKey(word)){
            words.put(word, words.get(word)+1);
        }else{
            words.put(word, 1);
        }
    }

    public int countTotal() {
        return words.values().stream().reduce((a,acc) -> acc+=a).get();
    }

    public int countDistinct() {
        return words.size();
    }

    public List<String> mostOften(int k) {
        LinkedList<String> ll = new LinkedList<>(words.keySet());

        ll.sort((o1, o2) -> {
            if(words.get(o1) < words.get(o2)){
                return 1;
            }else if(words.get(o1) == words.get(o2)){
                return o1.compareTo(o2);
            }
            return -1;
        });

        return ll.stream().limit(k).collect(Collectors.toList());
    }
}