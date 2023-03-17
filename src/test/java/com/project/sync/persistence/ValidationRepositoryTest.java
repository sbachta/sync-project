package com.project.sync.persistence;

import com.project.sync.helpers.ReadRepository;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
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
class ValidationRepositoryTest {

    @Autowired
    private DSLContext db;

    @Autowired
    private ReadRepository<ImageData> subject;

    private final ImageData imageData = ImageData.builder()
                                                 .imageInfo("someImageInfo")
                                                 .userData(UserData.builder()
                                                                   .password("password")
                                                                   .username("username")
                                                                   .build())
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
    void shouldReturnOptionalTrueIfUsernamePasswordComboArePresentInDb() {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values("someRealEmail@address.com", imageData.getUserData().getPassword(), imageData.getUserData().getUsername())
          .execute();

        Optional<Boolean> actual = subject.read(imageData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseIfUsernameIsNotPresent() {
        Optional<Boolean> actual = subject.read(imageData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }

    @Test
    void shouldReturnOptionalFalseIfPasswordDoesNotMatchForUsername() {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values("someRealEmail@address.com", "someWrongPassword", imageData.getUserData().getUsername())
          .execute();

        Optional<Boolean> actual = subject.read(imageData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }

}