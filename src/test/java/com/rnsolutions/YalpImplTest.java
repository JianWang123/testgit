/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rnsolutions;

import com.rnsolutions.model.EntryDAO;
import com.rnsolutions.model.FoodCategory;
import com.rnsolutions.model.YalpEntry;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.springframework.util.CollectionUtils;

/**
 * This unit test confirms the functionality of the yalp filtering mechanism
 * @author dseymore
 */
public class YalpImplTest extends TestCase {

	public YalpImplTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * What happens when the database has nothing in it?
	 */
	public void testFilterEntriesNull() {
		YalpImpl instance = new YalpImpl();
		//now lets mock the data
		EntryDAO entryDAO = EasyMock.createMock(EntryDAO.class);
		EasyMock.expect(entryDAO.getEntries()).andReturn(null);
		EasyMock.replay(entryDAO);

		instance.setEntryDAO(entryDAO);
		instance.initializeIndex();

		Collection<YalpEntry> entries = instance.filterEntries("Something");

		assertTrue(CollectionUtils.isEmpty(entries));
	}

	/**
	 * What happens when we have 1 record, that is cased differently. 
	 */
	public void testFilterEntriesSimpleDifferentCase() {
		YalpImpl instance = bootstratpWithData();
		Collection<YalpEntry> entries = instance.filterEntries("mcdonalds");
		assertTrue(entries.size() == 1);
		for(YalpEntry one: entries){
			assertEquals(one.getLocationName(),"McDonalds");
		}
	}

	/**
	 * 2 records with the same case
	 */
	public void testFilterEntriesSimpleSameCase() {
		YalpImpl instance = bootstratpWithData();
		Collection<YalpEntry> entries = instance.filterEntries("Rockville");
		assertTrue(entries.size() == 2);
		for(YalpEntry dude: entries){
			assertEquals(dude.getCity(),"Rockville");
		}
	}

	public void testFilterEntriesNothing(){
		YalpImpl instance = bootstratpWithData();
		Collection<YalpEntry> entries = instance.filterEntries("Jerry's Sub & Pizza");
		assertTrue(entries.size() == 0);
	}

	public YalpImpl bootstratpWithData() {
		YalpImpl instance = new YalpImpl();
		//now lets mock the data
		EntryDAO entryDAO = EasyMock.createMock(EntryDAO.class);
		//a collection to be returned
		EasyMock.expect(entryDAO.getEntries()).andReturn(someTestData());
		EasyMock.replay(entryDAO);
		instance.setEntryDAO(entryDAO);
		instance.initializeIndex();
		return instance;
	}

	public Collection<YalpEntry> someTestData() {
		Collection<YalpEntry> coll = new ArrayList<YalpEntry>();
		{
			YalpEntry entry = new YalpEntry();
			entry.setLocationName("McDonalds");
			entry.setCity("Rockville");
			entry.setCategory(FoodCategory.FAST);
			coll.add(entry);
		}
		{
			YalpEntry entry = new YalpEntry();
			entry.setLocationName("Yuan Fu");
			entry.setCity("Rockville");
			entry.setCategory(FoodCategory.VEGETARIAN);
			coll.add(entry);
		}
		return coll;
	}
}
