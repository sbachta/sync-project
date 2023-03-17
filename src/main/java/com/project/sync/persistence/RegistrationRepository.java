package com.project.sync.persistence;

import com.project.sync.helpers.CreateRepository;
import com.project.sync.models.RegistrationData;
import com.project.sync.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RegistrationRepository implements CreateRepository<RegistrationData> {

    private final        DSLContext dslContext;
    public RegistrationRepository(final DSLContext dslContext) {
        this.dslContext   = dslContext;
    }


    @Override
    public Optional<Boolean> create(RegistrationData registrationData) {
        try {
            dslContext.executeInsert(
                    new UsersRecord(
                            registrationData.getUsername(),
                            registrationData.getPassword(),
                            registrationData.getEmail()
                    ));
            return Optional.of(true);
        } catch (RuntimeException exception) {
            return Optional.of(false);
        }
    }
}
