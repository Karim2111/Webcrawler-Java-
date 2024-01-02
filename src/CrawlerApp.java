import java.io.IOException;

public class CrawlerApp {
    public static void main(String[] args) throws IOException {
        ProjectTesterImp project =  new ProjectTesterImp();
        project.initialize();
        project.crawl("http://people.scs.carleton.ca/~davidmckenney/fruits/N-0.html");
    }
}
