package weblog.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weblog.model.Contact;
import weblog.model.Logbook;
import weblog.model.User;

@Service
public class SearchService  {
	
	public static StandardAnalyzer analyzer = new StandardAnalyzer();

	Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EntityManager entityManager;
    
    @Autowired UserService userService;

    @Autowired
    public SearchService(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }


    public void initializeHibernateSearch() {

        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Transactional
    public List<Contact> fuzzySearch(String searchTerm){

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Contact.class).get();
        
        Query luceneQuery = qb
        		.keyword()
        			.fuzzy()
        				.withEditDistanceUpTo(1)
        				.withPrefixLength(1)
        				.onFields("callsign", "location", "name")
        				.matching(searchTerm)
        		.createQuery();
        		
        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Contact.class);

        // execute search

        List<Contact> contactList = null;
        try {
            contactList  = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            logger.warn("No result found");

        }

        return contactList;
    }
    
    @Transactional
    public List<Contact> perUserSearch(String searchTerm) throws ParseException{
    	
    	FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    	
    	// Build list of logbooks
    	List<Logbook> logbooks = (List<Logbook>) userService.getThisUserLogbookList();
    	StringBuilder sb = new StringBuilder();
    	for ( Logbook logbook : logbooks ) {
    		sb.append(logbook.getName());
    		sb.append(" ");
    	}
    	
        String[] stringQuery = { QueryParser.escape(sb.toString()), searchTerm, searchTerm, searchTerm };
    	//String[] stringQuery = { "3693, 3710", searchTerm };
    	String[] fields = { "logbook.name", "callsign", "name", "location" };
        Occur[] occ = { Occur.MUST, Occur.SHOULD, Occur.SHOULD, Occur.SHOULD };

//        Query query = new TermQuery(new Term("questionId","1"));
        Query query = MultiFieldQueryParser.parse(stringQuery, fields, occ, analyzer);


        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(query, Contact.class);
        
        logger.info(jpaQuery.toString());
        
        // execute search

        List<Contact> contactList = null;
        try {
            contactList  = jpaQuery.getResultList();
            logger.info("Query Results: " + Arrays.asList(contactList));
        } catch (NoResultException nre) {
            logger.warn("No result found");

        }

        return contactList;
    }
}