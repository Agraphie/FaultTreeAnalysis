package Printer;

import net.sf.javabdd.BDD;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Agraphie on 26.06.2015.
 */
public class Printer {

    public void printBDDToDOT(BDD bdd, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write("digraph G {");
            writer.newLine();
            writer.write("0 [shape=box, label=\"0\", style=filled, shape=box, height=0.3, width=0.3];");
            writer.newLine();
            writer.write("1 [shape=box, label=\"1\", style=filled, shape=box, height=0.3, width=0.3];");
            writer.newLine();

            boolean[] visited = new boolean[bdd.nodeCount() + 2];
            visited[0] = true;
            visited[1] = true;
            Map<BDD, Integer> map = new HashMap<BDD, Integer>();
            map.put(bdd.getFactory().zero(), 0);
            map.put(bdd.getFactory().one(), 1);
            // Recursion.
            this.printRec(bdd, writer, 1, visited, map);

            writer.write("}");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int printRec(BDD bdd, BufferedWriter writer, int current, boolean[] visited, Map<BDD, Integer> map) throws IOException {
        Integer ri = map.get(bdd);
        if (ri == null) {
            map.put(bdd.id(), ri = ++current);
        }

        int r = ri;
        if (visited[r]) {
            return current;
        } else {
            visited[r] = true;
            writer.write(ri + " [label=\"" + bdd.var() + "\"];");
            writer.newLine();
            BDD l = bdd.low();
            BDD h = bdd.high();
            Integer li = map.get(l);
            if (li == null) {
                map.put(l.id(), li = ++current);
            }

            Integer hi = map.get(h);
            if (hi == null) {
                map.put(h.id(), hi = ++current);
            }

            writer.write(ri + " -> " + li + " [style=dotted];");
            writer.write(ri + " -> " + hi + " [style=filled];");

            current = printRec(l, writer, current, visited, map);
            current = printRec(h, writer, current, visited, map);

            return current;
        }
    }
}
