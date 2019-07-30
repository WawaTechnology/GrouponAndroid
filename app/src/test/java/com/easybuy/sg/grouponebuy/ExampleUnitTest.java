package com.easybuy.sg.grouponebuy;

import android.support.v4.widget.TextViewCompat;

import com.easybuy.sg.grouponebuy.model.Product;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    Product product1;
    Product product2;


    @Before
    public void setUp() throws Exception {

        product1=new Product();
        product2=new Product();
        product1.setId("213k");
        product1.setNameEn("grapes");
        product1.setTotalNumber(3);
        product2.setId("213k");
        product2.setNameEn("grapes");
        product2.setTotalNumber(4);




    }
    @Test
    public void checkModified()
    {
        boolean res=product1.isModified(product2);
        assertTrue(res);
    }
    @Test
    public void checkEqual()
    {
        boolean res= product1.equals(product2);
        assertTrue(res);
    }
    @Test
    public void checkString()
    {
        String a="sam";
        String b="sam";
        String c=b;
        b="ram";

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);



    }
    @Test
    public void printFactorial()
    {
        int i=1;
        int f=1;
        while(i<=5)
        {
            f=f*i;
            i+=1;
        }
        System.out.println(f) ;
    }
    @Test
    public void reverseString()
    {
        String s="samp";
     char ch[]=s.toCharArray();
     int n=s.length();
     for(int i=0;i<s.length()/2;i++)
     {
         char temp=ch[i];
         ch[i]=ch[n-1-i];
         ch[n-1-i]=temp;

     }
     System.out.println(String.valueOf(ch));
    }


}