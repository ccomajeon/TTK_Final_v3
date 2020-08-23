package com.example;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.example.module.CategoryID;
import com.example.module.Estimate;
import com.example.module.EstimateBukkit;
import com.example.module.Part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Totheking extends Application {
    private boolean login = false;
    private String memberID;
    private int memberIndex;
    private String nickname;
    public Estimate estimate;
    private EstimateBukkit bukkit;

    public Totheking(){
        estimate = new Estimate();
        bukkit = new EstimateBukkit(5);
    }

    public void setLogin(int index, String id, String nick){
        login = true;
        memberID = id;
        memberIndex = index;
        nickname = nick;
    }

    public int getMemberIndex(){
        return memberIndex;
    }

    public String getNickname(){
        return nickname;
    }

    public boolean isLogin(){
        return login;
    }

    public void logout(){
        login = false;
        memberID = null;
        memberIndex = 0;
    }

    public String getSelectSocket(){
        String socket=null;
        int arr[] = {0, 1, 4};
        for(int i : arr){
            if(estimate.parts[i].socket != null && estimate.parts[i].socket.equals(""))
                socket = estimate.parts[i].socket;
        }
        return socket;
    }

    public boolean isSelectSocket(String socket){
        String s = getSelectSocket();
        if(s == null)
            return true;
        return s.contains(socket) || socket.contains(s);
    }

    public boolean setEstimate(CategoryID index, Part part){
        estimate.parts[index.getID() - 1] = part;
        /*if(index != CategoryID.CPU && index != CategoryID.MAINBOARD && index != CategoryID.COOLER)
            return true;
        if(isSelectSocket(part.socket)) {
            estimate.parts[index.getID() - 1] = part;
            return true;
        }
        return false;*/
        return true;
    }

    public void removeEstimate(int index){
        if(estimate == null)
            return;
        estimate.parts[index] = new Part();
    }

    public void cleanEstimate(){
        estimate.resetParts();
    }

    public String getEstimatePartName(int index){
        if(estimate.parts[index] == null)
            return "없음";
        return estimate.parts[index].getName();
    }

    public boolean addBukkit(String code){
        boolean bb = bukkit.addBukkitCode(code);
        save();
        return bb;
    }

    public boolean removeBukkit(String code){
        boolean bb =  bukkit.removeBukkitCode(code);
        save();
        return bb;
    }

    public void cleanBukkit(){
        bukkit.cleanBukkitCode();
        save();
    }

    public boolean isPartBukkitSearch(){
        for(int i=0; i<8; i++){
            if(estimate.parts[i] != null && estimate.parts[i].index != 0)
                return true;
        }
        return false;
    }

    public Map<String, String> getParameter(){
        String[] arr = {"cpu", "mb", "vga", "power", "cooler", "case","ram", "hdd"};
        Map<String, String> param = new HashMap<>();
        for(int i=0; i<8; i++){
            if(estimate.parts[i] != null)
                param.put(arr[i], estimate.parts[i].index+"");
        }
        return param;
    }

    public String partbukkitToSearchCode(){
        return estimate.getCode(2);
    }

    public String getCodeBukkit(){
        return bukkit.getCodes();
    }

    public ArrayList<String> getCodeList(){
        return bukkit.getCodeList();
    }

    public void save(){
        SharedPreferences sf = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        String codes = getCodeBukkit();
        if(codes == null)
            return;
        else{
            editor.putString("codelist", codes);
            editor.apply();
            editor.commit();
        }
    }

    public void load(Activity a){
        estimate = new Estimate();
        bukkit = new EstimateBukkit(5);
        SharedPreferences sf = getSharedPreferences("test", MODE_PRIVATE);
        String codes = sf.getString("codelist", null);
        if(codes == null)
            return;
        else {
            //Toast.makeText(a, codes + "    gkgkgkgkgk", Toast.LENGTH_LONG);
            bukkit.load(codes);
        }
    }
}
