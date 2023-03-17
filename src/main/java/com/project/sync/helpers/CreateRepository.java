package com.project.sync.helpers;

import java.util.Optional;

public interface CreateRepository <INPUT> {

    Optional<Boolean> create(INPUT input);
}
