package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.QueryResult;

public interface NL2SQLService {

    QueryResult processNaturalLanguageQuery(String naturalLanguageQuery);


}
