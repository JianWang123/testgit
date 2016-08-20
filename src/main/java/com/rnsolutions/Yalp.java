package com.rnsolutions;

import com.rnsolutions.model.YalpEntry;
import java.util.Collection;

/**
 * This interface describes the functionality yalp performs
 * @author dseymore
 */
public interface Yalp {

	public Collection<YalpEntry> filterEntries(String criteria);
	public void initializeIndex();
}
