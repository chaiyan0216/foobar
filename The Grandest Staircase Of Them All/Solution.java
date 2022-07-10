import java.util.HashMap;
import java.util.Map;

public class Solution {
    public static int solution(int n) {
        int cnt = 0;
        int sum = 0;
        while (sum <= n) {
            cnt++;
            sum += cnt;
        }
        int res = 0;
        for (int i = 2; i < cnt; i++) {
            res += count(n, 1, i, new HashMap<>());
        }
        return res;
    }

    private static int count(int n, int min, int num, Map<String, Integer> cache) {
        if (num == 1) {
            return n >= min ? 1 : 0;
        } else {
            String key = n + "-" + min + "-" + num;
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            int sum = 0;
            for (int i = 0; i < num; i++) {
                sum += i;
            }
            int cnt = 0;
            int max = (n - sum) / num;
            for (int i = min; i <= max; i++) {
                cnt += count(n - i, i + 1, num - 1, cache);
            }
            cache.put(key, cnt);
            return cnt;
        }
    }
}
