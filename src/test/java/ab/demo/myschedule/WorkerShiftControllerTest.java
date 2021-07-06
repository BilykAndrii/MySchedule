package ab.demo.myschedule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = MyScheduleApplication.class)
public class WorkerShiftControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WorkerShiftRepository repo;

    @Test
    public void addNewShiftTest() throws Exception {
        // given
        String url = "/shifts/add/{worker}/{date}/{shift}";

        WorkerShift expected = WorkerShift.of("worker", LocalDate.parse("2002-02-02"), Timetable.FIRST.description());

        // when
        ResponseEntity<WorkerShiftResponse> response = restTemplate.postForEntity(url, null,
                WorkerShiftResponse.class, expected.getName(), expected.getDate(), expected.getShift());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).contains("Shift created");

        List<WorkerShift> actual = repo.findByNameAndDate(expected.getName(), expected.getDate());
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());

        assertEquals(expected, actual.get(0));
    }

    @Test
    public void addNewShiftNoValidParamsTest() throws Exception {
        // given
        String url = "/shifts/add/{worker}/{date}/{shift}";

        String name = "  ";
        String date = "notCorrectDate";
        String shift = "notCorrectShift";

        // when
        ResponseEntity<WorkerShiftResponse> response = restTemplate.postForEntity(url, null,
                WorkerShiftResponse.class, name, date, shift);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("worker name is empty");
        assertThat(response.getBody().getMessage()).contains("shift name is not valid");
        assertThat(response.getBody().getMessage()).contains("date is not valid");
    }

    @Test
    public void getAllShiftsTest() throws Exception {
        // given
        String url = "/shifts/all/{worker}";

        WorkerShift someExpected = WorkerShift.of("someWorker", LocalDate.parse("2019-01-19"), Timetable.SECOND.description());
        WorkerShift anotherExpected = WorkerShift.of("someWorker", LocalDate.parse("2019-03-15"), Timetable.THIRD.description());
        repo.save(someExpected);
        repo.save(anotherExpected);

        // when
        ResponseEntity<WorkerShiftResponse> response = restTemplate.getForEntity(url, WorkerShiftResponse.class, someExpected.getName());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<WorkerShift> actual = response.getBody().getData();
        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());

        assertEquals(someExpected, actual.get(0));
        assertEquals(anotherExpected, actual.get(1));
    }

    @Test
    public void getAllShiftsNoDataTest() throws Exception {
        // given
        String url = "/shifts/all/{worker}";
        String name = "noDataWorker";

        // when
        ResponseEntity<WorkerShiftResponse> response = restTemplate.getForEntity(url, WorkerShiftResponse.class, name);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertEquals("No data about shifts of this worker", response.getBody().getMessage());
    }

    @Test
    public void removeAllTest() throws Exception {
        // given
        RestTemplate template = new RestTemplate(new CustomClientHttpRequestFactory());
        String url = "http://localhost:8081/api/shifts/removeAll/{worker}";

        WorkerShift someExpected = WorkerShift.of("anotherWorker", LocalDate.parse("2033-03-11"), Timetable.THIRD.description());
        repo.save(someExpected);

        // when
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<WorkerShiftResponse> response = template.exchange(url, HttpMethod.DELETE, entity,
                WorkerShiftResponse.class, someExpected.getName());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<WorkerShift> result = repo.findByName(someExpected.getName());
        assertTrue(result.isEmpty());
    }

    // workaround to test DELETE method, because RestTemplate doesn't support output
    private class CustomClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

        @Override
        protected void prepareConnection(HttpURLConnection connection,
                                         String httpMethod) throws IOException {

            super.prepareConnection(connection, httpMethod);
            if ("DELETE".equals(httpMethod)) {
                connection.setDoOutput(true);
            }
        }
    }
}