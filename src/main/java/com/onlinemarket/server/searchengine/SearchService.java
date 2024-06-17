package com.onlinemarket.server.searchengine;

import java.util.List;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onlinemarket.server.product.Product;
import com.onlinemarket.server.result.Result;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Component
public class SearchService {

        @PersistenceContext
        private EntityManager entityManager;

        public Result SearchInSearchBar(String keyword) {
                SearchSession searchSession = Search.session(entityManager);

                List<String> searchResult = searchSession.search(Product.class)
                                .select(f -> f.field("productName", String.class))
                                .where(f -> f.bool()
                                                .should(f.wildcard()
                                                                .field("productName")
                                                                .matching(keyword + "*"))
                                                .should(f.match()
                                                                .field("productName")
                                                                .matching(keyword)
                                                                .fuzzy(2)))
                                .sort(f -> f.score().desc())
                                .fetchHits(5);
                System.out.println(searchResult.size());
                return new Result(1, "Search success", searchResult);

        }

}
