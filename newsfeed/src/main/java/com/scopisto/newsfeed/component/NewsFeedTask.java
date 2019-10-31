package com.scopisto.newsfeed.component;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.scopisto.newsfeed.repository.NewsFeed;
import com.scopisto.newsfeed.repository.NewsFeedRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsFeedTask {
    private final static Logger logger = LogManager.getLogger(NewsFeedTask.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Value("${feed.url}")
    private String url;

    RestTemplate restTemplate = new RestTemplateBuilder().build();

    private Date lastPublishedDate = null;

    @Autowired
    NewsFeedRepository newsFeedRepository;

    @Scheduled(cron = "${batch.cron}")
    public void scheduleTaskWithCronExpression() {

        SyndFeed syndFeed = restTemplate.execute(url, HttpMethod.GET, null, response -> {
            SyndFeedInput input = new SyndFeedInput();
            try {
                return input.build(new XmlReader(response.getBody()));
            } catch (FeedException e) {
                throw new IOException("Could not parse response", e);
            }
        });


        List<SyndEntry> entries = syndFeed.getEntries();
        if (entries != null) {
            List<SyndEntry> filteredList = entries.stream().filter(entry -> {
                if (lastPublishedDate == null) {
                    return true;
                }
                return lastPublishedDate.before(entry.getPublishedDate());

            }).collect(Collectors.toList());

            logger.info("Found " + entries.size() + " entries");
            logger.info(filteredList.size() + " entries will be updated in DB");
            filteredList.stream().forEach(entry -> {
                if (lastPublishedDate == null || lastPublishedDate.before(entry.getPublishedDate())) {
                    lastPublishedDate = entry.getPublishedDate();
                }
                saveNewsFeed(entry);
            });
        }
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }
    
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
    private void saveNewsFeed(SyndEntry entry) {
        NewsFeed newsFeed = new NewsFeed();
        newsFeed.setTitle(entry.getTitle());
        newsFeed.setDescription(entry.getDescription().getValue());
        newsFeed.setPublicationDate(entry.getPublishedDate());
        String imageUrl = entry.getEnclosures().get(0).getUrl();
        newsFeed.setImageUrl(imageUrl);
        byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
        newsFeed.setImage(imageBytes);

        //NOTE: code block to test if the image can be created from the provided byte[]
        //        try {
        //            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        //            BufferedImage bImage = ImageIO.read(bis);
        //            ImageIO.write(bImage, "jpg", new File("output.jpg"));
        //            System.out.println("image created");
        //        } catch (Exception e) {
        //            
        //        }

        newsFeedRepository.save(newsFeed);
    }
}