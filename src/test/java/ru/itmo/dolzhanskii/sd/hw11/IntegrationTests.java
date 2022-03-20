package ru.itmo.dolzhanskii.sd.hw11;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.FileCopyUtils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.when;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = ANY, connection = H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Clock clock;

    @BeforeEach
    public void setUp() {
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        setTime("2022-01-01T00:00:00Z");
    }

    @Test
    void rejectUnknownCustomer() throws Exception {
        enter(42).andExpect(status().isForbidden());
    }

    @Test
    void creatCustomerAndAcceptEnter() throws Exception {
        createPass();
        enter(0).andExpect(status().isOk());
    }

    @Test
    void clumsyCustomer() throws Exception {
        createPass();
        setTime("2022-01-01T12:00:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-01T12:01:00Z");
        enter(0).andExpect(status().isForbidden());
        setTime("2022-01-01T13:00:00Z");
        exit(0).andExpect(status().isOk());
        setTime("2022-01-01T13:01:00Z");
        exit(0).andExpect(status().isForbidden());
    }

    @Test
    void revokedPass() throws Exception {
        setTime("2022-01-01T12:00:00Z");
        createPass();
        setTime("2022-01-01T12:01:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-01T12:02:00Z");
        revokePass(0);
        setTime("2022-01-01T13:00:00Z");
        exit(0).andExpect(status().isOk());
        setTime("2022-01-01T13:01:00Z");
        enter(0).andExpect(status().isForbidden());
    }

    @Test
    void extendedPass() throws Exception {
        setTime("2022-01-01T12:00:00Z");
        createPass();

        setTime("2022-01-01T12:01:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-01T12:02:00Z");
        exit(0).andExpect(status().isOk());

        setTime("2022-01-03T12:00:00Z");
        enter(0).andExpect(status().isForbidden());
        setTime("2022-01-03T12:01:00Z");
        extendPass(0);

        setTime("2022-01-03T12:02:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-03T13:00:00Z");
        exit(0).andExpect(status().isOk());

        setTime("2022-03-01T12:00:00Z");
        enter(0).andExpect(status().isForbidden());
    }

    @Test
    void emptyReport() throws Exception {
        getReport()
            .andExpect(status().isOk())
            .andExpect(content().json(loadFile("response/report/empty_report.json"), true));
    }

    @Test
    void oneVisitReport() throws Exception {
        setTime("2022-01-01T00:00:00Z");
        createPass();
        setTime("2022-01-01T12:00:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-01T13:00:00Z");
        exit(0).andExpect(status().isOk());

        getReport()
            .andExpect(status().isOk())
            .andExpect(content().json(loadFile("response/report/one_visit_report.json"), true));
    }

    @Test
    void twoCustomersVisitReport() throws Exception {
        setTime("2022-01-01T00:00:00Z");
        createPass(2, 0);
        setTime("2022-01-01T12:00:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-01T13:00:00Z");
        exit(0).andExpect(status().isOk());
        setTime("2022-01-01T23:00:00Z");
        enter(0).andExpect(status().isOk());
        setTime("2022-01-02T01:00:00Z");
        exit(0).andExpect(status().isOk());

        setTime("2022-01-02T11:00:00Z");
        createPass(1, 1);
        setTime("2022-01-02T12:00:00Z");
        enter(1).andExpect(status().isOk());
        setTime("2022-01-02T12:30:00Z");
        exit(1).andExpect(status().isOk());

        getReport()
            .andExpect(status().isOk())
            .andExpect(content().json(loadFile("response/report/two_customers_report.json"), true));
    }

    private void createPass() throws Exception {
        createPass(1, 0);
    }

    private void createPass(int days, int expectedId) throws Exception {
        mockMvc.perform(
        post("/administrator")
            .queryParam("days", Integer.toString(days))
        )
            .andExpect(status().isOk())
            .andExpect(content().string(Integer.toString(expectedId)));
    }

    private void revokePass(long passId) throws Exception {
        mockMvc.perform(
        post("/administrator/revoke")
            .queryParam("passId", Long.toString(passId))
        )
            .andExpect(status().isOk());
    }

    private void extendPass(long passId) throws Exception {
        mockMvc.perform(
        post("/administrator/updateValidUntil")
            .queryParam("passId", Long.toString(passId))
            .queryParam("newValidUntil", "2022-02-01")
        )
            .andExpect(status().isOk());
    }

    private ResultActions enter(long passId) throws Exception {
        return mockMvc.perform(
        put("/turnstile/enter")
            .queryParam("passId", Long.toString(passId))
        );
    }

    private ResultActions exit(long passId) throws Exception {
        return mockMvc.perform(
        put("/turnstile/exit")
            .queryParam("passId", Long.toString(passId))
        );
    }

    private ResultActions getReport() throws Exception {
        return mockMvc.perform(
            get("/report")
        );
    }

    private String loadFile(String path) {
        Resource resource = new DefaultResourceLoader().getResource(path);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void setTime(String time) {
        when(clock.instant()).thenReturn(Instant.parse(time));
    }
}
