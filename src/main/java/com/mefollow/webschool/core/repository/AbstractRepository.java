package com.mefollow.webschool.core.repository;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbstractRepository<T extends BaseModel> extends ReactiveMongoRepository<T, String> {
}
