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
                System.out.println("Added to xSet: " + x);
            }
            if (!yList.contains(y)) { // yList: [3,2,1,4]
                yList.add(y);
                System.out.println("Added to ySet: " + y);
            }
        }

        // Sort and remove duplicates
        Collections.sort(xList);
        System.out.println("Sorted X: " + xList);
        Collections.sort(yList);
        System.out.println("Sorted Y: " + yList);


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
        System.out.println("xMap: " + xMap);
        System.out.println("yMap: " + yMap);
        List<int[]> compressedPoints = new ArrayList<>();
        for (int[] point : points) {
            // ex) (0,3)
            // xMap[0] = 0 ->
            // yMap[3] = 3
            // compressedPoints.add(0,3)
            int compressedX = xMap.get(point[0]);
            System.out.println("Compressed X: " + compressedX);
            int compressedY = yMap.get(point[1]);
            System.out.println("Compressed Y: " + compressedY);
            compressedPoints.add(new int[]{compressedX, compressedY});
        }

        // Print the contents of compressedPoints
        System.out.println("Compressed Points:");
        for (int[] point : compressedPoints) {
            System.out.println(Arrays.toString(point));
        }

        ///  create the matrix:
        // numX = X axis (# of unique x-points)
        // numY = Y axis (# of unique y-points)
        int numX = xList.size();
        int numY = yList.size();
        // initialize 2D  matrix with known dimensions
        int[][] matrix = new int[numX][numY];

        //populate Matrix with compressed points:
        for (int[] point : compressedPoints) {
            // retrieve x and y coordinates of each point
            int x = point[0];
            int y = point[1];
            // mark each point
            // ex)
            // (0,3)
            // x = 0 -> point[0]
            // y = 3 -> point[1]
            // matrix[0][3] = 1 (each point marked as 1)
            matrix[x][y] = 1;
        }
        for (int i = 0; i < numX; i++) {
            for (int j = 0; j < numY; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        //System.out.println(Arrays.toString(matrix[0]));

        ///  create the PrefixSum2D object
        PrefixSum2D prefixSum = new PrefixSum2D(matrix);

        System.out.println("PrefixSum: " + prefixSum);
        /// apply recursive algorithm that calculates the preSum for rectangle
         /// sumRegion -> determines #of points within the rectangle


        /// TODO: next steps
        ///  PreSum Matrix
        ///  can move left and right in our rectangle
        ///
        /// possible assumptions:
        ///  both left and right have L and R amount of points on either side
        ///  will have
        ///  process:
        ///  1) create matrix Matrix that stores all of the points in 2D array
        ///  2) will need to pick arbitrary 2 points (vertex in entire graph)
          ///  ex) (i,j) = 0
        ///  3) do rectangle process (determining going left or right)
        ///  rangeQuery to calculate number of points to determine direction

        
        //
         // left = x-coordinate
         // right =
        for (int left = 0; left < numX; left++) {
            for (int right = left + 1; right < numX; right++) {
                // storing the sum of points in each row between the columns left
              //  and right
                // ex)
                int[] rowSum = new int[numY];
                for (int row = 0; row < numY; row++) {
                    rowSum[row] = prefixSum.sumRegion(left, row, right, row);
                    System.out.print(rowSum[row] + " ");
                }
               // count += countValidRectangles(rowSum);
            }
        }

         

        long count = 0;
        /* your code here to calculate the count*/

        System.out.println(count);
    }
}

/// create PrefixSum2D
class PrefixSum2D {
    // preSum[i][j] records the sum of elements in the matrix [0, 0, i - 1, j - 1]
    private int[][] preSum;

    // constructor that takes 2D matrix as input
    public PrefixSum2D(int[][] matrix) {
        // 1) define dimensions for preSum array
        int m = matrix.length, n = matrix[0].length;
        // check matrix is valid (not necessary)
        if (m == 0 || n == 0) return;
        // initialize preSum array
        preSum = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // calculate the sum of elements for each matrix [0,0,i,j]
                preSum[i][j] = preSum[i - 1][j] + preSum[i][j - 1]
                        + matrix[i - 1][j - 1] - preSum[i - 1][j - 1];
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print("preSum[" + i + "][" + j + "] = " + preSum[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print(preSum[i][j] + " ");
            }
            System.out.println();
        }

    }

    // calculate the sum of elements in the submatrix [x1, y1, x2, y2]
    public int sumRegion(int x1, int y1, int x2, int y2) {
        return preSum[x2 + 1][y2 + 1] - preSum[x2 + 1][y1] + preSum[x1][y1] - preSum[x1][y2+1];
    }


}