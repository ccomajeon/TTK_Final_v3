package com.example.module;

import java.util.ArrayList;

public class EstimateBukkit{
    ArrayList<String> code;
    int size;

    public EstimateBukkit(int size){
        code = new ArrayList<String>();
        this.size = size;
    }

    public boolean hasCode(String c){
        if(code.size()==0)
            return false;
        for(String str : code){
            if(str.equals(c))
                return true;
        }
        return false;
    }

    public boolean addBukkitCode(String c){
        if(hasCode(c))
            return false;
        if(code.size() == size)
            return false;
        code.add(c);
        return true;
    }

    public boolean removeBukkitCode(int index){
        if(code.size() == 0 || index >= code.size())
            return false;
        code.remove(index);
        return true;
    }

    public boolean removeBukkitCode(String c){
        if(code.size() == 0)
            return false;
        return code.remove(c);
    }

    public void cleanBukkitCode(){
        code.clear();
    }

    public String getCodes(){
        if(code.size() == 0)
            return null;
        else{
            String codes = code.get(0);
            for(int i=1; i<code.size(); i++){
                String t = code.get(i);
                if(t == null || t.equals(""))
                    continue;
                codes += "/"+code.get(i);
            }
            return codes;
        }
    }

    public ArrayList<String> getCodeList(){
        return code;
    }

    public void load(String codes){
        String[] arr = codes.split("/");
        code = new ArrayList<String>();
        if(arr != null) {
            for(int i=0; i<arr.length; i++){
                code.add(arr[i]);
            }
        }
    }
}
