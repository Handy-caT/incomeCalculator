package com.incomeCalculator.authapi.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "request_sources")
public class RequestSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    private Request request;

    private String sourceIp;

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

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestSource)) return false;
        RequestSource that = (RequestSource) o;
        return Objects.equals(id, that.id) && Objects.equals(request, that.request) && Objects.equals(sourceIp, that.sourceIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, request, sourceIp);
    }

    @Override
    public String toString() {
        return "RequestSource{" +
                "id=" + id +
                ", request=" + request +
                ", sourceIp='" + sourceIp + '\'' +
                '}';
    }

}