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
        List <int[]> compressedPoints = new ArrayList<>();
        for (int[] point: points) {
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


        long count = 0;
        /* your code here to calculate the count*/

        System.out.println(count);
    }
}