package sample;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    GridPane grid = new GridPane();
    final int GRID_SIZE = 4;
    final int SCRAMBLES = 1001;
    boolean isScrambling;
    int emptyX = 3, emptyY = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Puzzle 15");

        int cnt = 0;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                cnt++;
                Button btn = new Button(String.valueOf(cnt <= 15 ? cnt : ""));
                btn.setPrefWidth(200);
                btn.setPrefHeight(200);
                int finalX = x;
                int finalY = y;
                btn.setOnAction(e -> click(finalX, finalY)); // lambda
                grid.add(btn, x, y);
            }
        }

        // scramble (make a bunch of random, but valid, moves)
        isScrambling = true;
        for (int i = 0; i < SCRAMBLES; i++) {
            // lets find a button that is next to the empty space

            // decide direction (horizontally or vertically) from empty space
            int dir = Math.random() < 0.5 ? -1 : 1;
            int moveX = 0, moveY = 0;

            if (Math.random() > 0.5) {
                // move horizontally
                moveX = dir > 0.5 ? 1 : -1;
            } else {
                // move vertically
                moveY = dir > 0.5 ? 1 : -1;
            }
//            int randomMoveDirection2 = Math.random() < 0.5 ? -1 : 1;
//            System.out.println("Random direction: " + randomMoveDirection1 + "," + randomMoveDirection2);
//
            Button btn = getButtonFromGrid(emptyX + moveX, emptyY + moveY);
            if (btn != null) {
                btn.fire();
            }
        }
        isScrambling = false;

        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    Button getButtonFromGrid(int col, int row) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (Button) node;
            }
        }
        return null;
    }

    void click(int x, int y) {
        // is clicked tile neighbouring the empty space and therefore safe to move ?
        boolean isSafeToMove = false;
        if (emptyX == x - 1 && emptyY == y) isSafeToMove = true;
        if (emptyX == x + 1 && emptyY == y) isSafeToMove = true;
        if (emptyX == x && emptyY == y - 1) isSafeToMove = true;
        if (emptyX == x && emptyY == y + 1) isSafeToMove = true;
        if (!isSafeToMove) return;

        // button is safe to move, so lets move it
        Button clickedButton = getButtonFromGrid(x, y);
        Button emptyButton = getButtonFromGrid(emptyX, emptyY);
        emptyButton.setText(clickedButton.getText());
        clickedButton.setText("");

        // update position of the empty space
        emptyX = x;
        emptyY = y;

        if (!isScrambling && checkIfCompleted()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ny information till dej");
            alert.setContentText("Grattis, du vann!");
            alert.showAndWait();
        }
    }

    boolean checkIfCompleted() {
        int cnt = 0;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                cnt++;
                Button btn = getButtonFromGrid(x, y);
                if (cnt == 16 && btn.getText().equals("")) return true;
                String cntString = String.valueOf(cnt);
                String btnText = btn.getText();
                if (!cntString.equals(btnText)) return false;
            }
        }
        return false;
    }

    static void main(String[] args) {
        launch(args);
    }
}
