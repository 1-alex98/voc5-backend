package org.voc5.backend.data;

public class Vocabulary {
    private Integer id;
    private String answer;
    private String question;
    private String language;
    private Integer phase;

    public Vocabulary() {

    }

    public Vocabulary(int id, String answer, String question, String language, Integer phase) {
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.language = language;
        this.phase = phase;
    }

    public int getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }
}
