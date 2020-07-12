package kickstart_2020.round_d;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class RecordBreaker {

    private final static ByteArrayOutputStream INTERCEPTED_STREAM = new ByteArrayOutputStream();
    private final static PrintStream SYSTEM_OUT = System.out;

    private static PrintWriter out = new PrintWriter(System.out);
    private static InputReader in = new InputReader(System.in);

    public static void main(String[] args) {

        // edit the path
        String testFile = "resources/TC_%s.txt";
        String testAns = "resources/TC_%sa.txt";

        // set 0 if version to submit
        // TODO: in this task, all TCs put in a single file but tests below doesn't support this format yet
        int tcCount = 0;
        for (int testCase = 0; testCase < tcCount; testCase++) {

            enableTestFromFile(format(testFile, testCase));
            solve();

            String[] ans = getResultsFromInterceptedStreamAsStringArray();
            Assert.assertEquals(ans[0], $.readFile(format(testAns, testCase))[0], testCase);

            out.flush();
        }

        // this will run on submission only
        if (tcCount == 0) {
            solve();
        }
    }

    /**
     * Contains all code for the solution, including console read/write.
     */
    private static void solve() {

        int t = in.nextInt();

        for (int tc = 1; tc <= t; tc++) {
            long sum = 0;

            int n = in.nextInt(); // days
            int[] visitors = in.readArray(n); // visitors on given day

            int max = -1;

            for (int i = 0; i < visitors.length - 1; i++) {
                if (visitors[i] > max && visitors[i] > visitors[i + 1]) {
                    sum++;
                }
                max = Math.max(max, visitors[i]);
            }
            if (visitors[n - 1] > max) sum++;

            String result = "Case #%s: %d";
            out.println(String.format(result, tc, sum));
        }

        out.flush();
        out.close();
    }



    /**
     * Create interception stream and replace System.out with it.
     *
     * Sent file content to the stream.
     * @param fileName path to the test file
     */
    private static void enableTestFromFile(String fileName) {
        System.setOut(new PrintStream(INTERCEPTED_STREAM));
        out = new PrintWriter(System.out);
        try {
            in = new InputReader(new File(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't read from the file", e);
        }
    }

    /**
     * Read and return results from the interception stream.
     *
     * Set System.out back to default Stream.
     * This is needed if we want to print something to the console between the tests.
     * @return arrays of longs with results
     */
    private static long[] getResultsFromInterceptedStreamAsLingArray() {
        out.flush();
        String[] stringArray = INTERCEPTED_STREAM.toString().split("\r\n");

        INTERCEPTED_STREAM.reset();
        System.setOut(SYSTEM_OUT);
        out = new PrintWriter(System.out);

        return $.arrayStringToLong(stringArray);
    }

    private static String[] getResultsFromInterceptedStreamAsStringArray() {
        out.flush();
        String[] stringArray = INTERCEPTED_STREAM.toString().split("\r\n");

        INTERCEPTED_STREAM.reset();
        System.setOut(SYSTEM_OUT);
        out = new PrintWriter(System.out);

        return stringArray;
    }

    /**
     * Fast reader.
     */
    private static class InputReader {
        BufferedReader reader;
        StringTokenizer tokenizer;

        /**
         * Read from the file.
         */
        InputReader(File file) throws FileNotFoundException {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), 32768);
            tokenizer = null;
        }

        InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        boolean hasNext() {
            try {
                return reader.ready();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        int[] readArray(int n) {
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = nextInt();
            }
            return array;
        }

        long[] readArrayLong(int n) {
            long[] array = new long[n];
            for (int i = 0; i < n; i++) {

                array[i] = nextLong();
            }
            return array;
        }

        long[][] readDoubleArrayLong(int n) {
            long[][] array = new long[n][2];
            for (int i = 0; i < n; i++) {

                array[i][0] = nextLong();
                array[i][1] = nextLong();
            }
            return array;
        }
    }

    /**
     * Util class with different util methods for testing.
     */
    private static class $ {

        static int[] ints(int... args) {
            return args;
        }

        static long[] longs(long... args) {
            return args;
        }

        static String[] strings(String... args) {
            return args;
        }

        static long[] arrayStringToLong(String[] stringArray) {
            long[] longArray = new long[stringArray.length];

            for (int i = 0; i < stringArray.length; i++) {
                longArray[i] = Long.parseLong(stringArray[i]);
            }
            return longArray;
        }

        static int[] arrayStringToInt(String[] stringArray) {
            int[] intArray = new int[stringArray.length];

            for (int i = 0; i < stringArray.length; i++) {
                intArray[i] = Integer.parseInt(stringArray[i]);
            }
            return intArray;
        }

        static long GCD(long a, long b) {
            return b == 0 ? a : GCD(b, a % b);
        }

        static boolean isPrime(long n) {
            if (n == 1) return false;

            long limit = (long) Math.sqrt(n);
            for (long i = 2; i <= limit; i++) {
                if (n % i == 0) return false;
            }
            return true;
        }

        static String[] readFile(String fileName) {
            List<String> list = new ArrayList<>();

            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                list = stream.collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list.toArray(new String[0]);
        }

        static long[] readFileAsLong(String fileName) {
            return arrayStringToLong(readFile(fileName));
        }

        static int[] readFileAsInt(String fileName) {
            return arrayStringToInt(readFile(fileName));
        }
    }

    /**
     * Assert method for testing.
     */
    private static class Assert {

        static void assertEquals(String actual, String expected, int index) {
            actual = actual.replace("\n", "");

            if (!actual.equals(expected)) {
                throw new RuntimeException("\n" + actual + " != \n"  + expected);
            }
            System.out.println("Test passed " + index);
        }

        static void assertEquals(int actual, int expected, int index) {
            if (actual != expected) {
                throw new RuntimeException("\n" + actual + " != "  + expected);
            }
            System.out.println("Test passed " + index);
        }

        static void assertEquals(long actual, long expected, int index) {
            if (actual != expected) {
                throw new RuntimeException("\n" + actual + " != "  + expected);
            }
            System.out.println("Test passed " + index);

        }

        static void assertEquals(int[] actual, int[] expected, int index) {
            if (!Arrays.equals(actual, expected)) {
                throw new RuntimeException("\n" + Arrays.toString(actual)
                        + "\n" + Arrays.toString(expected));
            }
            System.out.println("Test passed " + index);

        }

        static void assertEquals(long[] actual, long[] expected, int index) {
            if (!Arrays.equals(actual, expected)) {
                throw new RuntimeException("\n" + Arrays.toString(actual)
                        + "\n" + Arrays.toString(expected));
            }
            System.out.println("Test passed " + index);
        }
    }
}
