package com.jenscribe.demo;

public class CharsetTool {
    public String iso2utf8(String s){
        try {
            byte[] bytes = s.getBytes("ISO-8859-1");
            return new String(bytes, "UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    public String utf82iso(String s){
        try {
            byte[] bytes = s.getBytes("UTF-8");
            return new String(bytes, "ISO-8859-1");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    public boolean isEncoding(String s, String encode){
        try {
            if (s.equals(new String(s.getBytes(), encode)))
                return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
