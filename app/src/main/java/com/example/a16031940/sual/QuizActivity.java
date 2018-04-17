package com.example.a16031940.sual;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extrascore";
    private static final long COUNTDOWN_IN_MILLIS = 40000;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView getTextViewQuestionCount;
    private TextView textViewCountDown;
    private Button button_next;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private ColorStateList textColorDefaultRb;
    private List<Question> questionList;

    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.question_text);
        textViewScore = findViewById(R.id.score);
        textViewCountDown = findViewById(R.id.countdown);
        getTextViewQuestionCount = findViewById(R.id.qns_count);
        rbGroup = findViewById(R.id.rg);
        rb1 = findViewById(R.id.option1);
        rb2 = findViewById(R.id.option2);
        rb3 = findViewById(R.id.option3);
        button_next = findViewById(R.id.confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);
        showNextQuestion();

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answer){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        checkAnswer();
                    }else{
                        Toast.makeText(QuizActivity.this,"Please select an answer!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    showNextQuestion();
                }
            }
        });
    }
    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if(questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestions());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            getTextViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answer = false;
            button_next.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountdown();
        }else{
            finishQuiz();
        }
    }
    private  void startCountdown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                 timeLeftInMillis = 0;
                 updateCountDownText();
                 checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int)(timeLeftInMillis / 1000) / 60;
        int seconds = (int)(timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        textViewCountDown.setText(timeFormatted);

        if(timeLeftInMillis < 10000){
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer(){
    answer = true;
    countDownTimer.cancel();

    RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
    int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

    if(answerNr == currentQuestion.getAnswerNr()){
        score++;
        textViewScore.setText("Score " + score);
    }
    showSolution();
    }


    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }

        if(questionCounter < questionCountTotal){
            button_next.setText("Next");
        }else{
            button_next.setText("Finish");
        }


    }

    private void finishQuiz(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE , score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
