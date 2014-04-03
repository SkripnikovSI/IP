
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;


public class Test {
    public static void main(String[] args) throws IOException {
        TreeSet<Long> set = new TreeSet<Long>() ;
        set.add((long)100);
        set.add((long)25);
        set.add((long)1);
        set.add((long)75);
        System.out.println(set.first());
        System.out.println(set.last());
        System.out.println(set.remove(new Long(1)));
        System.out.println(set.first());
    }
}
