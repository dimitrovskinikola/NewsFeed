package com.scopisto.newsfeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NewsFeedRepository
        extends JpaRepository<NewsFeed, Long> {

}
