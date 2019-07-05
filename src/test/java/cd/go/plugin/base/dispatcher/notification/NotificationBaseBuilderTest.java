package cd.go.plugin.base.dispatcher.notification;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationBaseBuilderTest {
    @Test
    void shouldSupportV1OfTheExtension() {
        assertThat(new NotificationBaseBuilder().v4())
                .isNotNull()
                .isInstanceOf(NotificationBuilderV4.class);
    }
}
