import java.util.*;

class WordFrequencyCounter {
    public static void main(String[] args) {

        String paragraph = "java is easy and java is interesting";

        String words[] = paragraph.split(" ");

        HashMap<String, Integer> map = new HashMap<>();

        for (String word : words) {
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        }

        List<String> list = new ArrayList<>(map.keySet());

        Collections.sort(list, new Comparator<String>() {
            public int compare(String a, String b) {
                return map.get(b) - map.get(a);
            }
        });

        System.out.println("Word Frequency:");

        for (String word : list) {
            System.out.println(word + " : " + map.get(word));
        }
    }
}
