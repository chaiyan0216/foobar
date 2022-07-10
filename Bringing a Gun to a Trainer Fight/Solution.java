import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static int solution(int[] dimensions, int[] your_position, int[] trainer_position, int distance) {
        int cnt = 0;
        Set<String> set = new HashSet<>();
        int[][] points = getPoints(dimensions, your_position, trainer_position, distance);
        for (int[] point : points) {
            if (dist2(point, your_position) <= Math.pow(distance, 2)) {
                int dx = point[0] - your_position[0];
                int dy = point[1] - your_position[1];
                int common = gcd(Math.abs(dx), Math.abs(dy));
                if (common == 0) {
                    common = 1;
                }
                String s = dx / common + "/" + dy / common;
                if (!set.contains(s)) {
                    set.add(s);
                    if (point[2] == 1) {
                        cnt++;
                    }
                }
            } else {
                break;
            }
        }
        return cnt;
    }

    private static int[][] getPoints(int[] dimensions, int[] your_position, int[] trainer_position, int distance) {
        int cntX = (distance + your_position[0]) / dimensions[0] + 1;
        int cntY = (distance + your_position[1]) / dimensions[1] + 1;
        int[][] points = new int[cntX * cntY * 8][3];
        int cnt = 0;
        for (int i = 0; i < cntX; i++) {
            int baseX = i * dimensions[0];
            boolean flagX = i % 2 == 0;
            int yourX = baseX + (flagX ? your_position[0] : dimensions[0] - your_position[0]);
            int trainerX = baseX + (flagX ? trainer_position[0] : dimensions[0] - trainer_position[0]);
            for (int j = 0; j < cntY; j++) {
                int baseY = j * dimensions[1];
                boolean flagY = j % 2 == 0;
                int yourY = baseY + (flagY ? your_position[1] : dimensions[1] - your_position[1]);
                int trainerY = baseY + (flagY ? trainer_position[1] : dimensions[1] - trainer_position[1]);
                points[cnt++] = new int[] {yourX, yourY, 0};
                points[cnt++] = new int[] {-1 * yourX, yourY, 0};
                points[cnt++] = new int[] {yourX, -1 * yourY, 0};
                points[cnt++] = new int[] {-1 * yourX, -1 * yourY, 0};
                points[cnt++] = new int[] {trainerX, trainerY, 1};
                points[cnt++] = new int[] {-1 * trainerX, trainerY, 1};
                points[cnt++] = new int[] {trainerX, -1 * trainerY, 1};
                points[cnt++] = new int[] {-1 * trainerX, -1 * trainerY, 1};
            }
        }
        Arrays.sort(points, Comparator.comparingDouble(p -> dist2(p, your_position)));
        return points;
    }

    private static double dist2(int[] p1, int[] p2) {
        return Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2);
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a%b);
    }
}