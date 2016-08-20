package com.rnsolutions;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.rnsolutions.model.EntryDAO;
import com.rnsolutions.model.YalpEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Implementation of the yalp proprietary indexing service. 
 * @author dseymore
 */
public class YalpImpl implements Yalp {

	/**
	 * Index names for our lucene document
	 */
	public enum Index {

		CITY, NAME, CATEGORY, OBJECT;
	}
	private EntryDAO entryDAO;
	private Directory directory;
	private List<YalpEntry> cache;

	Logger logger = Logger.getAnonymousLogger();
	
	public void setEntryDAO(EntryDAO dao) {
		this.entryDAO = dao;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * When Yalp starts up, we want to index our data so that we can filter our results quickly, and accurately. 
	 */
	public void initializeIndex() {
		if (directory == null) {
			//default to a ramdirectory if no one configured me. 
			directory = new RAMDirectory();
		}
		//we need to have a lookup mechanism for the hits we get
		cache = new ArrayList<YalpEntry>();
		//this is gonna be costly, but, lets get the entries..
		Collection<YalpEntry> allData = entryDAO.getEntries();
		
		
		
		
		//index them
		try {
			//For lucene index
			Analyzer analyzer = new WhitespaceAnalyzer();
			MaxFieldLength mfl = new IndexWriter.MaxFieldLength(120);
			IndexWriter iwriter = new IndexWriter(directory, analyzer, mfl);
			for (YalpEntry entry : allData) {
				Document doc = new Document();
				doc.add(new Field(Index.CITY.name(), entry.getCity(), Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field(Index.NAME.name(), entry.getLocationName(), Field.Store.YES, Field.Index.ANALYZED));
				doc.add(new Field(Index.CATEGORY.name(), entry.getCategory().name(), Field.Store.YES, Field.Index.ANALYZED));
				//attach our key to the document so we can lookup after discovery. 
				doc.add(new Field(Index.OBJECT.name(), String.valueOf(cache.size()), Field.Store.YES, Field.Index.NOT_ANALYZED));
				cache.add(entry);
				iwriter.addDocument(doc);
			}
			iwriter.optimize();
			iwriter.close();
		} catch (Exception e) {
			//FIXME - add logging
			logger.log(Level.INFO, "catch an exception.", e);
		}
	}
	
	/**
	 * Using the index created in the initialization method, lets query for matching values. 
	 * @param criteria
	 * @return
	 */
	public Collection<YalpEntry> filterEntries(String criteria) {
		//FIXME - make this case insensitive
		Collection<YalpEntry> results_1 = new HashSet<YalpEntry>();
		Collection<YalpEntry> results = Collections.unmodifiableCollection(results_1);
		
		if (criteria != null) {
			try {
				Analyzer analyzer = new WhitespaceAnalyzer();
				// Now search the index:
				IndexSearcher isearcher = new IndexSearcher(directory);
				// Parse a simple query that searches for "text":
				QueryParser parser = new MultiFieldQueryParser(new String[]{Index.CATEGORY.name(), Index.CITY.name(), Index.NAME.name()}, analyzer);
				Query query = parser.parse(criteria);
				TopDocCollector collector = new TopDocCollector(500);
				isearcher.search(query, collector);
				// Iterate through the results:
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				for (ScoreDoc hit : hits) {
					Document document = isearcher.doc(hit.doc);
					YalpEntry entry = cache.get(Integer.valueOf(document.get(Index.OBJECT.name())));
					results.add(entry);
				}
				isearcher.close();
			} catch (Exception e) {
				//FIXME - add logging
				logger.log(Level.INFO, "catch an exception ", e);
			}
		}
		//FIXME - make the results immutable.
		return results;
	}
}
