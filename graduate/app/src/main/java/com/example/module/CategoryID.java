package com.example.module;

public enum CategoryID {
    CPU(1,"CPU"), MAINBOARD(2, "MAINBOARD"), VGA(3, "VGA"), POWER(4, "POWER"), COOLER(5, "COOLER"), BOX(6, "CASE"), RAM(7, "RAM"), HDD(8, "HDD/SSD");
    int id;
    String category;
    CategoryID(int id, String name){
        this.id = id;
        category = name;
    }
    public String getCategoryName(){
        return category;
    }
    public int getID(){
        return id;
    }
}
