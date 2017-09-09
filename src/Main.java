import ru.spbau.alferov.javahw.hashtable.*;

public class Main {

    public static void main(String[] args) {
        /*
	    List l = new List();

	    for (int i = 1; i <= 17; i++) {
	        l.insert(i);
        }

        for (List.Iterator it = l.listHead(); !it.isEnd(); it.advance()) {
            if ((Integer)it.get() == 7) {
                l.erase(it);
                break;
            }
        }

        l.erase(l.listHead());

        System.out.print(l.getSize());
        System.out.print(": ");
	    for (List.Iterator it = l.listHead(); !it.isEnd(); it.advance()) {
	        Integer cur = (Integer)it.get();
            System.out.print(cur);
            System.out.print(" ");
        }
        System.out.println();
        */

        Hashtable h = new Hashtable();

        h.put("a", "b");
        System.out.println(h.get("a"));
        System.out.println(h.put("a", "c"));
        System.out.println(h.get("a"));
        h.clear();
    }
}
