package com.project.sync.helpers;

import java.util.Optional;

public interface Service<REQUEST, RESPONSE> {

    Optional<RESPONSE> serve(REQUEST request);
}
