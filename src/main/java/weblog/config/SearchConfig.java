package weblog.config;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import weblog.SearchIndexServiceBean;

@Configuration
public class SearchConfig {
	Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public SearchIndexServiceBean luceneIndexServiceBean(EntityManagerFactory EntityManagerFactory){
        SearchIndexServiceBean luceneIndexServiceBean = new SearchIndexServiceBean(EntityManagerFactory);
        luceneIndexServiceBean.triggerIndexing();
        return luceneIndexServiceBean;
    }

}