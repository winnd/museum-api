package com.museum.api.common.util;

  
import net.sourceforge.pinyin4j.PinyinHelper;  
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;  
  
public class PinyinUtil {  
    public static String toPinYin(String str) {  
        String py = "";  
        String[] t = new String[str.length()];  
          
        char [] hanzi=new char[str.length()];  
        for(int i=0;i<str.length();i++){  
            hanzi[i]=str.charAt(i);  
        }  
          
        net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat t1 = new HanyuPinyinOutputFormat();  
        t1.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        t1.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        t1.setVCharType(HanyuPinyinVCharType.WITH_V);  
          
        try {  
            for (int i = 0; i < str.length(); i++) {  
                if ((str.charAt(i) >= 'a' && str.charAt(i) < 'z')  
                        || (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')  
                        || (str.charAt(i) >= '0' && str.charAt(i) <= '9')) {  
                    py += str.charAt(i);  
                } else {  
                        t = PinyinHelper.toHanyuPinyinStringArray(hanzi[i], t1);  
                        py=py+t[0];  
                    }  
                }  
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
  
        return py.trim().toString();  
    }  
    /**
     * 取第一个字的首字母的大写
     * @param str
     * @return
     */
    public static char getInitials(String str){
    	
    	char first  = str.charAt(0);	
		 net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat t1 = new HanyuPinyinOutputFormat();  
	     t1.setCaseType(HanyuPinyinCaseType.UPPERCASE);  
	     t1.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
	     t1.setVCharType(HanyuPinyinVCharType.WITH_V); 
    	
    	try {  
           
        if ((first>= 'a' && first < 'z')  
                || (first>= 'A' && first <= 'Z')  
                ) {  
        	return   Character.toUpperCase(first);
        } else if(isChinese(first)){  
                String[] a = PinyinHelper.toHanyuPinyinStringArray(first, t1);    
                return a[0].charAt(0);

         }else{
            	return '#';
         } 
                 
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();
            return 0;
        } 
    }
    
    static boolean isChinese(char c) {
        boolean result = false;
        if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
            result = true;
        }
        return result;
    }
    
    public static void main(String args[]){  
        System.out.println(PinyinUtil.getInitials("杭州"));  
    }  
}