package com.project.sync.helpers;

import java.util.Optional;

public interface ReadRepository<INPUT> {

    Optional<Boolean> read(INPUT input);
}
