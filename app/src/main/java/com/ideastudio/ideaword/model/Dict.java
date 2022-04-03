package com.ideastudio.ideaword.model;

import java.util.List;

public class Dict {
    private String query;
    private List<String> suggestions;
    private List<String> data;

    public String getQuery() { return query; }
    public void setQuery(String value) { this.query = value; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> value) { this.suggestions = value; }

    public List<String> getData() { return data; }
    public void setData(List<String> value) { this.data = value; }
}
