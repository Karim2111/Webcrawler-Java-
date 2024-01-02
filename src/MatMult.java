public class MatMult {
    public static double calculateEuclideanDistance(double[][] a2D, double[][] b2D) {
        double[] a = a2D[0];
        double[] b = b2D[0];
        if (a.length != b.length) {
            return Double.NaN; // Return NaN for invalid input
        }
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow((a[i] - b[i]), 2);
        }

        return Math.sqrt(sum);
    }

    public static double[][] multiplyMatrices(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            return null; // Return null for invalid input
        }

        double[][] resultMatrix = new double[a.length][b[0].length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                double total = 0;
                for (int k = 0; k < b.length; k++) {
                    total += a[i][k] * b[k][j];
                }
                resultMatrix[i][j] = total;
            }
        }

        return resultMatrix;
    }
}
