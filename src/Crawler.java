import java.io.*;
import java.util.*;

public class Crawler {
    private HashMap<String, Webpage> urlMap;
    private HashSet<String> visitedUrl;
    private Queue<String> queue;
    private HashMap<String, Double> idf;

    public Crawler() {
        this.queue = new LinkedList<>();
        this.visitedUrl = new HashSet<>();
        this.urlMap = new HashMap<>();
        this.idf = new HashMap<>();
    }

    public void crawl(String seed) throws IOException {
        FileHelper.clearDirectory("Webpages");
        visitedUrl.add(seed);
        queue.offer(seed);

        while (!queue.isEmpty()) {
            String currentUrl = queue.poll();
            String html = WebRequester.readURL(currentUrl);

            Webpage page = new Webpage(html, currentUrl);
            urlMap.put(currentUrl, page);

            addOutLinksToQueue(page);


        }

        setInLinks();
        calculateIdf();
        setTfIdf();
        calculatePageRank();

        for (Webpage page : urlMap.values()) {
            FileHelper.saveToFile(page, "Webpages" + File.separator + FileHelper.urlToFilename(page.getUrl()) + ".ser");
        }
        FileHelper.saveToFile(idf, "Webpages" + File.separator + "idf.ser");
        FileHelper.saveToFile(new ArrayList<>(urlMap.values()), "Webpages" + File.separator + "All Webpages.ser");
    }

    public void addOutLinksToQueue(Webpage page) {
        for (String outLink : page.getOutLinks()) {
            if (!visitedUrl.contains(outLink)) {
                queue.add(outLink);
                visitedUrl.add(outLink);
            }
        }
    }
    public void setInLinks() {
        for (Webpage page : urlMap.values()) {
            for (String outLink : page.getOutLinks()) {
                urlMap.get(outLink).addInLinks(page.getUrl());
            }
        }
    }

    public void setTfIdf() {
        for (Webpage page : urlMap.values()) {
            page.calculateTfIdf(idf);
        }
    }
    public void calculateIdf() {
        int totalDocuments = urlMap.values().size();
        for (Webpage page : urlMap.values()) {
            for (String word : page.getWordFreq().keySet()) {
                if (!idf.containsKey(word)) {
                    idf.put(word, 0d);
                }
                idf.put(word, idf.get(word) + 1);
            }
        }
        for (String word : idf.keySet()) {
            idf.put(word, Math.log(totalDocuments/(1 + idf.get(word))) / Math.log(2));
        }
    }

    public void calculatePageRank() {
        int N = urlMap.size();
        double[][] adjacencyMatrix = new double[N][N];
        double alpha = 0.1;
        int row = 0;
        int col = 0;
        int pageIndex = 0;
        HashMap<Webpage, Double> pageRank;

        for (Webpage i : urlMap.values()) {
            int rowTotal = 0;
            for (Webpage j : urlMap.values()) {
                if (i.getOutLinks().contains(j.getUrl())) {
                    adjacencyMatrix[row][col] = 1;
                    rowTotal++;
                }
                col++;
            }
            for (int k = 0; k < N; k++) {
                adjacencyMatrix[row][k] = ((1 - alpha) * (adjacencyMatrix[row][k] / rowTotal) + (alpha / N));
            }
            col = 0;
            row++;
        }

        double[][] previous = new double[1][N];
        Arrays.fill(previous[0], (double) 1/N);
        double[][] result = MatMult.multiplyMatrices(previous, adjacencyMatrix);
        while (MatMult.calculateEuclideanDistance(previous, result) > 0.0001) {
            previous = result;
            result = MatMult.multiplyMatrices(previous, adjacencyMatrix);
        }

        for (Webpage aPage : urlMap.values()) {
            aPage.setPageRank(result[0][pageIndex]);
            pageIndex++;
        }
    }
    public static void main(String[] args) throws IOException {
        Crawler crawl1 = new Crawler();
        crawl1.crawl("http://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html");
    }
}
