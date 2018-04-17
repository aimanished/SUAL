package com.example.a16031940.sual;

public class Question {
    private String questions;
    private String option1;
    private String option2;
    private String option3;
    private int answerNr;
    private String sound;

    public Question() {}

    public Question(String questions, String option1, String option2, String option3, int answerNr , String sound) {
        this.questions = questions;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNr = answerNr;
        this.sound = sound;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
