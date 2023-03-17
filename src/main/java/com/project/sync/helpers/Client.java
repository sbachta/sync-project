package com.project.sync.helpers;

import java.util.Optional;

public interface Client<REQUEST, RESPONSE> {

    Optional<RESPONSE> send(REQUEST request);
}
