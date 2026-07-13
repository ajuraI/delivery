package libs.errs;

import java.util.UUID;

public final class Except {

    private static final UUID EMPTY_UUID = new UUID(0L, 0L);

    private Except() {
    }

    public static String againstNullOrEmpty(String value, String paramName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(paramName + " must not be null or blank");
        }
        return value;
    }

    public static UUID againstNullOrEmpty(UUID value, String paramName) {
        if (value == null || EMPTY_UUID.equals(value)) {
            throw new IllegalArgumentException(paramName + " must not be null or empty");
        }
        return value;
    }

    public static <T> T againstNull(T value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " must not be null");
        }
        return value;
    }
}
