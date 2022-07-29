package com.incomeCalculator.userapi.models;

import com.incomeCalculator.userservice.models.Request;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "request_statistics")
public class RequestStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long requestCount;

    private Long cardCount;

    private Long userCount;

    @OneToOne
    private Request lastRequest;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    public RequestStatistics(Long id, Long requestCount, Long cardCount, Long userCount, Request lastRequest, LocalDateTime createDateTime) {
        this.id = id;
        this.requestCount = requestCount;
        this.cardCount = cardCount;
        this.userCount = userCount;
        this.lastRequest = lastRequest;
        this.createDateTime = createDateTime;
    }

    public RequestStatistics(Long id, Long requestCount, LocalDateTime createDateTime) {
        this.id = id;
        this.requestCount = requestCount;
        this.createDateTime = createDateTime;
    }

    public RequestStatistics(Long requestCount) {
        this.requestCount = requestCount;
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

    public Long getCardCount() {
        return cardCount;
    }

    public void setCardCount(Long cardCount) {
        this.cardCount = cardCount;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Request getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Request lastRequest) {
        this.lastRequest = lastRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestStatistics)) return false;
        RequestStatistics that = (RequestStatistics) o;
        return Objects.equals(id, that.id) && Objects.equals(requestCount, that.requestCount) && Objects.equals(cardCount, that.cardCount) && Objects.equals(userCount, that.userCount) && Objects.equals(lastRequest, that.lastRequest) && Objects.equals(createDateTime, that.createDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestCount, cardCount, userCount, lastRequest, createDateTime);
    }

    @Override
    public String toString() {
        return "RequestStatistics{" +
                "id=" + id +
                ", requestCount=" + requestCount +
                ", cardCount=" + cardCount +
                ", userCount=" + userCount +
                ", lastRequest=" + lastRequest +
                ", createDateTime=" + createDateTime +
                '}';
    }

}