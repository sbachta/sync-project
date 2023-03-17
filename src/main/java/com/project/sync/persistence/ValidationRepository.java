package com.project.sync.persistence;

import com.project.sync.helpers.ReadRepository;
import com.project.sync.models.UserData;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.project.sync.Tables.USERS;

@Repository
public class ValidationRepository implements ReadRepository<UserData> {

    private final DSLContext dslContext;

    public ValidationRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Optional<Boolean> read(UserData userData) {
        return dslContext.fetchExists(
                dslContext.selectFrom(USERS)
                          .where(USERS.USERNAME.eq(userData.getUsername()))
                          .and(USERS.PASSWORD.eq(userData.getPassword())))
               ? Optional.of(true)
               : Optional.of(false);
    }
}
