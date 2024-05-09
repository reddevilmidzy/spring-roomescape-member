package roomescape.controller;

import roomescape.exception.RoomescapeException;

public class AuthorizationException extends RoomescapeException {

    public AuthorizationException(final String message) {
        super(message);
    }
}
