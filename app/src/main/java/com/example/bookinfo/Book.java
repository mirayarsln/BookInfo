package com.example.bookinfo;
public class Book {
    private String title;
    private String summary;

    public Book(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String newSummary) {
        this.summary = newSummary;
    }

    @Override
    public String toString() {
        return title + " - " + summary;
    }
}
