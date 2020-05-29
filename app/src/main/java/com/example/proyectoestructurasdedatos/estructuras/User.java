package com.example.proyectoestructurasdedatos.estructuras;

import java.time.LocalTime;

public class User {
    private LocalTime attentionHour;
    private String uid;
    private Double priority;

    public User(LocalTime attentionHour, String uid, Double priority) {
        this.attentionHour = attentionHour;
        this.uid = uid;
        this.priority = priority;
    }

    public LocalTime getAttentionHour() {
        return attentionHour;
    }

    public void setAttentionHour(LocalTime attentionHour) {
        this.attentionHour = attentionHour;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }
}
