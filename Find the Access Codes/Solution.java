import java.util.LinkedList;
import java.util.List;

public class Solution {
    public static int solution(int[] l) {
        List<List<Integer>> list = new LinkedList<>();
        for (int i = 0; i < l.length; i++) {
            list.add(new LinkedList<>());
            for (int j = i + 1; j < l.length; j++) {
                if (l[j] % l[i] == 0) {
                    list.get(i).add(j);
                }
            }
        }

        int cnt = 0;
        for (List<Integer> li : list) {
            for (int i : li) {
                cnt += list.get(i).size();
            }
        }

        return cnt;
    }
}
