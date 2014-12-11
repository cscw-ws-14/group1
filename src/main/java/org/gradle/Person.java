package org.gradle;

import org.apache.commons.collections.list.GrowthList;

import com.jpmorrsn.fbp.examples.networks.*;

public class Person {
    private String name;
    
    public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
    	
//		System.out.println("hello world");
    	
//		CopyFileToCons.main(null);
//		Telegram.main(null);
//    	Deadlock.main(null);
//    	NoDeadlock.main(null);
		
	}
    
    public Person(String name) {
        this.name = name;
        new GrowthList();
    }

    public String getName() {
        return name;
    }
}
