package com.incomeCalculator.authapi.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "REQUEST_DESTINATIONS", indexes = {
        @Index(name = "idx_requestdestination", columnList = "apiName"),
        @Index(name = "idx_requestdestination_type", columnList = "requestType")
})
public class RequestDestination {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Request request;

    private String apiName;

    private String requestType;

    public RequestDestination(Long id, Request request, String apiName, String requestType) {
        this.id = id;
        this.request = request;
        this.apiName = apiName;
        this.requestType = requestType;
    }

    public RequestDestination(Request request, String apiName, String requestType) {
        this.request = request;
        this.apiName = apiName;
        this.requestType = requestType;
    }

    public RequestDestination(Request request) {
        this.request = request;
    }

    public RequestDestination() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestDestination)) return false;
        RequestDestination that = (RequestDestination) o;
        return Objects.equals(id, that.id) && Objects.equals(request, that.request) && Objects.equals(apiName, that.apiName) && Objects.equals(requestType, that.requestType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, request, apiName, requestType);
    }

    @Override
    public String toString() {
        return "RequestDestination{" +
                "id=" + id +
                ", request=" + request +
                ", apiName='" + apiName + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
    
}