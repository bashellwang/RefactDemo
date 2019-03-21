package com.bashellwang.refactdemo.notification;

import com.bashellwang.refactdemo.notification.annotation.WhereToDeal;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liang.wang on 2019/3/19.
 */
public class EventHelper {

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void post(EventMessage event) {
        EventBus.getDefault().post(event);
    }

    @WhereToDeal(results = {""})
    public static void postStick(EventMessage event) {
        EventBus.getDefault().postSticky(event);
    }

    public static <T> void removeStickyEvent(Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent((T) stickyEvent);
        }
    }

    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    public static void cancelEventDelivery(EventMessage event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }
}
