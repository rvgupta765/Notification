package com.zappos.iitc.notifications.vo;

import com.zappos.iitc.notification.util.NotificationEvents;

/**
 * @author RaviGupta
 * This is a Value Object representing the entire Notification body
 * that would be input by a user while saving notification.
 */
public class NotificationVO {

    private NotificationEvents event;
    private Long transactionId;
    private Long sourceEmpId;
    private Long destinationEmpId;
    private Long srcCircleId;
    private Long destCircleId;
    private String srcNotificationText;
    private String destNotificationText;
    private Integer status;

    public NotificationEvents getEvent() {
        return event;
    }

    public void setEvent(NotificationEvents event) {
        this.event = event;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getSourceEmpId() {
        return sourceEmpId;
    }

    public void setSourceEmpId(Long sourceEmpId) {
        this.sourceEmpId = sourceEmpId;
    }

    public Long getDestinationEmpId() {
        return destinationEmpId;
    }

    public void setDestinationEmpId(Long destinationEmpId) {
        this.destinationEmpId = destinationEmpId;
    }

    public Long getSrcCircleId() {
        return srcCircleId;
    }

    public void setSrcCircleId(Long srcCircleId) {
        this.srcCircleId = srcCircleId;
    }

    public Long getDestCircleId() {
        return destCircleId;
    }

    public void setDestCircleId(Long destCircleId) {
        this.destCircleId = destCircleId;
    }

    public String getSrcNotificationText() {
        return srcNotificationText;
    }

    public void setSrcNotificationText(String srcNotificationText) {
        this.srcNotificationText = srcNotificationText;
    }

    public String getDestNotificationText() {
        return destNotificationText;
    }

    public void setDestNotificationText(String destNotificationText) {
        this.destNotificationText = destNotificationText;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotificationVO{" +
                "event=" + event +
                ", transactionId=" + transactionId +
                ", sourceEmpId=" + sourceEmpId +
                ", destinationEmpId=" + destinationEmpId +
                ", srcCircleId=" + srcCircleId +
                ", destCircleId=" + destCircleId +
                ", srcNotificationText='" + srcNotificationText + '\'' +
                ", destNotificationText='" + destNotificationText + '\'' +
                ", status=" + status +
                '}';
    }
}
