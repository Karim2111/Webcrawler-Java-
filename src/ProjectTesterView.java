import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


import java.awt.*;
import java.util.*;
import java.util.List;

public class ProjectTesterView extends Pane {
    private ProjectTesterImp model;
    private ListView<SearchResult> list;
    private Button SearchButton;
    private RadioButton Boost;
    private TextField SearchText;

    public ListView<SearchResult> getList() { return list; }
    public Button getSearchButton() { return SearchButton; }
    public RadioButton getBoost() { return Boost;}
    public TextField getNewItemField() { return SearchText; }

    public void update(){
        String text = SearchText.getText();
        if (Objects.equals(text.strip(), "")) {return;}
        boolean boost = Boost.isSelected();
        List<SearchResult> updatedlist = model.search(text,boost,10);
        ObservableList<SearchResult> observableList = FXCollections.observableArrayList(updatedlist);
        list.setItems(observableList);
    }
    public ProjectTesterView(ProjectTesterImp m){
        model = m;


        SearchText = new TextField();
        SearchText.relocate(10, 10);
        SearchText.setPrefSize(200, 25);

        SearchButton = new Button("Search");
        SearchButton.relocate(225, 10);
        SearchButton.setPrefSize(100, 25);

        Boost = new RadioButton("Boost");
        Boost.relocate(225, 50);
        Boost.setPrefSize(100, 25);

        list = new ListView<>();
        list.relocate(10, 50);
        list.setPrefSize(200, 235);

        update();

        getChildren().addAll(SearchText, SearchButton, Boost, list);
    }

}
