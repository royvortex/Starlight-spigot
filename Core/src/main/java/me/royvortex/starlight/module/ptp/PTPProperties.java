package me.royvortex.starlight.module.ptp;

import me.royvortex.starlight.user.property.UserProperty;

public class PTPProperties {

    public static final UserProperty<Boolean> TELEPORT_REQUESTS = UserProperty.create("teleport_requests", Boolean.class, true, true);
}
