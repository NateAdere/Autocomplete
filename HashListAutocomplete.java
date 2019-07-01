//Natnael Adere na147
import java.util.*;

public class HashListAutocomplete implements Autocompletor {

private static final int MAX_PREFIX = 10;
private Map<String, List<Term>> myMap;
private int mySize;

public HashListAutocomplete(String[] terms, double[] weights) {
	mySize = 0;
	if (terms == null || weights == null) {
		throw new NullPointerException();
	}
	initialize(terms,weights);
}

@Override
/**
 * Returns the top k matching terms in descending order of weight. If there
 * are fewer than k matches, return all matching terms in descending order
 * of weight. If there are no matches, return an empty list.
 * @param prefix that is being searched for 
 */
public List<Term> topMatches(String prefix, int k) {

	List <Term> ans = new ArrayList<Term>();
	if(prefix.length() > MAX_PREFIX) {
		prefix = prefix.substring(0,MAX_PREFIX);
	}
	if(myMap.containsKey(prefix)) {
		List<Term> all = myMap.get(prefix);
		List<Term> list = all.subList(0,Math.min(k,all.size()));
		ans = list;
	}
	return ans;
}

@Override
/**
 * Create internal state needed to store Term objects
 * from the parameters. Should be called in implementing
 * constructors
 * @param terms is array of Strings for words in each Term
 * @param weights is corresponding weight for word in terms
 */
public void initialize(String[] terms, double[] weights) {
if (myMap != null) myMap.clear();
else myMap = new HashMap<>();


	for(int k = 0; k < terms.length; k++) { 
		Term myTerm = new Term(terms[k], weights[k]);
	
		for(int i = 0; i <= Math.min(MAX_PREFIX, terms[k].length()); i++) {
			String s = terms[k].substring(0,i);
			if(!myMap.containsKey(s)){
				myMap.put(s,  new ArrayList<Term>());
			}
			myMap.get(s).add(myTerm);

		}
	}


	Set<String> a = myMap.keySet();
	for(String k : a) {
	Collections.sort(myMap.get(k), new Term.ReverseWeightOrder());
	}
}

@Override
/**
 * Return size in bytes of all Strings and doubles
 * stored in this class. To the extent that
 * other types are used for efficiency, there size should
 * be included too 
 * @return number of bytes used after initialization
 */
public int sizeInBytes() {
	Set<String> a = myMap.keySet();
	for (String k : a) {
		mySize +=  BYTES_PER_CHAR*k.length();
		List <Term> b = myMap.get(k);
		for (Term i : b) {
			mySize += BYTES_PER_DOUBLE + BYTES_PER_CHAR*i.getWord().length();
		}
	}
	return mySize;
}
}
