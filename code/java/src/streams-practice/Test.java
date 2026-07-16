
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args){
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("banana");
        words.add("cherry");
        words.add("date");


        List<String> result = words.stream()
                .filter(word -> word.length() < 5)
                .map(String:: toUpperCase)
                .collect(Collectors.toList());

        System.out.println(result);

        Map<Integer,List<String>> nested = words.stream()
                .collect(Collectors.groupingBy(String::length));

        System.out.println(nested);

        int total = words.stream()
                .reduce(0, (a,b) -> a + b.length(), Integer::sum);

        int total2 = words.stream()
                .mapToInt(String:: length)
                        .sum();

        System.out.println(total);
        System.out.println(total2);


        Map<Integer, Long> countMap = words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));

    }
}
