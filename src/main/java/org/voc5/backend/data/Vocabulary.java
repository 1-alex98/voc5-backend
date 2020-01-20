package org.voc5.backend.data;

public class Vocabulary {
    private final Integer id;
    private final String answer;
    private final String question;
    private final String language;
    private final Integer phase;

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
}
