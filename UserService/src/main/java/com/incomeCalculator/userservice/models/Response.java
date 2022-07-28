package com.incomeCalculator.userservice.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "RESPONSES", indexes = {
        @Index(name = "idx_response_request_id", columnList = "request_id")
})
public class Response {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Request request;

    private int status;

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

    public Response(Request request) {
        this.request = request;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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