package com.filenanumi.sejongce.rss;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtility
{
    static SimpleDateFormat[] formats;
    
    static
    {
        SimpleDateFormat[] arrayOfSimpleDateFormat = new SimpleDateFormat[5];
        
        SimpleDateFormat localSimpleDateFormat0 = new SimpleDateFormat("yyyy년 MM월 dd일");
        arrayOfSimpleDateFormat[0] = localSimpleDateFormat0;
        
        SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("y-M-d H:m:s");
        arrayOfSimpleDateFormat[1] = localSimpleDateFormat1;
        
        SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("y/M/d H:m:s");
        arrayOfSimpleDateFormat[2] = localSimpleDateFormat2;

        SimpleDateFormat localSimpleDateFormat3 = new SimpleDateFormat("dd MMM yyyy H:m:s Z");
        arrayOfSimpleDateFormat[3] = localSimpleDateFormat3;

        SimpleDateFormat localSimpleDateFormat4 = new SimpleDateFormat("EEE, dd MMM yyyy H:m:s Z");
        arrayOfSimpleDateFormat[4] = localSimpleDateFormat4;
        
        formats = arrayOfSimpleDateFormat;        
    }
    
    /**
     * ���ڿ��� �Ǿ��� �ð��(XML ���) ��Ŀ� �°� ��ȯ���� ������
     * @param pubdate ��¥�� �Է��ϴ� ��(String)
     * @param type Ÿ��� �����(int)</br>0: yyyy�� MM�� dd�� HH�� mm�� ss��</br>1: y-M-d H:m:s</br>2: y/M/d H:m:s</br>3: dd MMM yyy H:m:s Z</br>4: EEE, dd MMM yyy H:m:s Z
     * @return String ��� ���ϵǾ���
     */
    public static String getDateByString(String pubdate, int type)
    {
        SimpleDateFormat sdf = formats[type];
        Date date = new Date(Date.parse(pubdate));
        
        return sdf.format(date);
    }
    
    /**
     * ��� ��� ��½���</br>MM�� dd�� ������ ��½�����
     * @param pubdate
     * @return
     */
    public static String getMd(String pubdate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일");
        Date date = new Date(Date.parse(pubdate));
        
        return sdf.format(date);
    }
}
