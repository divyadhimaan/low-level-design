package practice;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Test3 {
    public static void main(String[] args){
        int [][] arrays = {{0,1}, {2,0}, {3,1}, {3,2}};


        List<Integer> distinct = Arrays.stream(arrays)
                .flatMapToInt(arr -> Arrays.stream(arr))
                .distinct()
                .boxed()
                .filter(n -> n%2==0)
                        .reduce(0, (a,b) -> a + b);
//                .collect(Collectors.toList());


//        List<Integer> arr = Arrays.asList(1,2,3,4);
//
//        int sum = arr.stream()
//                .reduce(2, (a, b) -> a + b);
//
//        System.out.println(sum);




        System.out.println(distinct);
    }

}
