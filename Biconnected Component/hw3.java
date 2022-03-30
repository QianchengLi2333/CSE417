import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

public class hw3 {
    private static int num = 0;
    private static int numEdge = 0;
    private static int dfsCounter = 0;

    private static Map<Integer, List<Integer>> edges;
    private static Stack<Edge> stack;
    private static List<Integer> articulation;

    public static int readFile(String file) throws FileNotFoundException {
        Scanner input = new Scanner(new File(file));
        int numV = input.nextInt();
        edges = new HashMap<Integer, List<Integer>>();
        while (input.hasNextInt()) {
            int u = input.nextInt();
            int v = input.nextInt();
            numEdge++;
            if (!edges.containsKey(u)) {
                edges.put(u, new ArrayList<>());
            }
            if (!edges.containsKey(v)) {
                edges.put(v, new ArrayList<>());
            }
            edges.get(u).add(v);
            edges.get(v).add(u);
        }
        return numV;
    }

    public static void BiC(int numV, PrintStream output) {
        int[] DFS = new int[numV];
        int[] low = new int[numV];
        int[] parent = new int[numV];
        stack = new Stack<>();
        articulation = new ArrayList<>();
        for (int i = 0; i < DFS.length; i++) {
            DFS[i] = -1;
        }
        for (int i = 0; i < numV; i++) {
            if (DFS[i] == -1) {
                BiC(i, DFS, low, stack, parent, articulation, output);
            }
        }
    }

    private static void BiC(int u, int[] DFS, int[] low, Stack<Edge> stack, int[] parent,
                           List<Integer> articulation, PrintStream output) {
        DFS[u] = dfsCounter++;
        low[u] = DFS[u];
        int child = 0;
        List<Integer> incidentEdges = edges.get(u);
        for (int i = 0; i < incidentEdges.size(); i++) {
            int v = incidentEdges.get(i);
            if (DFS[v] == -1) {
                child++;
                parent[v] = u;
                stack.push(new Edge(u, v));
                BiC(v, DFS, low, stack, parent, articulation, output);
                if (low[u] > low[v]) {
                    low[u] = low[v];
                }
                Edge e = new Edge(u, v);
                if (DFS[u] == 1 && child > 1) {
                    num++;
                    System.out.print("Component " + num + ": {");
                    output.print("Component " + num + ": {");
                    while (!stack.peek().equals(e)) {
                        System.out.print("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}, ");
                        output.print("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}, ");
                        stack.pop();
                    }
                    System.out.println("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}}");
                    output.println("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}}");
                    stack.pop();
                    if (!articulation.contains(u)) {
                        articulation.add(u);
                    }
                } else if (DFS[u] > 1 && low[v] >= DFS[u]) {
                    num++;
                    System.out.print("Component " + num + ": {");
                    output.print("Component " + num + ": {");
                    while (!stack.peek().equals(e)) {
                        System.out.print("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}, ");
                        output.print("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}, ");
                        stack.pop();
                    }
                    System.out.println("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}}");
                    output.println("{" + stack.peek().getU() + ", " + stack.peek().getV() + "}}");
                    stack.pop();
                    if (!articulation.contains(u)) {
                        articulation.add(u);
                    }
                }
            } else if (DFS[v] < DFS[u] - 1 && parent[u] != v) {
                if (low[u] > DFS[v]) {
                    low[u] = DFS[v];
                }
                stack.push(new Edge(u, v));
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String file = args[0];
        int numV = readFile(file);
        PrintStream output = new PrintStream(new File("hw3out.txt"));
        long startTime = currentTimeMillis();
//        long startTime = nanoTime();
        BiC(numV, output);
        long endTime = currentTimeMillis();
//        long endTime = nanoTime();
        double time = (endTime - startTime) * 1.0 ;
        if (stack.size() > 0) {
            num++;
            System.out.print("Component " + num + ": {");
            output.print("Component " + num + ": {");
            System.out.print("{" + stack.peek().getU() + ", " + stack.peek().getV());
            output.print("{" + stack.peek().getU() + ", " + stack.peek().getV());
            stack.pop();
            while (!stack.isEmpty()) {
                System.out.print("}, {" + stack.peek().getU() + ", " + stack.peek().getV());
                output.print("}, {" + stack.peek().getU() + ", " + stack.peek().getV());
                stack.pop();
            }
            System.out.println("}}");
            output.println("}}");
        }
        if (!articulation.isEmpty()) {
            System.out.print("Articulations: {" + articulation.get(0));
            output.print("Articulations: {" + articulation.get(0));
            for (int i = 1; i < articulation.size(); i++) {
                System.out.print(", " + articulation.get(i));
                output.print(", " + articulation.get(i));
            }
            System.out.println("}");
            output.println("}");
        }
        if (numV > 2) {
            System.out.println("Summary: " + file + ", " + numV + ", " + numEdge
                    + ", " + articulation.size() + ", " + num + ", " + time);
            output.println("Summary: " + file + ", " + numV + ", " + numEdge
                    + ", " + articulation.size() + ", " + num + ", " + time);
        }
    }

    public static class Edge {
        private final int u;
        private final int v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }

        public int getU() {
            return u;
        }

        public int getV() {
            return v;
        }

        public boolean equals(Edge e) {
            if (u == e.getU() && v == e.getV()) {
                return true;
            } else if (u == e.getV() && v == e.getU()) {
                return true;
            }
            return false;
        }
    }
}
