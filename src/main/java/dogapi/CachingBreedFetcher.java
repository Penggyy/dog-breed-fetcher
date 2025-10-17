package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final BreedFetcher fetcher;
    private final HashMap<String, List<String>> dict = new HashMap<>();
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException {
        if (!(dict.containsKey(breed))) {
            try {
                List<String> breeds = fetcher.getSubBreeds(breed);
                dict.put(breed, breeds);
                callsMade++;
            } catch (BreedFetcher.BreedNotFoundException e) {
                callsMade++;
                throw new BreedFetcher.BreedNotFoundException(breed);
            }
        }
        return dict.get(breed);
    }

    public int getCallsMade() {
        return callsMade;
    }
}