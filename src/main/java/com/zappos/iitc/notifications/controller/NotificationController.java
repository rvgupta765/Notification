package com.zappos.iitc.notifications.controller;

import com.zappos.iitc.commons.account.domain.Employee;
import com.zappos.iitc.notification.domain.notification.Notification;
import com.zappos.iitc.notification.exception.NotificationMgmtException;
import com.zappos.iitc.notification.service.NotificationService;
import com.zappos.iitc.notification.util.NotificationEvents;
import com.zappos.iitc.notifications.validator.NotificationValidator;
import com.zappos.iitc.notifications.vo.NotificationVO;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author RaviGupta
 * This controller is meant for performing CRUD operations over the employee notifications.
 */
@RestController
@Api(description = "Operations pertaining to Employee Notifications ", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController {

    private static Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    NotificationService notificationService;

    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String RESPONSE_MESSAGE = "responseMessage";

    /**
     * This method is used to list out all the notifications for an employee.
     *
     * @param circleId
     * @param offset
     * @param size
     * @return
     */
    @ApiOperation(value = "View a list of notifications.", response = Notification.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Token is not authenticated."),
            @ApiResponse(code = 404, message = "No notifications found.")
    })
    @RequestMapping(value = "/notifications", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getEmployeeNotification(
            @ApiParam(value = "x-auth-token to be provided in header. Ex- \"2CIR.8MYPROD.3SREQ\"", required = true) @RequestHeader(value = "x-auth-token") String xAuthToken,
            @ApiIgnore @RequestAttribute(value = "notificableEmployee") Employee user,
            @ApiParam(value = "offset 'i' to fetch the requests starting ith index") @RequestParam(value = "offset", required = false) Integer offset,
            @ApiParam(value = "size 's' to fetch the 's' number of requests starting ith index") @RequestParam(value = "size", required = false) Integer size,
            @ApiParam(value = "Employee circle's identifier to fetch all notifications under it") @RequestParam(value = "circleId", required = true) Long circleId) {
        Page<Notification> pagedResponse = null;
        try {
            if (offset == null)
                offset = 0;
            if (size == null)
                size = 40;

            if (user != null && user.getEmpId() != null)
                pagedResponse = notificationService.getNotifications(user, circleId, offset, size);
            else {

                logger.debug("No notification mapped for NotificationRequest:: offset=" + offset + "& size=" + size + "& circleId=" + circleId);
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No notification mapped for NotificationRequest:: offset=" + offset + "& size=" + size + "& circleId=" + circleId);
            }
            if (pagedResponse.getContent() == null || pagedResponse.getContent().isEmpty()) {
                logger.debug("No notification mapped for NotificationRequest:: offset=" + offset + "& size=" + size + "& circleId=" + circleId);
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No notification mapped for NotificationRequest:: offset=" + offset + "& size=" + size + "& circleId=" + circleId);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getMessage());
        } catch (Exception ex1) {
            logger.error("Error occurred while fetching Notification Requests.", ex1);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching Notification Requests. " + ex1.getMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("x-total-size", pagedResponse.getTotalElements());
        response.put("notifications", pagedResponse.getContent());

        /*Resources<Notification> requestResource = new Resources(pagedResponse.getContent());
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        requestResource.add(new Link(uriString, "self"));*/
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-total-size", String.valueOf(pagedResponse.getTotalElements()));
        headers.add("cache-control", "no-transform;must-revalidate;max-age=86400;smax-age=86400");
        return ResponseEntity.ok().headers(headers).body(response);
        //return new ResponseEntity<Resources<Notification>>(requestResource, headers, HttpStatus.OK);
    }

    /**
     * This method is used to get details of Notification.
     *
     * @param xAuthToken
     * @param id
     * @return
     */
    @ApiOperation(value = "View details of notification.", response = Notification.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Token is not authenticated."),
            @ApiResponse(code = 404, message = "No notification found.")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Resource<Notification>> getEmployeeNotificationById(
            @ApiParam(value = "x-auth-token to be provided in header. Ex- \"2CIR.2MYPROD.8SREQ\"", required = true) @RequestHeader(value = "x-auth-token") String xAuthToken,
            @ApiIgnore @RequestAttribute(value = "notificableEmployee") Employee emp,
            @PathVariable int id) {
        Notification response = null;
        try {

            if (emp != null && emp.getEmpId() != null && emp.getEmpId() != 0L) {
                response = notificationService.getNotificationById(id);
            }

            if (response == null) {
                logger.debug("No notification mapped for NotificationId: " + id);
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No notification mapped for NotificationId: " + id);
            }
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getMessage());
        } catch (Exception ex1) {
            logger.error("Error occurred while fetching Notification Details.", ex1);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching Notification Details. " + ex1.getMessage());
        }

        Resource<Notification> notificationResource = new Resource(response);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        notificationResource.add(new Link(uriString, "self"));
        return new ResponseEntity<Resource<Notification>>(notificationResource, HttpStatus.OK);
    }

    /**
     * This method is responsible for saving a notification
     *
     * @param xAuthToken
     * @param notificationVO
     * @return
     */
    @ApiOperation(value = "Saves an Event Notification.")
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Token is not authenticated.")
    })
    @RequestMapping(value = "/saveNotification", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Resource<Notification>> saveNotification(
            @ApiParam(value = "x-auth-token to be provided in header. Ex- \"2CIR.2MYPROD.8SREQ\"", required = true) @RequestHeader(value = "x-auth-token") String xAuthToken,
            @ApiIgnore @RequestAttribute(value = "notificableEmployee") Employee emp,
            @RequestBody NotificationVO notificationVO) {

        Notification notification = null;
        JSONObject jsonResponse = new JSONObject();
        try {
            StringBuilder errMsg = new StringBuilder();
            boolean isInvalid = NotificationValidator.validateMandatoryParamsToSaveNotification(xAuthToken, notificationVO, errMsg);
            if (isInvalid == true) {
                logger.error(errMsg.toString());
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, errMsg.toString());
            }

            if (emp != null) {
                notification = notificationService.saveNotification(notificationVO.getEvent(), notificationVO.getTransactionId(), notificationVO.getSourceEmpId(), notificationVO.getDestinationEmpId(), notificationVO.getSrcCircleId(), notificationVO.getDestCircleId(), notificationVO.getSrcNotificationText(), notificationVO.getDestNotificationText(), notificationVO.getStatus());
            }

            Resource<Notification> notificationResource = new Resource(notification);
            final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
            notificationResource.add(new Link(uriString, "self"));
            return ResponseEntity.created(linkTo(methodOn(NotificationController.class).saveNotification(null, null, null)).toUri()).build();
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getMessage());
        } catch (Exception ex1) {
            logger.error("Error occurred while saving Notification.", ex1);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while saving Notification. " + ex1.getMessage());
        }
    }
}