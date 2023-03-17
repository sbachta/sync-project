package com.project.sync.persistence;

import com.project.sync.helpers.CreateRepository;
import com.project.sync.models.RegistrationData;
import com.project.sync.models.UserData;
import com.project.sync.tables.pojos.Users;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.project.sync.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RegistrationRepositoryTest {

    @Autowired
    private DSLContext db;

    @Autowired
    private CreateRepository<RegistrationData> subject;

    private final RegistrationData registrationData = RegistrationData.builder()
                                                                      .userData(UserData.builder().username("someName")
                                                                                        .password("somePassword")
                                                                                        .build())
                                                                      .email("someemail@email.com")
                                                                      .build();

    @BeforeEach
    void setUp() {
        db.deleteFrom(USERS).execute();
    }

    @AfterEach
    void tearDown() {
        db.deleteFrom(USERS).execute();
    }

    @Test
    void shouldStoreUserInDatabase() {
        subject.create(registrationData);

        Users actual = db.select().from(USERS).fetchOne().into(Users.class);

        assertThat(actual.getEmail()).isEqualTo(registrationData.getEmail());
        assertThat(actual.getUsername()).isEqualTo(registrationData.getUserData().getUsername());
        assertThat(actual.getPassword()).isEqualTo(registrationData.getUserData().getPassword());

    }

    @Test
    void shouldReturnOptionalTrueWhenSuccessful() {
        Optional<Boolean> actual = subject.create(registrationData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseWhenUnableToSave() {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values(registrationData.getEmail(), registrationData.getUserData().getPassword(), registrationData.getUserData().getUsername())
          .execute();

        Optional<Boolean> actual = subject.create(registrationData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}