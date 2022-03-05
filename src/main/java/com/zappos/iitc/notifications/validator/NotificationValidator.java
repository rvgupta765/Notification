package com.zappos.iitc.notifications.validator;

import com.zappos.iitc.notification.util.NotificationEvents;
import com.zappos.iitc.notifications.vo.NotificationVO;
import org.apache.commons.lang3.StringUtils;

/**
 * @author RaviGupta
 * This validator is meant for validate NotificationRequest parameters.
 */
public class NotificationValidator {

    /**
     * This method is used for validating whether the required params were passed in by the user for the create request
     *
     * @param xAuthToken
     * @param notificationVO
     * @param errMsg
     * @return
     */
    public static boolean validateMandatoryParamsToSaveNotification(String xAuthToken, NotificationVO notificationVO, StringBuilder errMsg) {
        errMsg.append("{\"errorMessage\":\" Following mandatory notification params are found to be empty: ");
        boolean errFlag = false;

        if (StringUtils.isEmpty(xAuthToken)) {
            errMsg.append("x-auth-token (Header Param), ");
            errFlag = true;
        }

        if (notificationVO.getEvent() == null) {
            errMsg.append("event, ");
            errFlag = true;
        }
        if (notificationVO.getSrcNotificationText() == null || notificationVO.getSrcNotificationText().isEmpty()) {
            errMsg.append("srcNotificationText, ");
            errFlag = true;
        }

        if (notificationVO.getDestNotificationText() == null || notificationVO.getDestNotificationText().isEmpty()) {
            errMsg.append("destNotificationText, ");
            errFlag = true;
        }

        if (notificationVO.getStatus() == null) {
                errMsg.append("status, ");
                errFlag = true;
            }

        errMsg.append(" Please provide values for them.\"}");
        return errFlag;
    }
}
