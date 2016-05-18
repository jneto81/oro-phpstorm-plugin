package com.oroplatform.idea.oroplatform.intellij;

import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.oroplatform.idea.oroplatform.OroPlatformBundle;
import com.oroplatform.idea.oroplatform.settings.OroPlatformSettings;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;

class OroNotifications {
    private final static NotificationGroup GROUP = NotificationGroup.balloonGroup(OroPlatformBundle.message("notifications.group"));

    static void showPluginEnableNotification(@NotNull final Project project) {
        final Notification notification = GROUP.createNotification(
            OroPlatformBundle.message("notifications.enablePluginTitle"),
            OroPlatformBundle.message("notifications.enablePlugin"),
            NotificationType.INFORMATION,
            new NotificationListener() {
                @Override
                public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
                    notification.expire();

                    final OroPlatformSettings settings = OroPlatformSettings.getInstance(project);

                    if("enable".equals(event.getDescription())) {
                        settings.setPluginEnabled(true);
                        showPluginEnabledNotification(project);
                    } else if("dismiss".equals(event.getDescription())) {
                        settings.setPluginEnableDismissed(true);
                    }
                }
            });

        Notifications.Bus.notify(notification, project);
    }

    private static void showPluginEnabledNotification(@NotNull final Project project) {
        Notifications.Bus.notify(GROUP.createNotification(
            OroPlatformBundle.message("notifications.pluginEnabledTitle"),
            OroPlatformBundle.message("notifications.pluginEnabled"),
            NotificationType.INFORMATION, null
        ), project);
    }
}
