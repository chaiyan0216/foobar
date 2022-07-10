import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Solution {
    public static String solution(String s) {
        return sum(new BigInteger(s)).toString();
    }

    private static BigInteger sum(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }

        BigDecimal r = new BigDecimal("1.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727350138462309122970249248360558507372126441214971");
        BigDecimal s = new BigDecimal("3.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727350138462309122970249248360558507372126441214971");

        BigInteger brn = r.multiply(new BigDecimal(n)).toBigInteger();
        BigInteger brns = new BigDecimal(brn).divide(s, 100, RoundingMode.DOWN).toBigInteger();

        return brn.multiply(brn.add(BigInteger.ONE)).divide(BigInteger.valueOf(2))
                .subtract(brns.multiply(brns.add(BigInteger.ONE)))
                .subtract(sum(brns));
    }
}
