package com.rnsolutions.model;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.io.FileReader;
import au.com.bytecode.opencsv.CSVReader;

/**
 * This implementation of the entry dao leverages a CSV file in the working directory.
 * 
 * @author dseymore
 */
public class FilesystemEntryDAO implements EntryDAO {

	public Collection<YalpEntry> getEntries() {
		try {
			Set<YalpEntry> entries = new HashSet<YalpEntry>();
			CSVReader reader = new CSVReader(new FileReader("data.csv"));
			List<String[]> myEntries = (List<String[]>) reader.readAll();
			//parsing the csv file. 
			for (String[] line : myEntries) {
				YalpEntry entry = new YalpEntry();
				entry.setLocationName(line[0]);
				entry.setStreetAddress(line[1]);
				entry.setCity(line[2]);
				entry.setState(line[3]);
				entry.setZipcode(line[4]);
				entry.setCategory(FoodCategory.valueOf(line[5]));
				entries.add(entry);
			}

			return entries;
		} catch (Exception e) {
			//FIXME - add logging
		}
		//if we can't parse, give back nothing. 
		return Collections.emptySet();
	}
}
