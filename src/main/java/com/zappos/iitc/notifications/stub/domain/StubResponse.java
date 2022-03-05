package com.zappos.iitc.notifications.stub.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author RaviGupta
 * This entity class Model representing a stubbedResponse for account mgmt api.
 */
@Entity
@Table(name = "STUBRESPONSE")
public class StubResponse<T> {

    @Id
    @Column(name="X_AUTH_TOKEN")
    private String token;

    @Column(name="PATH")
    private String path;

    @Column(name = "RESPONSE")
    private String response;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
