

package com.github.huzekang.jdbcservice.enums;

public enum UploadModeEnum {

    NEW((short) 0),
    REPLACE((short) 1),
    APPEND((short) 2),

    ;

    private short mode;

    UploadModeEnum(short mode) {
        this.mode = mode;
    }

    public short getMode() {
        return mode;
    }
}
