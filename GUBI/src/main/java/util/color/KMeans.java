package util.color;
import java.util.*;

public class KMeans {
    private int k; // Number of clusters
    private int[][] centroids; // Cluster centroids
    private Map<int[], Integer> pointToCluster; // 데이터 포인트 -> 클러스터 매핑
    private double threshold; // 거리 기준

    public KMeans(int k, double threshold) {
        this.k = k;
        this.threshold = threshold;
    }

    public void fit(List<int[]> data) {
        initializeCentroids(data);
        pointToCluster = new HashMap<>();

        boolean changed;
        do {
            List<List<int[]>> clusters = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                clusters.add(new ArrayList<>());
            }

            // Assign points to nearest centroids
            for (int[] point : data) {
                int closest = -1;
                double minDistance = Double.MAX_VALUE;

                for (int i = 0; i < k; i++) {
                    double distance = euclideanDistance(point, centroids[i]);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = i;
                    }
                }

                // 거리 기준을 만족하는 경우에만 클러스터에 포함
                if (minDistance <= threshold) {
                    clusters.get(closest).add(point);
                    pointToCluster.put(point, closest);
                } else {
                    pointToCluster.put(point, -1); // 클러스터에 포함되지 않음
                }
            }

            changed = false;

            // Update centroids
            for (int i = 0; i < k; i++) {
                if (clusters.get(i).isEmpty()) {
                    // 빈 클러스터를 랜덤 데이터 포인트로 초기화
                    centroids[i] = data.get(new Random().nextInt(data.size()));
                    changed = true;
                } else {
                    int[] newCentroid = computeCentroid(clusters.get(i));
                    if (!Arrays.equals(centroids[i], newCentroid)) {
                        centroids[i] = newCentroid;
                        changed = true;
                    }
                }
            }
        } while (changed);
    }

    public int[][] getCentroids() {
        return centroids;
    }

    public int getClusterForPoint(int[] point) {
        return pointToCluster.getOrDefault(point, -1); // -1: 클러스터에 포함되지 않음
    }

    private void initializeCentroids(List<int[]> data) {
        Random random = new Random();
        centroids = new int[k][3];
        centroids[0] = data.get(random.nextInt(data.size()));

        for (int i = 1; i < k; i++) {
            double[] distances = new double[data.size()];
            for (int j = 0; j < data.size(); j++) {
                double minDistance = Double.MAX_VALUE;
                for (int l = 0; l < i; l++) {
                    double distance = euclideanDistance(data.get(j), centroids[l]);
                    minDistance = Math.min(minDistance, distance);
                }
                distances[j] = minDistance;
            }

            double totalDistance = Arrays.stream(distances).sum();
            double randomValue = random.nextDouble() * totalDistance;

            double cumulativeDistance = 0;
            for (int j = 0; j < data.size(); j++) {
                cumulativeDistance += distances[j];
                if (cumulativeDistance >= randomValue) {
                    centroids[i] = data.get(j);
                    break;
                }
            }
        }
    }

    private double euclideanDistance(int[] a, int[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2) + Math.pow(a[2] - b[2], 2));
    }

    private int[] computeCentroid(List<int[]> cluster) {
        int[] centroid = new int[3];
        for (int[] point : cluster) {
            centroid[0] += point[0];
            centroid[1] += point[1];
            centroid[2] += point[2];
        }
        int size = cluster.size();
        if (size > 0) {
            centroid[0] /= size;
            centroid[1] /= size;
            centroid[2] /= size;
        }
        return centroid;
    }
}