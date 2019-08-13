package weblog;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
 
import javax.persistence.EntityManagerFactory;
 
public class SearchIndexServiceBean {
 
    private FullTextEntityManager fullTextEntityManager;
 
    public SearchIndexServiceBean(EntityManagerFactory entityManagerFactory){
        fullTextEntityManager = Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
    }
 
    public void triggerIndexing() {
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}