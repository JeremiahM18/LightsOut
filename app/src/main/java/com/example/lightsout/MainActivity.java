package com.example.lightsout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int[] OnColor = new int[]{
            R.color.blue_500,
            R.color.yellow,
            R.color.red,
            R.color.green,
            R.color.purple
    };

    private int OnColorId = R.color.blue_500;
    private final Random rnd = new Random();

    private void pickRandomColor() {
        OnColorId = OnColor[rnd.nextInt(OnColor.length)];
    }
    public static final int GRID_SIZE = 3;
    private int score = 0;
    private GridLayout grid;
    private TextView scoreLabel;
    private Button resetButton;
    private Button randomizeButton;
    private boolean[][] cellState = new boolean[GRID_SIZE][GRID_SIZE];

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Identify which button was pressed
            for (int i = 0; i < grid.getChildCount(); i++) {
                Button currButton = (Button) grid.getChildAt(i);
                if (currButton == v) {
                    int row = i / GRID_SIZE;
                    int col = i % GRID_SIZE;

                    cellState[row][col] = !cellState[row][col];

                    score++;

                    recolor();
                    updateScore();
                    checkWinAndNavigate();
                    break;
                }
            }
        }
    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            grid = findViewById(R.id.light_grid);
            scoreLabel = findViewById(R.id.counterScore);
            resetButton = findViewById(R.id.btn_reset);
            randomizeButton = findViewById(R.id.btn_randomize);

            // Start randomized board
            //clearBoard(false);
            randomize();
            recolor();
            updateScore();

            // Bind grid listeners
            for (int i = 0; i < grid.getChildCount(); i++) {
                Button currButton = (Button) grid.getChildAt(i);
                currButton.setOnClickListener(buttonListener);
            }

            // Reset -> all Off
            resetButton.setOnClickListener(v -> {
                clearBoard();
                score = 0;
                recolor();
                updateScore();
            });

            // Randomize -> random On/Off
            randomizeButton.setOnClickListener(v -> {
                pickRandomColor();
                randomize();
                recolor();
                updateScore();
                checkWinAndNavigate();
            });
        }

        private void clearBoard() {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    cellState[i][j] = false;
                }
            }
        }

    public void randomize() {
        Random random = new Random();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cellState[i][j] = random.nextBoolean();
            }
        }
    }
        public void recolor() {
            final int offColor = ContextCompat.getColor(this, R.color.black);
            final int onColor = ContextCompat.getColor(this, OnColorId);

            for (int i = 0; i < grid.getChildCount(); i++) {
                Button gridButton = (Button) grid.getChildAt(i);

                // Find the button's row and col
                int row = i / GRID_SIZE;
                int col = i % GRID_SIZE;
                gridButton.setBackgroundColor(cellState[row][col] ? onColor : offColor);
            }
        }

        private int countOn(){
            int count = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (cellState[i][j]) {
                        count++;
                    }
                }
            }
            return count;
        }

        private void updateScore(){
            String label = getString(R.string.score_format, score);
            scoreLabel.setText(label);
        }

        private void checkWinAndNavigate() {
            // Win when all lights are On
            if (countOn() == GRID_SIZE * GRID_SIZE) {
                Intent intent = new Intent(this, WinActivity.class);
                startActivity(intent);
            }
        }
}