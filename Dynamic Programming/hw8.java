import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.nanoTime;

public class hw8 {
    private static int[][] OPT;
    private static String pair;

    public static List<String> readFile(Scanner input) throws FileNotFoundException {
        List<String> RNA = new ArrayList<>();
        while (input.hasNextLine()) {
            RNA.add(input.nextLine());
        }
        return RNA;
    }

    public static void nussinov(String sequence) {
        int length = sequence.length();
        OPT = new int[length][length + 1];
        for (int i = 0; i < length; i++) {
            OPT[i][0] = 0;
        }
        for (int j = 1; j <= length; j++) {
            for (int i = length; i >= 1; i--) {
                if (i >= j - 4) {
                    OPT[i -1][j] = 0;
                } else {
                    int max = 0;
                    for (int t = i; t < j - 4; t++) {
                        if (match(sequence, t - 1, j - 1)) {
                            if (OPT[i - 1][t - 1] + OPT[t][j - 1] > max) {
                                max = OPT[i - 1][t - 1] + OPT[t][j - 1];
                            }
                            OPT[i - 1][j] = Math.max(max + 1, OPT[i - 1][j - 1]);
                        } else {
                            OPT[i - 1][j] = Math.max(OPT[i - 1][j], OPT[i - 1][j - 1]);
                        }
                    }
                }
//                showTable(sequence);
            }
        }
    }

    public static boolean match(String sequence, int a, int b) {
        if (sequence.charAt(a) == 'A') {
            return sequence.charAt(b) == 'U';
        } else if (sequence.charAt(a) == 'U') {
            return sequence.charAt(b) == 'A';
        } else if (sequence.charAt(a) == 'G') {
            return sequence.charAt(b) == 'C';
        } else {
            return sequence.charAt(b) == 'G';
        }
    }

    public static void showTable(String sequence) {
        int length = sequence.length();
        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length + 1; j++) {
                System.out.print(OPT[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void traceback(String sequence, int start, int end) {   // 1 <= start <= end <= sequence.length()
        if (end < start) {
            return;
        }
        if (OPT[start - 1][end] == OPT[start - 1][end - 1]) {   //when traceback(1, 16), actually searching OPT[0][16]
            traceback(sequence, start, end - 1);
        } else {
            int index = 0;
            for (int t = start; t <= end - 4; t++) {
                if (match(sequence, t - 1, end - 1) &&
                        OPT[start - 1][end] == OPT[start - 1][t - 1] + OPT[t][end - 1] + 1) {
                    index = t;
                    break;
                }
            }
            pair = pair.substring(0, index - 1) + "(" + pair.substring(index, end - 1) + ")" + pair.substring(end);
            traceback(sequence, start, index - 1);
            traceback(sequence, index + 1, end - 1);
        }
    }

    public static void output(List<String> RNA) {
        for (int i = 0; i < RNA.size(); i++) {
            String sequence = RNA.get(i);
            long startTime = nanoTime();
            nussinov(sequence);
            //long endTime = currentTimeMillis();
            long endTime = nanoTime();
            double time = (endTime - startTime) * 1.0 / 10000.0;
            pair = "";
            for (int j = 0; j < OPT.length; j++) {
                pair += ".";
            }
            traceback(sequence, 1, sequence.length());
            System.out.println(sequence);
            System.out.println(pair);
            if (sequence.length() <= 15) {
                showTable(sequence);
            }
            System.out.println("Summary: " + OPT[0][sequence.length()] + ", " + sequence.length() + ", " + time);
            System.out.println();
        }
    }

    public static void generate() throws FileNotFoundException {
        String[] RNA = new String[4];
        PrintStream output = new PrintStream(new File("test1.txt"));
        RNA[0] = "A";
        RNA[1] = "C";
        RNA[2] = "G";
        RNA[3] = "U";
        for (int i = 4; i <= 12; i++) {
            for (int j = 0; j < Math.pow(2, i); j++) {
                String item = RNA[(int) (4 * Math.random())];
                System.out.print(item);
                output.print(item);
            }
            System.out.println();
            output.println();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
//        String sequence = "AGCUCAUAUGGC";
//        String sequence = "GCUCCAGUGGCCUAAUGGAUAUGGCUUUGGACUUCUAAUCCAAAGGUUGCGGGUUCGAGUCCCGUCUGGAGUA";
//        String file = "test1.txt";
//        Scanner input = new Scanner(new File(file));
        Scanner input = new Scanner(System.in);
//        String sequence = "CUCCGGUUGCAAUGUC";
        List<String> RNA = readFile(input);
//        long startTime = currentTimeMillis();
        output(RNA);
    }
}
