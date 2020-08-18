package scalableweb.v1.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import scalableweb.DifferenceApplication;
import scalableweb.domain.JsonBase64DTO;
import scalableweb.repository.IDiffRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DifferenceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application-integrationtest.properties",
        "classpath:messages.properties"})
@AutoConfigureTestDatabase
public class DiffControllerIntegrationTest {

    private static final Long ID = 1L;
    private static final String LEFT =
            "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";
    private static final String RIGHT =
            "ewogInBsYW5ldCIgOiAiZWFydGgiLAogInNpeiIgOiAiMWttIiwKICJtYXNzIiA6ICIxN2tnIiwKICJ0aW1lIiA6ICIxc2Vjb25kIgp9";
    private static final String LEFT_DECODED =
            "{\n \"planet\" : \"pluto\",\n \"size\" : \"6km\",\n \"mass\" : \"3kg\",\n \"time\" : \"7second\"\n}";
    private static final String RIGHT_DECODED =
            "{\n \"planet\" : \"earth\",\n \"siz\" : \"1km\",\n \"mass\" : \"17kg\",\n \"time\" : \"1second\"\n}";
    private static final String EXPECTED_DIFFERENT_OFFSETS =
            "[15, 16, 17, 19, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 68]";
    private static final String LEFT_ENDPOINT = "/v1/diff/{id}/left";
    private static final String RIGHT_ENDPOINT = "/v1/diff/{id}/right";
    private static final String CALCULATE_DIFF_ENDPOINT = "/v1/diff/{id}";

    @Value("${differentSize}")
    private String differentSize;

    @Value("${noDifference}")
    private String noDifference;

    @Value("${wrongFormat}")
    private String wrongFormat;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private IDiffRepository diffRepository;

    @After
    public void resetDb() {
        diffRepository.deleteAll();
    }

    @Test
    public void createDiffLeft_whenInvalidBase64_thenReturnBadRequest()
            throws Exception {

        mvc.perform(put(LEFT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO("15310597111111111111111111111111111111")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDiffLeftWhenBase64Given() throws Exception {

        mvc.perform(put(LEFT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(LEFT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.left")
                        .value(LEFT_DECODED))
                .andExpect(jsonPath("$.right").isEmpty());
    }

    @Test
    public void createDiffRightWhenBase64Given() throws Exception {

        mvc.perform(put(RIGHT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(RIGHT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.left").isEmpty())
                .andExpect(jsonPath("$.right")
                        .value(RIGHT_DECODED));
    }

    @Test
    public void calculateDifference_whenLeftAndRightSameSizeNotEqual_thenDifferentOffsetsReturned()
            throws Exception {

        mvc.perform(put(LEFT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(LEFT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.left")
                        .value(LEFT_DECODED))
                .andExpect(jsonPath("$.right").isEmpty());

        mvc.perform(put(RIGHT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(RIGHT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.left").value(LEFT_DECODED))
                .andExpect(jsonPath("$.right")
                        .value(RIGHT_DECODED));

        mvc.perform(get(CALCULATE_DIFF_ENDPOINT, ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.differentOffsets")
                        .value(EXPECTED_DIFFERENT_OFFSETS));
    }

    @Test
    public void calculateDifference_whenLeftAndRightAreEqual_thenNoDifferenceMessageReturned()
            throws Exception {

        String randomBase64 = "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbS";

        mvc.perform(put(LEFT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(randomBase64)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(put(RIGHT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(randomBase64)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get(CALCULATE_DIFF_ENDPOINT, ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(noDifference));
    }

    @Test
    public void calculateDifference_whenLeftAndRightDifferentSize_thenDifferentSizeMessageReturned()
            throws Exception {

        mvc.perform(put(LEFT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(LEFT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(put(RIGHT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(RIGHT.substring(RIGHT.length() / 2))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get(CALCULATE_DIFF_ENDPOINT, ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(differentSize));
    }

    @Test
    public void calculateDifference_whenRightOrLeftDiffSideIsNull_thenReturnBadRequest()
            throws Exception {

        mvc.perform(put(LEFT_ENDPOINT, ID)
                .content(asJsonString(new JsonBase64DTO(LEFT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get(CALCULATE_DIFF_ENDPOINT, ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void calculateDifference_whenNoDiffExistsWithThatId_thenReturnNotFound()
            throws Exception {

        mvc.perform(get(CALCULATE_DIFF_ENDPOINT, ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
