package me.royvortex.starlight.module.essential;

import me.royvortex.starlight.user.property.UserProperty;

public class EssentialProperties {

    public static final UserProperty<String> CUSTOM_NAME = UserProperty.create("custom_name", String.class, "", true);
}
