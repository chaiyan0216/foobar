import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Solution {

    public static int[] solution(int[][] m) {
        int n = m.length;

        // Corner case
        if (n == 1) {
            return new int[]{1, 1};
        }

        // Get QR matrix
        Fraction[][] QR = getQR(m);

        // Get (I - Q)^-1
        updateQR(QR);

        // Multi FR
        Fraction[] raw = new Fraction[n - QR.length + 1];
        for (int i = 0; i < raw.length - 1; i++) {
            raw[i] = new Fraction(0);
            for (int j = 0; j < QR.length; j++) {
                raw[i] = raw[i].plus(QR[0][j].multiply(QR[j][i + QR.length]));
            }
        }

        // Update to integer
        int scale = 1;
        for (int i = 0; i < raw.length - 1; i++) {
            scale = scale * raw[i].denominator / gcd(scale, raw[i].denominator);
        }

        int[] res = new int[raw.length];
        for (int i = 0; i < res.length - 1; i++) {
            res[i] = raw[i].numerator * scale / raw[i].denominator;
            res[res.length - 1] += res[i];
        }

        scale = 0;
        for (int i = 0; i < res.length - 1; i++) {
            scale = gcd(scale, res[i]);
        }
        for (int i = 0; i < res.length; i++) {
            res[i] /= scale;
        }

        return res;
    }

    private static Fraction[][] getQR(int[][] m) {
        int n = m.length;
        List<Integer> nonAbsorbList = new LinkedList<>();
        List<Integer> absorbList = new LinkedList<>();
        List<Integer> sumList = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            int sum = Arrays.stream(m[i]).sum();
            if (sum != 0) {
                nonAbsorbList.add(i);
                sumList.add(sum);
            } else {
                absorbList.add(i);
            }
        }

        Fraction[][] QR = new Fraction[nonAbsorbList.size()][n];
        int i = 0;
        for (int row : nonAbsorbList) {
            int j = 0;
            for (int col : nonAbsorbList) {
                QR[i][j++] = new Fraction(m[row][col], sumList.get(i));
            }
            for (int col : absorbList) {
                QR[i][j++] = new Fraction(m[row][col], sumList.get(i));
            }
            i++;
        }

        return QR;
    }

    private static void updateQR(Fraction[][] QR) {
        int n = QR.length;
        Fraction[][] IQ = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    IQ[i][j] = new Fraction(1).minus(QR[i][j]);
                } else {
                    IQ[i][j] = QR[i][j].neg();
                }
            }
        }

        Fraction[][] reverseIQ = reverse(IQ);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                QR[i][j] = reverseIQ[i][j];
            }
        }
    }

    private static Fraction[][] reverse(Fraction[][] IQ) {
        int n = IQ.length;
        Fraction[][] rm = new Fraction[n][n];
        Fraction value = getValue(IQ);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.pow(-1, i + j) < 0) {
                    rm[j][i] =  getValue(getRest(IQ, i, j)).neg().divided(value);
                } else {
                    rm[j][i] =  getValue(getRest(IQ, i, j)).divided(value);
                }
            }
        }
        return rm;
    }

    private static Fraction getValue(Fraction[][] m) {
        int n = m.length;
        if (n == 1) {
            return new Fraction(m[0][0].numerator, m[0][0].denominator);
        } else if (n == 2) {
            return (m[0][0].multiply(m[1][1])).minus(m[0][1].multiply(m[1][0]));
        } else {
            Fraction value = new Fraction(0);
            for (int i = 0; i < n; i++) {
                Fraction sub = m[0][i].multiply(getValue(getRest(m, 0, i)));
                if (Math.pow(-1, i) < 0) {
                    value = value.plus(sub.neg());
                } else {
                    value = value.plus(sub);
                }
            }
            return value;
        }
    }

    private static Fraction[][] getRest(Fraction[][] m, int row, int col) {
        int n = m.length;
        Fraction[][] sm = new Fraction[n - 1][n - 1];
        int smi = 0, smj = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (j == col) {
                    continue;
                }
                sm[smi][smj++] = new Fraction(m[i][j].numerator, m[i][j].denominator);
            }
            smi++;
            smj = 0;
        }
        return sm;
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static class Fraction {
        int numerator;
        int denominator = 1;

        public Fraction(int num) {
            numerator = num;
            simplify();
        }
        public Fraction(int num, int den) {
            if (den < 0) {
                num *= -1;
                den *= -1;
            }
            numerator = num;
            denominator = den;
            simplify();
        }

        private void simplify() {
            int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
            numerator /= gcd;
            denominator /= gcd;
        }

        public Fraction neg() {
            numerator *= -1;
            return new Fraction(numerator, denominator);
        }

        public Fraction plus(Fraction that) {
            int num = this.numerator * that.denominator + this.denominator * that.numerator;
            int den = this.denominator * that.denominator;
            return new Fraction(num, den);
        }

        public Fraction minus(Fraction that) {
            return plus(that.neg());
        }

        public Fraction multiply(Fraction that) {
            return new Fraction(this.numerator * that.numerator, this.denominator * that.denominator);
        }

        public Fraction divided(Fraction that) {
            return new Fraction(this.numerator * that.denominator, this.denominator * that.numerator);
        }
    }
}
