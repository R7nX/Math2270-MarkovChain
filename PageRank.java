package math2270midterm;

import java.util.*;

public class PageRank {
    private static final double damping_factor = 0.85;
    private static final double tolerance = 1.0e-6;
    private static final int max_iteration = 100;
    private String page;
    private double rank;

    public PageRank(String page, double rank){
        this.page = page;
        this.rank = rank;
    }

    public static List<PageRank> computeWeightedRank(Map<String, Map<String, Double>> links) {

        Set<String> pagesSet = new HashSet<>();
        pagesSet.addAll(links.keySet());
        for (Map<String, Double> outlinks : links.values()){
            pagesSet.addAll(outlinks.keySet());
        }
        List<String> pages = new ArrayList<>(pagesSet);
        int N = pages.size();


        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < N; i++) {
            index.put(pages.get(i), i);
        }


        double[][] pageMatrix = new double[N][N];
        for (int j = 0; j < N; j++) {
            String page = pages.get(j);
            Map<String, Double> outlinks = links.get(page);
            if (outlinks == null || outlinks.isEmpty()) {
               
                for (int i = 0; i < N; i++) {
                    pageMatrix[i][j] = 1.0 / N;
                }
            } else {
               
                double totalWeight = 0.0;
                for (double weight : outlinks.values()) {
                    totalWeight += weight;
                }
                for (Map.Entry<String, Double> entry : outlinks.entrySet()) {
                    int i = index.get(entry.getKey());
                    pageMatrix[i][j] = entry.getValue() / totalWeight;
                }
            }
        }


        double[] qMatrix = new double[N];
        Arrays.fill(qMatrix, 1.0 / N);
        double[] newRank = new double[N];

        // Iteratively compute the PageRank until convergence.
        for (int iter = 0; iter < max_iteration; iter++ ) {
            for (int j = 0; j < N; j++) {
                newRank[j] = (1 - damping_factor) / N;
                for (int k = 0; k < N; k++) {
                    newRank[j] += damping_factor * pageMatrix[j][k] * qMatrix[k];
                }
            }
            double diff = 0.0;
            for (int j = 0; j < N; j++) {
                diff += Math.abs(newRank[j] - qMatrix[j]);
            }
            if (diff < tolerance) {
                break;
            }
            System.arraycopy(newRank, 0, qMatrix, 0, N);
        }

        
        List<PageRank> result = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            result.add(new PageRank(pages.get(i), qMatrix[i]));
        }
        result.sort((a, b) -> Double.compare(b.rank, a.rank));
        return result;
    }
    public static void main(String[] args) {
        Map<String, Map<String, Double>> weightedLinks = new HashMap<>();

        Map<String, Double> aLinks = new HashMap<>();
        aLinks.put("B", 0.7);
        aLinks.put("C", 0.3);
        weightedLinks.put("A", aLinks);

        Map<String, Double> bLinks = new HashMap<>();
        bLinks.put("C", 1.0);
        weightedLinks.put("B", bLinks);

        Map<String, Double> cLinks = new HashMap<>();
        cLinks.put("A", 0.5);
        cLinks.put("B", 0.5);
        weightedLinks.put("C", cLinks);

        System.out.println("\nWeighted graph PageRank:");
        List<PageRank> weightedRanks = PageRank.computeWeightedRank(weightedLinks);
        for (PageRank page: weightedRanks) {
            System.out.println(page);
        }
    }
    }

