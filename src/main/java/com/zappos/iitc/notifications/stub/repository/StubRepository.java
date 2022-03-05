package com.zappos.iitc.notifications.stub.repository;

import com.zappos.iitc.notifications.stub.domain.StubResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author RaviGupta
 * This repository is used for obtaining dummy data
 */
@Qualifier("stubRepo")
public interface StubRepository extends JpaRepository<StubResponse, String>{

    @Query(value = "select s from StubResponse s where s.token = :token and s.path= :path")
    StubResponse getByIdAndPath(@Param("token") String token, @Param("path") String path);

}
