import java.io.File;
import java.io.IOException;
import java.util.*;

public class ProjectTesterImp implements ProjectTester {
    @Override
    public void initialize() {
        FileHelper.clearDirectory("Webpages");
    }

    @Override
    public void crawl(String seedURL) {
        Crawler crawlTest = new Crawler();
        try {
            crawlTest.crawl(seedURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getOutgoingLinks(String url) {
        Webpage page;
        try {
            page = (Webpage) FileHelper.readFile("Webpages" + File.separator + FileHelper.urlToFilename(url) + ".ser");
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return page.getOutLinks();
    }

    @Override
    public List<String> getIncomingLinks(String url) {
        Webpage page;
        try {
            page = (Webpage) FileHelper.readFile("Webpages" + File.separator + FileHelper.urlToFilename(url) + ".ser");
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return new ArrayList<>(page.getInLinks());
    }

    @Override
    public double getPageRank(String url) {
        Webpage page;
        try {
            page = (Webpage) FileHelper.readFile("Webpages" + File.separator + FileHelper.urlToFilename(url) + ".ser");
        } catch (IOException | ClassNotFoundException e) {
            return -1;
        }
        return page.getPageRank();
    }

    @Override
    public double getIDF(String word) {
        HashMap<String, Double> idf;
        try {
            idf = (HashMap<String, Double>) FileHelper.readFile("Webpages" + File.separator + "idf.ser");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (idf.containsKey(word)) {
            return idf.get(word);
        }
        return 0;
    }

    @Override
    public double getTF(String url, String word) {
        Webpage page;
        try {
            page = (Webpage) FileHelper.readFile("Webpages" + File.separator + FileHelper.urlToFilename(url) + ".ser");
        } catch (IOException | ClassNotFoundException e) {
            return 0;
        }
        if (page.getTf().containsKey(word)) {
            return page.getTf().get(word);
        }
        return 0;
    }

    @Override
    public double getTFIDF(String url, String word) {
        Webpage page;
        try {
            page = (Webpage) FileHelper.readFile("Webpages" + File.separator + FileHelper.urlToFilename(url) + ".ser");
        } catch (IOException | ClassNotFoundException e) {
            return 0;
        }
        return page.getTfIdf(word);
    }

    @Override
    public List<SearchResult> search(String query, boolean boost, int X) {
        HashMap<String, Double> queryVector = new HashMap<>();
        //query vector {word : tf-idf}
        String[] words = query.split(" ");
        int totalQueryWords = words.length;
        for (String word : words) {
            if (!queryVector.containsKey(word)) {
                queryVector.put(word, 0d);
            }
            queryVector.put(word, queryVector.get(word) + 1);
        }
        for (String word : queryVector.keySet()) {
            double tf = queryVector.get(word) / totalQueryWords;
            queryVector.put(word, Math.log(1 + tf) / Math.log(2) * getIDF(word));
        }

        List<Webpage> allWebpages = getAllWebpages();
        for (Webpage page : allWebpages) {
            page.setScore(getCosineSimilarity(queryVector, page, boost));
        }
        Collections.sort(allWebpages);

        List<SearchResult> result = new ArrayList<>();
        for (Webpage page : allWebpages) {
            result.add(new SearchResultImp(page.getTitle(), page.getScore()));
        }

        if (X > result.size()) {return result;}
        return result.subList(0,X);
    }

    public double getCosineSimilarity(Map<String, Double> queryVector, Webpage page, boolean boost) {
        double numerator = 0;
        double leftDenominator = 0;
        double rightDenominator = 0;

        for (String term : queryVector.keySet()) {
            double tfIdf = page.getTfIdf(term);
            numerator += queryVector.get(term) * tfIdf;
            leftDenominator += queryVector.get(term) * queryVector.get(term);
            rightDenominator += tfIdf * tfIdf;
        }

        leftDenominator = Math.sqrt(leftDenominator);
        rightDenominator = Math.sqrt(rightDenominator);

        double result;

        if (numerator == 0) {result = 0;}
        else {result = numerator / (leftDenominator * rightDenominator);}

        if (boost) {result = result * page.getPageRank();}

        return result;
    }

    public ArrayList<Webpage> getAllWebpages() {
        try {
            return (ArrayList<Webpage>) FileHelper.readFile("Webpages" + File.separator + "All Webpages.ser");
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static void main(String[] args) {
    }
}
