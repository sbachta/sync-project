package com.project.sync.persistence;

import com.project.sync.helpers.CreateRepository;
import com.project.sync.models.RegistrationData;
import com.project.sync.tables.pojos.Users;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.project.sync.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RegistrationRepositoryTest {

    @Autowired
    private DSLContext db;

    @Autowired
    private CreateRepository<RegistrationData> subject;

    private final RegistrationData registrationData = RegistrationData.builder()
                                                                      .username("someName")
                                                                      .password("somePassword")
                                                                      .email("someemail@email.com")
                                                                      .build();

    @BeforeEach
    void setUp() {
        db.deleteFrom(USERS).execute();
    }

    @Test
    void shouldStoreUserInDatabase() {
        subject.create(registrationData);

        Users actual = db.select().from(USERS).fetchOne().into(Users.class);

        assertThat(actual.getEmail()).isEqualTo(registrationData.getEmail());
        assertThat(actual.getUsername()).isEqualTo(registrationData.getUsername());
        assertThat(actual.getPassword()).isEqualTo(registrationData.getPassword());

    }

    @Test
    void shouldReturnOptionalTrueWhenSuccessful() {
        Optional<Boolean> actual = subject.create(registrationData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseWhenUnableToSave() {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values(registrationData.getEmail(), registrationData.getPassword(), registrationData.getUsername())
          .execute();

        Optional<Boolean> actual = subject.create(registrationData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}