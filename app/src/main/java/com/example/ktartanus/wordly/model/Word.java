package com.example.ktartanus.wordly.model;

public class Word {

    public enum WORD_STATE {EMPTY, NOT_KNOWN, JUST_KNEW, JUST_REMEMBERED, KNOWN, WELL_KNOWN };

    private String content;
    private WORD_STATE state;
    private String state_last_modyfication_date;
    private String translation;

    public Word(String content, WORD_STATE state, String state_last_modyfication_date, String translation) {
        this.content = content;
        this.state = state;
        this.state_last_modyfication_date = state_last_modyfication_date;
        this.translation = translation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public WORD_STATE getState() {
        return state;
    }

    public void setState(WORD_STATE state) {
        this.state = state;
    }

    public String getState_last_modyfication_date() {
        return state_last_modyfication_date;
    }

    public void setState_last_modyfication_date(String state_last_modyfication_date) {
        this.state_last_modyfication_date = state_last_modyfication_date;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
