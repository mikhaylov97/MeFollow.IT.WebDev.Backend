package com.mefollow.webschool.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MongoConfig {
    private final Logger log = LoggerFactory.getLogger(MongoConfig.class);

    private final MongoConverter mongoConverter;
    private final MongoTemplate mongoTemplate;

    public MongoConfig(MongoConverter mongoConverter, MongoTemplate mongoTemplate) {
        this.mongoConverter = mongoConverter;
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {
        log.info("Mongo InitIndicesAfterStartup init");
        final var startTime = LocalDateTime.now();

        var mappingContext = this.mongoConverter.getMappingContext();

        if (mappingContext instanceof MongoMappingContext) {
            var mongoMappingContext = (MongoMappingContext) mappingContext;

            for (BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
                var clazz = persistentEntity.getType();
                if (clazz.isAnnotationPresent(Document.class)) {
                    var indexOps = mongoTemplate.indexOps(clazz);
                    var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
                    resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
                }
            }
        }

        final var finishTime = LocalDateTime.now();
        log.info("Mongo InitIndicesAfterStartup take: {} seconds", ChronoUnit.SECONDS.between(startTime, finishTime));
    }
}
