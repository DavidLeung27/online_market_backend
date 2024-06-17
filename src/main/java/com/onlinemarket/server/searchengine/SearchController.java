package com.onlinemarket.server.searchengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.onlinemarket.server.result.Result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public Result SearchInSearchBar(@RequestParam String keyword) {
        return searchService.SearchInSearchBar(keyword);
    }

}
