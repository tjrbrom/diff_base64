package tobase64.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import tobase64.domain.Diff;
import tobase64.domain.DiffCalculationResponse;
import tobase64.domain.JsonBase64DTO;
import tobase64.exceptions.MissingInformationException;
import tobase64.exceptions.ResourceNotFoundException;
import tobase64.repository.IDiffRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:messages.properties")
public class DiffServiceTest {

    private static final Long ID = 1L;

    @Value("${differentSize}")
    private String differentSize;

    @Value("${noDifference}")
    private String noDifference;

    @Value("${wrongFormat}")
    private String wrongFormat;

    @TestConfiguration
    static class DiffServiceTestContextConfiguration {

        @Bean
        public IDiffService diffService() {

            return new DiffService();
        }
    }

    @Autowired
    private IDiffService diffService;

    @MockBean
    private IDiffRepository diffRepository;

    private Diff diff;

    @Before
    public void setUp() {

        diff = new Diff(ID, null, null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void
    calculateDifference_whenDiffWithGivenIdDoesNotExist_thenThrowResourceNotFoundException() {

        diffService.calculateDifference(ID);
    }

    @Test(expected = MissingInformationException.class)
    public void
    calculateDifference_whenDiffExistsWithLeftOrRightNull_thenThrowMissingInformationException() {

        String left =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";

        when(diffRepository.findById(ID)).thenReturn(Optional.of(diff));

        diffService.saveLeft(ID, new JsonBase64DTO(left));

        diffService.calculateDifference(ID);
    }

    @Test
    public void calculateDifference_whenDiffExistsWithLeftAndRightEqual_thenNoDifferenceMessageReturned() {

        String left =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";
        String right =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";

        when(diffRepository.findById(ID)).thenReturn(Optional.of(diff));

        diffService.saveLeft(ID, new JsonBase64DTO(left));
        diffService.saveRight(ID, new JsonBase64DTO(right));

        DiffCalculationResponse response = diffService.calculateDifference(ID);
        assertEquals(response.getMessage(), noDifference);
    }

    @Test
    public void calculateDifference_whenDiffExistsWithLeftAndRightDifferentSize_thenDifferentSizeMessageReturned() {

        String left =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";
        String right =
                "ewogInBsYW5ldCIgOiAiZWFydGgiLAogInNpeiIgOiAiMWttIi";

        when(diffRepository.findById(ID)).thenReturn(Optional.of(diff));

        diffService.saveLeft(ID, new JsonBase64DTO(left));
        diffService.saveRight(ID, new JsonBase64DTO(right));

        DiffCalculationResponse response = diffService.calculateDifference(ID);
        assertEquals(response.getMessage(), differentSize);
    }

    @Test
    public void calculateDifference_whenDiffExistsWithLeftAndRightSameSizeNotEqual_thenDifferenceOffsetReturned() {

        String left =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";
        String right =
                "ewogInBsYW5ldCIgOiAiZWFydGgiLAogInNpeiIgOiAiMWttIiwKICJtYXNzIiA6ICIxN2tnIiwKICJ0aW1lIiA6ICIxc2Vjb25kIgp9";

        when(diffRepository.findById(ID)).thenReturn(Optional.of(diff));

        diffService.saveLeft(ID, new JsonBase64DTO(left));
        diffService.saveRight(ID, new JsonBase64DTO(right));

        DiffCalculationResponse response = diffService.calculateDifference(ID);
        assertEquals(response.getDifferentOffsets(),
                "[15, 16, 17, 19, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 68]");
    }

    @Test
    public void saveLeft_whenGivenEncodedInput_thenExpectDecoded() {

        String left =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";

        String leftDecoded =
                "{\n \"planet\" : \"pluto\",\n \"size\" : \"6km\",\n \"mass\" : \"3kg\",\n \"time\" : \"7second\"\n}";

        when(diffRepository.findById(ID)).thenReturn(Optional.of(diff));
        when(diffRepository.save(diff)).thenReturn(diff);

        Diff result = diffService.saveLeft(ID, new JsonBase64DTO(left));

        assertEquals(result.getLeft(), leftDecoded);
        assertNull(result.getRight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveLeft_whenInvalidBase64_thenThrowIllegalArgumentException() {

        diffService.saveLeft(ID, new JsonBase64DTO("15310597111111111111111111111111111111"));
    }

    @Test
    public void saveRight_whenGivenEncodedInput_thenExpectDecoded() {

        String left =
                "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";

        String rightDecoded =
                "{\n \"planet\" : \"pluto\",\n \"size\" : \"6km\",\n \"mass\" : \"3kg\",\n \"time\" : \"7second\"\n}";

        when(diffRepository.findById(ID)).thenReturn(Optional.of(diff));
        when(diffRepository.save(diff)).thenReturn(diff);

        Diff result = diffService.saveRight(ID, new JsonBase64DTO(left));

        assertEquals(result.getRight(), rightDecoded);
        assertNull(result.getLeft());
    }

}
