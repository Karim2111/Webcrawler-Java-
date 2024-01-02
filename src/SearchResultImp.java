public class SearchResultImp implements SearchResult {
    private String title;
    private double score;

    public SearchResultImp(String title, double score) {
        this.title = title;
        this.score = score;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double getScore() {
        return score;
    }

    public String toString() {
        return title + ": " + score;
    }
}
