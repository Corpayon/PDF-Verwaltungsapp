package com.example.meinuniverwalter;

import java.util.List;

public class Untermenue {
    String name;
    String fach;


    public Untermenue(String name,String fach) {
        this.name = name;
        this.fach = fach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    @Override
    public String toString() {
        return "Untermenue{" +
                "name='" + name + '\'' +
                ", fach='" + fach + '\'' +

                '}';
    }
}
