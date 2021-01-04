package com.example.meinuniverwalter;

public class Fach implements Comparable<Fach>{
    private String name;
    private int status;

    public Fach(String name, int status){
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int compareTo(Fach o) {
        int status = o.getStatus();
        return this.status - status;
    }
}
