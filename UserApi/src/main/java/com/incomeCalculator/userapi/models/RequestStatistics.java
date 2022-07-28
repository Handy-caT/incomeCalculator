package com.incomeCalculator.userapi.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "request_statistics")
public class RequestStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long requestCount;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    public RequestStatistics(Long id, Long requestCount, LocalDateTime createDateTime) {
        this.id = id;
        this.requestCount = requestCount;
        this.createDateTime = createDateTime;
    }

    public RequestStatistics() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestStatistics)) return false;
        RequestStatistics that = (RequestStatistics) o;
        return Objects.equals(id, that.id) && Objects.equals(requestCount, that.requestCount) && Objects.equals(createDateTime, that.createDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestCount, createDateTime);
    }

    @Override
    public String toString() {
        return "RequestStatistics{" +
                "id=" + id +
                ", requestCount=" + requestCount +
                ", createDateTime=" + createDateTime +
                '}';
    }

}