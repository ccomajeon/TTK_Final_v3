package com.example.etc;

public enum ListModelType {
    NONE(-1), TITLE(0), ESTIMATE_INFO_ITEM(1), ESTIMATE_INFO_PART_ITEM(2), ESTIMATE_INFO_REVIEW_READ(3), ESTIMATE_INFO_REVIEW_WRITE(4), ESTIMATE_LIST_ITEM(5), PART_LIST_ITEM(6), SELECT_PART_ITEM(7);

    int type;
    String category;
    ListModelType(int type){
        this.type = type;
    }
    public int getType(){
        return type;
    }

    public static ListModelType getType(int t){
        switch (t){
            case -1:
                return NONE;
            case 0:
                return TITLE;
            case 1:
                return ESTIMATE_INFO_ITEM;
            case 2:
                return ESTIMATE_INFO_PART_ITEM;
            case 3:
                return ESTIMATE_INFO_REVIEW_READ;
            case 4:
                return ESTIMATE_INFO_REVIEW_WRITE;
            case 5:
                return ESTIMATE_LIST_ITEM;
            case 6:
                return PART_LIST_ITEM;
            case 7:
                return SELECT_PART_ITEM;
        }
        return null;
    }
}
