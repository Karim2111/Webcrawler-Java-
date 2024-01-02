import java.io.*;
import java.util.*;

public class Webpage implements Serializable, Comparable<Webpage> {
    private String url;
    private String title;
    private Set<String> inLinks;
    private List<String> outLinks;
    private HashMap<String, Integer> wordFreq;
    private HashMap<String, Double> tf;
    private HashMap<String, Double> idf;
    private HashMap<String, Double> tfIdf;
    private double pageRank;
    private double score;

    public String getUrl() {return url;}
    public String getTitle() {return title;}
    public double getScore() {return score;}

    public Set<String> getInLinks() {return inLinks;}
    public List<String> getOutLinks() {return outLinks;}
    public HashMap<String, Integer> getWordFreq() {return wordFreq;}
    public HashMap<String, Double> getTf() {return tf;}
    public double getPageRank() {return pageRank;}
    public double getTfIdf(String term) {
        if (tfIdf.containsKey(term)) return tfIdf.get(term);
        return 0;
    }

    public void setUrl(String url) {this.url = url;}
    public void setTitle(String title) {this.title = title;}
    public void setInLinks(Set<String> inLinks) {this.inLinks = inLinks;}
    public void setOutLinks(List<String> outLinks) {this.outLinks = outLinks;}
    public void setWordFreq(HashMap<String, Integer> wordFreq) {this.wordFreq = wordFreq;}
    public void setTf(HashMap<String, Double> tf) {this.tf = tf;}
    public void setPageRank(Double pageRank) {this.pageRank = pageRank;}
    public void setScore(Double score) {this.score = score;}
    public void setIdf(HashMap<String, Double> idf) {this.idf = idf;}
    public void setTfIdf(HashMap<String, Double> tfIdf) {this.tfIdf = tfIdf;}

    public void addInLinks(String inLinks) {this.inLinks.add(inLinks);}


    public Webpage(String html, String url) {
        this.url = url;
        this.title = findTitle(html);
        this.inLinks = new HashSet<>();
        this.outLinks = findOutLinks(html, url);
        this.wordFreq = findWordFreq(html);
        this.tf = calculateTf();
        this.pageRank = 0d;
        this.score = 0d;
        this.idf = new HashMap<String, Double>();
        this.tfIdf = new HashMap<String, Double>();

    }

    public String findTitle(String html) {
        String openingTag = "<title>";
        String closingTag = "</title>";
        int startIndex = html.indexOf(openingTag) + openingTag.length();
        int endIndex = html.indexOf(closingTag);
        return html.substring(startIndex, endIndex);
    }

    public List<String> findOutLinks(String html, String url) {
        String openingTag = "<a href=\"";
        String closingTag = "\">";
        Set<String> outLinks = new HashSet<>();
        String urlBase = url.substring(0, url.lastIndexOf("/") + 1);

        while (true) {
            int startIndex = html.indexOf(openingTag);
            int endIndex = html.indexOf(closingTag);

            if (startIndex == -1) {
                break;
            }

            startIndex += openingTag.length();
            String link = html.substring(startIndex, endIndex);
            if (link.startsWith("./")) {
                outLinks.add(link.replace("./", urlBase));
            }
            else {
                outLinks.add(html.substring(startIndex, endIndex));
            }
            html = html.substring(endIndex + closingTag.length());
        }
        return new ArrayList<>(outLinks);
    }

    public HashMap<String, Integer> findWordFreq(String html) {
        String openingTag = "<p>";
        String closingTag = "</p>";
        HashMap<String, Integer> WordFreq = new HashMap<>();

        while (true) {
            int startIndex = html.indexOf(openingTag);
            int endIndex = html.indexOf(closingTag);

            if (startIndex == -1) {
                break;
            }
            startIndex += openingTag.length();
            String[] wordsInParagraph = html.substring(startIndex, endIndex).trim().split("\\s+");
            for (String word : wordsInParagraph) {
                if (!WordFreq.containsKey(word)) {
                    WordFreq.put(word, 0);
                }
                WordFreq.put(word, WordFreq.get(word) + 1);
            }
            html = html.substring(endIndex + closingTag.length());
        }

        return WordFreq;
    }

    public HashMap<String, Double> calculateTf() {
        int totalWords = 0;
        HashMap<String, Double> newTf = new HashMap<>();
        for (int frequency : wordFreq.values()) {
            totalWords += frequency;
        }
        for (String word : wordFreq.keySet()) {
            newTf.put(word, ((double)wordFreq.get(word)/totalWords));
        }
        return newTf;
    }

    public void calculateTfIdf(HashMap<String, Double> idfHashMap) {
        for (String term : tf.keySet()) {
            if (idfHashMap.containsKey(term)) {
                tfIdf.put(term, (Math.log(1 + tf.get(term)) / Math.log(2)) * idfHashMap.get(term));
            }
            else {
                tfIdf.put(term, 0d);
            }
        }
    }

    @Override
    public int compareTo(Webpage o) {
        double thisRounded = Math.round(this.getScore()*1000)/1000.0d;
        double oRounded = Math.round(o.getScore()*1000)/1000.0d;

        if (thisRounded == oRounded) {
            return this.getTitle().compareTo(o.getTitle());
        }
        else if (thisRounded > oRounded) return -1;
        return 1;
    }

    public String toString() {
        return title;
    }
}
