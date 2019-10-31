package com.scopisto.newsfeed.repository;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NEWSFEED")
@Data
public class NewsFeed {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;


    @Column(name = "publication_date")
    private Date publicationDate;


    @Column(name = "image_url")
    private String imageUrl;

    @Lob
    @Column(name = "image")
    private byte[] image;

}
