package com.dotcms.notifications.business;

import java.util.Collection;
import java.util.List;

import com.dotcms.notifications.dto.NotificationDTO;
import com.dotmarketing.exception.DotDataException;
import com.liferay.portal.model.User;

/**
 * This data access object provides access to information associated to
 * notifications. A notification can be generated by any component or
 * third-party code in the application so that a message can be displayed in the
 * browser for users to see it.
 * 
 * @author Daniel Silva
 * @version 3.0, 3.7
 * @since Feb 3, 2014
 *
 */
public abstract class NotificationFactory {

	/**
	 * Saves a Notification to the database.
	 * 
	 * @param notification
	 *            - The {@link NotificationDTO} object to save.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract void saveNotification(final NotificationDTO notification) throws DotDataException;

	/**
	 * Saves a Notification for each user in the given users collection
	 *
	 * @param notificationTemplate Will be used as a Notification template in order to create the notification record
	 *                             for each user on the list
	 * @param users                For each element on this list a Notification record will be created
	 * @throws DotDataException
	 */
	public abstract void saveNotificationsForUsers(final NotificationDTO notificationTemplate, Collection<User> users) throws DotDataException;

	/**
	 * Returns a notification based on its ID.
	 * 
	 * @param notificationId
	 *            - The ID of the notification.
	 * @return The {@link NotificationDTO} object.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract NotificationDTO findNotification(final String notificationId) throws DotDataException;

	/**
	 * Deletes a notification based on its ID.
	 * 
	 * @param notificationId
	 *            - The ID of the notification.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract void deleteNotification(final String notificationId) throws DotDataException;

	/**
	 * Deletes all the notifications associated to a specific user ID.
	 * 
	 * @param userId
	 *            - The ID of the user.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract void deleteNotifications(final String userId) throws DotDataException;

	/**
	 * Deletes all the notification in the array.
	 * @param notificationsId {@link String} array
	 * @throws DotDataException
     */
	public abstract void deleteNotification(String[] notificationsId) throws DotDataException;

	/**
	 * Returns a paginated result of all the notifications according to the
	 * specified filters.
	 * 
	 * @param offset
	 *            - The row number to read notifications from.
	 * @param limit
	 *            - The limit of rows to include in the result.
	 * @return The list of {@link NotificationDTO} objects in the paginated
	 *         result.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract List<NotificationDTO> getNotifications(final long offset, final long limit) throws DotDataException;

	/**
	 * Returns all notifications associated to a user ID.
	 * 
	 * @param userId
	 *            - The ID of the user.
	 * @return The list of {@link NotificationDTO} objects associated to a user.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract List<NotificationDTO> getAllNotifications(final String userId) throws DotDataException;

	/**
	 * Returns the number of notifications associated to a specific user ID.
	 * 
	 * @param userId
	 *            - The ID of the user.
	 * @return The number of notifications for a user.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract Long getNotificationsCount(final String userId) throws DotDataException;

	/**
	 * Returns a paginated result of all notifications associated to a user ID.
	 * 
	 * @param userId
	 *            - The ID of the user.
	 * @param offset
	 *            - The row number to read notifications from.
	 * @param limit
	 *            - The limit of rows to include in the result.
	 * @return The list of paginated {@link NotificationDTO} objects associated to
	 *         a user.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract List<NotificationDTO> getNotifications(final String userId, final long offset, final long limit)
			throws DotDataException;

	/**
	 * Returns the number of new notifications for a specific user ID.
	 * 
	 * @param userId
	 *            - The ID of the user.
	 * @return The number of new notifications.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract Long getNewNotificationsCount(final String userId) throws DotDataException;

	/**
	 * Marks all the notifications of a user as "read".
	 * 
	 * @param userId
	 *            - The ID of the user.
	 * @throws DotDataException
	 *             An error occurred when executing the SQL query. Please check
	 *             your database and/or query syntax.
	 */
	public abstract void markNotificationsAsRead(final String userId) throws DotDataException;

}
