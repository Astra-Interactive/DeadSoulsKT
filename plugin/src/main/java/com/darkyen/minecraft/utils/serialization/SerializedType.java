package com.darkyen.minecraft.utils.serialization;

public enum SerializedType {
    NULL,
    PRIMITIVE_BOOLEAN_TRUE,
    PRIMITIVE_BOOLEAN_FALSE,
    PRIMITIVE_BYTE,
    PRIMITIVE_CHARACTER,
    PRIMITIVE_SHORT,
    PRIMITIVE_INT,
    PRIMITIVE_LONG,
    PRIMITIVE_FLOAT,
    PRIMITIVE_DOUBLE,
    STRING,
    /** List with a small number of entries whose amount fit into a byte. */
    LIST_BYTE,
    LIST,
    /** Map with a small number of entries whose amount fit into a byte. */
    MAP_BYTE,
    MAP,
    /** ConfigurationSerializable whose root map has a small number of entries whose amount fit into a byte. */
    CONFIGURATION_SERIALIZABLE_BYTE,
    CONFIGURATION_SERIALIZABLE;

    static final SerializedType[] VALUES = values();
}