package com.incomeCalculator.authapi.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "RESPONSES", indexes = {
        @Index(name = "idx_response_request_id", columnList = "request_id")
})
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    private Request request;

    private short status;

    private String statusText;

    public Response(Long id, Request request, short status, String statusText) {
        this.id = id;
        this.request = request;
        this.status = status;
        this.statusText = statusText;
    }

    public Response(Request request, short status, String statusText) {
        this.request = request;
        this.status = status;
        this.statusText = statusText;
    }

    public Response() {

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

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return status == response.status && Objects.equals(id, response.id) && Objects.equals(request, response.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, request, status);
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", request=" + request +
                ", status=" + status +
                '}';
    }

}