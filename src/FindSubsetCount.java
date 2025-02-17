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
        for (int i = 0; i < numX; i++) {
            for (int j = 0; j < numY; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        ///  create the PrefixSum2D object
        PrefixSum2D prefixSum = new PrefixSum2D(matrix);
        System.out.println("PrefixSum: " + prefixSum);
        /// TODO: Create class that stores current dimensions of rectangle?
       // int x1, int y1, int x2, int y2;
        // Where to start our recursive search?
        for (int i = 0; i < rs.length; i++) {
            System.out.println("rs[i]: " + rs[i]);
        }
       // int solution = search(rs, prefixSum,0);
        //prefixSum.printPreSum();
        // System.out.println("PrefixSum: " + prefixSum);

        /// sumRegion -> determines #of points within the rectangle

        long count = 0;
        /* your code here to calculate the count*/


       // System.out.println(count);

        //
        // left = starting column of submatrix
        // right = represents the ending column of submatrix
        // ex)
        // rowSum[] -> for each pair of left and right calculate sum of points in
        // a specific row
        // if rowSum[row] = 0 // no point contained in submatrix
        // if rowSum[row] = 1 // point contained in submatrix
        for (int left = 0; left < numX; left++) {
            for (int right = left+1; right < numX; right++) {
                // storing the sum of points in each row between the columns left
                //  and right
                // ex)
                int[] rowSum = new int[numY];
               // System.out.println("left = " + left + ", right = " + right);
                for (int row = 0; row < numY; row++) {
                    System.out.println("prefixSum.sumRegion(" + left + ", " + row + ", " + right + ", " + row + ")");
                    int sumInRow = prefixSum.sumRegion(left, row, right, row);
                    rowSum[row] = sumInRow;
                    System.out.print("rowSum[" + row + "] = " + rowSum[row] + " ");
                }
                System.out.println();
                count += countValidRectangles(rowSum);
            }
        }
        System.out.println("Count: " +  count);
    }

    private static int search(int[] rs, PrefixSum2D prefixSum, int column, int x1, int y1, int x2, int y2) {
        if (column >= rs.length) { // no more columns to check
            return 0;
        }
        int count = 0;
        // go through every column
        // ex)
        //
        // Matrix:
        // 0 0 0 1 0
        // 0 1 0 0 0
        // 0 0 1 0 0
        // 1 0 0 0 0
        // 0 0 0 0 1
        //
        // Matrix:
        // 0 0 0 1 0
        // 0 2 0 0 0
        // 0 0 3 0 0
        // 4 0 0 0 0
        // 0 0 0 0 5
        //
         // 1 = [0][3] -> preSum[1][4]
        // 2 =  [1][1] -> preSum[2][2]
        // 3 =  [2][2] -> preSum[3][3]
        // 4 =  [3][0] -> preSum[4][1]
        // 5 =  [4][4] -> preSum[5][5]
        //
        // All Possible Subsets:
        // {1}, {2}, {3}, {4}, {5}, {1,2},{2,3},{3,4},{2,3,4},{
     // PreSum:
        //
         // 0 0 0 1 1
         // 0 1 1 2 2
        //  0 1 2 3 3
        //  1 2 3 4 4
        //  1 2 3 4 5

        // 0 0 0 x 1
        // 0 x 1 2 2
        // 0 1 x 3 3
        // x 2 3 4 4
        // 1 2 3 4 x

        // Algorithm:
        // 1) pick 1 [0][3]
        //   -> possible options: [2,3,4,5]
        //      -> pick 2:
        //            {1,2} -> can do
        //      -> pick 3:
        //      -> pick 4:
        //      -> pick 5:


       // go through each 'column'
        // in this column is 'row' (x and y inverted)
        // i = 1
        // (expand(x1,y1,x2,y2,1)
        //   -> pick
        // How to determine two points?
        // i = 2
        for (int i = column+1; i < rs.length; i++) {
            if (expand(x1,y1,x2,y2,i)) {
                // if rectangle can be 'expanded' (counts as possible subset?)
                count++;
            }

        }
        return count;

    }

    private static boolean expand(int x1, int y1, int x2, int y2, int i) {
        // method to expand rectangle
        // 1) Check that new point at column i is within bounds of rectangle
        /*
        // Check if the new point at column i is within the bounds of the rectangle
        if (i < y1 || i > y2) {
        return false;
        }

        // Check if the point at column i is present in the matrix
        // Assuming rs[i] gives the row index of the point in column i
        int newX = rs[i];
        if (newX < x1 || newX > x2) {
        return false;
        }

        // Update the rectangle's bounds to include the new point
        x1 = Math.min(x1, newX);
        x2 = Math.max(x2, newX);
        y1 = Math.min(y1, i);
        y2 = Math.max(y2, i);

         */

        return false;
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
       // return preSum[x2 + 1][y2 + 1] - preSum[x2 + 1][y1] + preSum[x1][y1] - preSum[x1][y2+1];
       // return preSum[x2+1][y2+1] - preSum[x1][y2+1] - preSum[x2+1][y1] + preSum[x1][y1];
        // debugging:
      //  System.out.println("x1: " + x1 + ", y1: " + y1 + ", x2: " + x2 + ", y2: " + y2);
       /*
        int a = preSum[x2 + 1][y2 + 1];
        int b = preSum[x1 + 1][y2 + 1];
        int c = preSum[x2 + 1][y1];
        int d = preSum[x1][y2];
        System.out.println("a = " + a + ", b = " + b + ", c = " + c + ", d = " + d);
        return a - b - c + d;
        */

        int a = preSum[x2+1][y2+1];
        System.out.println("a = " + a);
        int b = preSum[x2+1][y1];
        System.out.println("b = " + b);
        int c = preSum[x1][y1];
        System.out.println("c = " + c);
        int d = preSum[x1][y2+1];
        System.out.println("d = " + d);
        //System.out.println("a = " + a + ", b = " + b + ", c = " + c + ", d = " + d);
        return a - b + c - d;
        /*

        // Adjust the indices to account for 1-indexed preSum matrix
        int a = preSum[x2 + 1][y2 + 1];   // bottom-right corner
        int b = preSum[x1][y2 + 1];       // top-right corner (row above)
        int c = preSum[x2 + 1][y1];       // bottom-left corner (column before)
        int d = preSum[x1][y1];           // top-left corner (to add back, as it's subtracted twice)
        int sum = a - b - c + d;
        System.out.println("Sum: " + sum);
        // Debugging output for checking each term
        System.out.println("a = " + a + ", b = " + b + ", c = " + c + ", d = " + d);

        // Calculate the sum for the submatrix by applying the inclusion-exclusion principle
        return sum;

         */
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