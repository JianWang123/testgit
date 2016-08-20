package com.rnsolutions;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import com.rnsolutions.model.YalpEntry;

/**
 * This is the Yalp runner. 
 *
 */
public class App {

	public static void main(String[] args) {
		//we need a classloader referene
		XmlBeanFactory bf = new XmlBeanFactory(new ClassPathResource("com/rnsolutions/applicationContext.xml"));
		Yalp yalp = (Yalp) bf.getBean("yalp");


// An example call that just filters by the word Food, and outputs what matches. 
	for(YalpEntry entry : yalp.filterEntries("McDonalds")){
		System.out.println(entry.getState());
	}

		//FIXME - implement filtering using a supplied argument

		//FIXME - sort results

		//FIXME - output the results

	}
}
