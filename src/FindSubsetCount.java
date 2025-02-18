import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 Test your code with the provided test cases in \tests
 The command should look like:
    java [your_java_file] [path_to_test_file]
 For example:
 in windows PS:
    \> java .\FindSubsetCount.java .\tests\test1
 in Linux Bash or Windows CMD:
    $ java FindSubsetCount.java tests/test1
 You can compare your output with the key in tests/key* files
*/
public class FindSubsetCount {
    private static long countValidRectangles(int[] rowSum) {
        long count = 0;
        int consecutiveValidRows = 0;

        // Traverse the rowSum array to count the consecutive valid rows
        for (int i = 0; i < rowSum.length; i++) {
            if (rowSum[i] == 1) {
                // Increment the count of consecutive valid rows
                consecutiveValidRows++;
            } else {
                // If we encounter a non-valid row, calculate the rectangles
                if (consecutiveValidRows > 0) {
                    // The number of rectangles that can be formed from `consecutiveValidRows` valid rows
                    count += (long) consecutiveValidRows * (consecutiveValidRows + 1) / 2;
                    consecutiveValidRows = 0;
                }
            }
        }

        // If the last sequence of valid rows ends at the end of the array, we need to count them as well
        if (consecutiveValidRows > 0) {
            count += (long) consecutiveValidRows * (consecutiveValidRows + 1) / 2;
        }

        return count;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(args[0]));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int n = Integer.parseInt(st.nextToken());

        // array to store original (x,y) points
        List<int[]> points = new ArrayList<int[]>();
        // arrayList to store x and y values
        List<Integer> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();


        for (int j = 0; j < n; j++) {
            st = new StringTokenizer(f.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            points.add(new int[]{x, y}); // [(0,3), (2,2), (1,1), (3,0), (4,4)]
            // check for duplicates
            if (!xList.contains(x)) { // xList: [0,2,1,3,4]
                xList.add(x);
                // System.out.println("Added to xSet: " + x);
            }
            if (!yList.contains(y)) { // yList: [3,2,1,4]
                yList.add(y);
                // System.out.println("Added to ySet: " + y);
            }
        }

        // Sort and remove duplicates
        Collections.sort(xList);
        // System.out.println("Sorted X: " + xList);
        Collections.sort(yList);
        //  System.out.println("Sorted Y: " + yList);


        // Coordinate Compression:
        // xMap: {0:0, 1:1, 2 :2, 3:3, 4:4}
        // yMap: {0: 0, 1:1, 2:2, 3:3, 4:4}
        // {0,:0} -> (x-point, rank)
        // rank = position of x and y coordinate
        // ex) rank 0 = point 0

        // use HashMap to determine
        Map<Integer, Integer> xMap = new HashMap<>();
        Map<Integer, Integer> yMap = new HashMap<>();
        int rank = 0;
        for (int x : xList) {
            // populate map with sorted x-points from xList
            xMap.put(x, rank++);
        }
        rank = 0;
        for (int y : yList) {
            // populate map with sorted y-points from yList
            yMap.put(y, rank++);
        }
        //
        // System.out.println("xMap: " + xMap);
        // System.out.println("yMap: " + yMap);
        List<int[]> compressedPoints = new ArrayList<>();
        for (int[] point : points) {
            // ex) (0,3)
            // xMap[0] = 0 ->
            // yMap[3] = 3
            // compressedPoints.add(0,3)
            int compressedX = xMap.get(point[0]);
            //System.out.println("Compressed X: " + compressedX);
            int compressedY = yMap.get(point[1]);
            // System.out.println("Compressed Y: " + compressedY);
            compressedPoints.add(new int[]{compressedX, compressedY});
        }

        // Print the contents of compressedPoints
        // System.out.println("Compressed Points:");
        for (int[] point : compressedPoints) {
            // System.out.println(Arrays.toString(point));
        }

        ///  create the matrix:
        // numX = X axis (# of unique x-points)
        // numY = Y axis (# of unique y-points)
        int numX = xList.size();
        int numY = yList.size();
        // initialize 2D  matrix with known dimensions
        int[][] matrix = new int[numX][numY];
        // for every column, tells us row of where point is located
        // numY -> Columns
        int[] rs = new int[numY];
        //populate Matrix with compressed points:
        for (int[] point : compressedPoints) {
            // retrieve x and y coordinates of each point
            int x = point[0];
            int y = point[1];
            // rs[3] = 0
            // rs[2] = 2
            // rs[1] = 1
            // rs[0] = 3
            // rs[4] = 4
            // rs[3,1,2,0,4]
            // rs[0] = 3
            // rs[1]
            // rs[2]
            // rs[3]
            // rs[4]
            // rs = #of columns in the set
            rs[y] = x;
            // mark each point
            // ex)
            // (0,3)
            // x = 0 -> point[0]
            // y = 3 -> point[1]
            // matrix[0][3] = 1 (each point marked as 1)
            matrix[x][y] = 1;
        }
        /*
        System.out.println("Printing Matrix below: ");
        for (int i = 0; i < numX; i++) {
            for (int j = 0; j < numY; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        */

        PrefixSum2D prefixSum = new PrefixSum2D(matrix);
        /*
        System.out.println("Printing row location of each point in the given column in the matrix: ");
        for (int i = 0; i < rs.length; i++) {
            System.out.println("rs[" + i + "]: " + rs[i]);
        }
        */

        long solution = 1;
        for (int y1 = 0; y1 < rs.length; y1++) {
            solution += search(rs, prefixSum, rs[y1], y1, rs[y1], y1);
        }
        System.out.println("Count: " + solution);
    }

    private static long search(int[] rs, PrefixSum2D prefixSum, int x1, int y1, int x2, int y2) {
        long count = 1;
        for (int y3 = y2 + 1; y3 < rs.length; y3++) {
            int x1_ = Math.min(x1, rs[y3]);
            int x3 = Math.max(x2, rs[y3]);
            long small = prefixSum.sumRegion(x1, y1, x2, y2);
            long large = prefixSum.sumRegion(x1_, y1, x3, y3);
            if (large == small + 1) {
                count += search(rs, prefixSum, x1_, y1, x3, y3);
            }
        }
        return count;
    }
}

/// create PrefixSum2D
class PrefixSum2D {
    // preSum[i][j] records the sum of elements in the matrix [0, 0, i - 1, j - 1]
    private long[][] preSum;

    // constructor that takes 2D matrix as input
    public PrefixSum2D(int[][] matrix) {
        // 1) define dimensions for preSum array
        int m = matrix.length, n = matrix[0].length;
        // check matrix is valid (not necessary)
        if (n == 0) return;
        // initialize preSum array
        preSum = new long[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // calculate the sum of elements for each matrix [0,0,i,j]
                preSum[i][j] = preSum[i - 1][j] + preSum[i][j - 1]
                        + matrix[i - 1][j - 1] - preSum[i - 1][j - 1];

            }
        }
        /*
        System.out.println("Printing preSum matrix below: ");
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print(preSum[i][j] + " ");
            }
            System.out.println();
        }

         */
    }

    // calculate the sum of elements in the submatrix [x1, y1, x2, y2]
    public long sumRegion(int x1, int y1, int x2, int y2) {
        long a = preSum[x2 + 1][y2 + 1];
        long b = preSum[x2 + 1][y1];
        long c = preSum[x1][y1];
        long d = preSum[x1][y2 + 1];
        return a - b + c - d;
    }

    public void printPreSum() {
        for (int i = 1; i <= preSum.length; i++) {
            for (int j = 1; j <= preSum[0].length; j++) {
                System.out.print(preSum[i][j] + " ");
            }
            System.out.println();
        }
    }


}