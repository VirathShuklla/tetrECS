package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.game.Grid;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
public class InstructionsScene extends BaseScene{
    private static final Logger logger = LogManager.getLogger(InstructionsScene.class);

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public InstructionsScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating an Instruction scene");
    }

    @Override
    public void initialise() {
        getScene().setOnKeyPressed(keyEvent ->{
            if (keyEvent.getCode().toString().equals("ESCAPE")){
                logger.info("Pressing esc to exit the Instructions scene");
                gameWindow.startMenu();
            }
        });
    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

        var howtoplayPane = new StackPane();
        howtoplayPane.setMaxWidth(gameWindow.getWidth());
        howtoplayPane.setMaxHeight(gameWindow.getHeight());
        howtoplayPane.getStyleClass().add("instruction-background");
        root.getChildren().add(howtoplayPane);

        var intructions = new BorderPane();
        intructions.getStyleClass().add("backgroundhowtoplay");

        var borderPane = new BorderPane();
        howtoplayPane.getChildren().add(borderPane);
        howtoplayPane.getStyleClass().add("howtoplayscene");
        var space = new Text();


        var vBox = new VBox();

        borderPane.setCenter(vBox);

//        borderPane.getStyleClass().add("backgroundhowtoplay");

        var title = new Text("Instructions");
        title.getStyleClass().add("title");
        vBox.getChildren().add(title);
        var hbox= new HBox(250,vBox);
        borderPane.setTop(hbox);

        var text = new Text();
        var box = new VBox(1000, text);
        var boxs = new HBox(1000, text, box);
        boxs.getStyleClass().add("backgroundhowtoplay");
        vBox.getChildren().add(boxs);

        logger.info(gameWindow.getWidth());
        logger.info(gameWindow.getHeight());


        var gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(0, 180, 0, 180));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int rows = 3;
        int cols = 5;
        int piece = 55;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                int number = (j * cols) + i;
                GamePiece gamePiece = GamePiece.createPiece(number);
                PieceBoard pieceBoard = new PieceBoard(gamePiece, gameWindow.getWidth()/10, gameWindow.getHeight()/10);
                pieceBoard.displayGamePieces(gamePiece);
                gridPane.add(pieceBoard, i, j);
            }
        }

        borderPane.setBottom(gridPane);
    }

}