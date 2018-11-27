package com.example.unsan.grouponebuy;

import android.support.v4.widget.TextViewCompat;

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


    public class Node {
        int x;
        Node next;

        Node(int data) {
            x = data;
            next = null;
        }
    }

    Node head;
    Node tail;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addLinkedList(int x) {
        if (head == null) {
            Node node = new Node(x);
            head = node;
            tail = node;

        } else {
            Node node = new Node(x);
            tail.next = node;
            tail = node;

        }
    }

    @Test public boolean detectLoop()
    {
        Set<Node> nodeSet=new HashSet<>();
        while(head!=null)
        {
            if(nodeSet.contains(head))
            {
                return true;
            }
            else
            {
                nodeSet.add(head);
                head=head.next;
            }
        }
        return false;

    }

    @Test
    public void removeInLinkedList(int data) {
        if (head == null) {
            return;
        } else if (head.x == data) {
            Node node2Del = head;
            head = head.next;
            node2Del.next = null;
        } else {
            Node temp = head;
            while (temp.next != null && temp.next.x != data) {
                temp = temp.next;
            }

            if (temp == tail && temp.x != data) {
                return;
            } else {
                Node node2Del = temp.next;
                //check if it is tail
                if (node2Del == tail) {

                    tail = temp;
                } else {
                    temp.next = temp.next.next;
                }
                node2Del.next = null;
            }
        }
    }
    @Test
    public boolean checkLoop()
    {
        Node slow_pointer=head;
        Node fast_pointer=head;
        while(slow_pointer!=null&&fast_pointer!=null&&fast_pointer.next!=null)
        {
            slow_pointer=slow_pointer.next;
            fast_pointer=fast_pointer.next.next;
            if(slow_pointer==fast_pointer)
            {
                return true;
            }
        }
        return false;
    }


}