package com.dotcms.api.system.event;

import com.dotcms.exception.BaseInternationalizationException;
import com.dotcms.exception.BaseRuntimeInternationalizationException;
import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotcms.repackage.javax.validation.*;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.PermissionAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.rules.conditionlet.Conditionlet;
import com.liferay.portal.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * This Util class provided methods to record different events link with the several types of
 * {@link Contentlet}
 *
 * @see SystemEventsAPI
 * @see SystemEvent
 */
public class ContentletSystemEventUtil {

    private static final String SAVE_EVENT_PREFIX = "SAVE";
    private static final String UPDATE_EVENT_PREFIX = "UPDATE";
    private static final String ARCHIVED_EVENT_PREFIX = "ARCHIVED";
    private static final String UN_ARCHIVED_EVENT_PREFIX = "UN_SAVE";

    private static final String SITE_EVENT_SUFFIX= "SITE";

    private final SystemEventsAPI systemEventsAPI;

    @VisibleForTesting
    protected ContentletSystemEventUtil(SystemEventsAPI systemEventsAPI){
        this.systemEventsAPI = systemEventsAPI;
    }

    private ContentletSystemEventUtil(){
        this(APILocator.getSystemEventsAPI());
    }

    private static class SingletonHolder {
        private static final ContentletSystemEventUtil INSTANCE = new ContentletSystemEventUtil();
    }

    public static ContentletSystemEventUtil getInstance() {
        return ContentletSystemEventUtil.SingletonHolder.INSTANCE;
    }

    public void pushSaveEvent(Contentlet contentlet, boolean isNew){
        String actionName = getActionName(contentlet, isNew);
        sendEvent(contentlet, actionName);
    }

    private void sendEvent(Contentlet contentlet, String action) {
        SystemEventType systemEventType = getSystemEventType(contentlet, action);

        if (systemEventType != null) {
            Payload payload = new Payload(contentlet, Visibility.PERMISSION, String.valueOf(PermissionAPI.PERMISSION_READ));

            try {
                systemEventsAPI.push(new SystemEvent(systemEventType, payload));
            } catch (DotDataException e) {
                throw new CanNotPushSystemEventException(e);
            }
        }
    }

    public void pushArchiveEvent(Contentlet contentlet){
        sendEvent(contentlet, ARCHIVED_EVENT_PREFIX);
    }

    public void pushUnArchiveEvent(Contentlet contentlet){
        sendEvent(contentlet, UN_ARCHIVED_EVENT_PREFIX);
    }

    private String getActionName(Contentlet contentlet, boolean isNew) {
        return isNew ? SAVE_EVENT_PREFIX : UPDATE_EVENT_PREFIX;
    }

    private SystemEventType getSystemEventType(Contentlet contentlet, String methodName) {
        String contentType = getType(contentlet);
        String eventName = String.format("%s_%s", methodName, contentType);

        try {
            return SystemEventType.valueOf(eventName.toUpperCase());
        }catch(IllegalArgumentException e){
            return null;
        }
    }

    private String getType(Contentlet contentlet) {
        return contentlet != null && contentlet.isHost() ? SITE_EVENT_SUFFIX : contentlet.getStructure().getName().replace(" ", "_").toUpperCase();
    }

}
