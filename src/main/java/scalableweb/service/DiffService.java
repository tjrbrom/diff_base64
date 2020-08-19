package scalableweb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import scalableweb.domain.Diff;
import scalableweb.domain.DiffCalculationResponse;
import scalableweb.domain.JsonBase64DTO;
import scalableweb.exceptions.MissingInformationException;
import scalableweb.exceptions.NotBase64Exception;
import scalableweb.exceptions.ResourceNotFoundException;
import scalableweb.repository.IDiffRepository;

/**
 * {@link Service} responsible for delegating REST transaction calls to the underlying Spring
 * Repository. Includes validations and throws appropriate exceptions based on input, as well as the
 * calculation of the difference between left and right input data.
 */
@Service
public class DiffService implements IDiffService {

    Logger LOGGER = LoggerFactory.getLogger(DiffService.class);

    @Value("${differentSize}")
    private String differentSize;

    @Value("${noDifference}")
    private String noDifference;

    @Value("${wrongFormat}")
    private String wrongFormat;

    @Autowired
    private IDiffRepository diffRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public DiffCalculationResponse calculateDifference(Long id) {

        Optional<Diff> diffOpt = diffRepository.findById(id);

        if (diffOpt.isPresent()) {

            return calculateDifference(diffOpt.get());
        }

        LOGGER.warn("Diff with id {} not found", id);
        throw new ResourceNotFoundException("Diff with id " + id + " not found");
    }

    /**
     * {@inheritDoc} If {@link Diff} doesn't exist, will create a new one first.
     */
    @Override
    public Diff saveLeft(Long id, JsonBase64DTO diffLeft) {

        String decoded = decode(diffLeft);

        Optional<Diff> diffOpt = diffRepository.findById(id);

        if (!diffOpt.isPresent()) {

            Diff newDiff = new Diff(id, decoded, null);
            return diffRepository.save(newDiff);
        }

        Diff diff = diffOpt.get();
        diff.setLeft(decoded);

        return diffRepository.save(diff);
    }

    /**
     * {@inheritDoc} If {@link Diff} doesn't exist, will create a new one first.
     */
    @Override
    public Diff saveRight(Long id, JsonBase64DTO diffRight) {

        String decoded = decode(diffRight);

        Optional<Diff> diffOpt = diffRepository.findById(id);

        if (!diffOpt.isPresent()) {

            Diff newDiff = new Diff(id, null, decoded);
            return diffRepository.save(newDiff);
        }

        Diff diff = diffOpt.get();
        diff.setRight(decoded);

        return diffRepository.save(diff);
    }

    private String decode(JsonBase64DTO jsonBase64DTO) {

        String base64Data = jsonBase64DTO.getBase64EncodedData();

        if (!Base64.isBase64(base64Data)) {

            LOGGER.warn(wrongFormat);
            throw new NotBase64Exception(wrongFormat);
        }

        byte[] base64DecodedData = Base64.decodeBase64(base64Data);

        return new String(base64DecodedData);
    }

    private DiffCalculationResponse calculateDifference(Diff diff) {

        String left = diff.getLeft();
        String right = diff.getRight();

        if (left == null || right == null) {

            LOGGER.warn("Base64 data values are missing, for diff {}.", diff.getId());
            throw new MissingInformationException(
                    String.format("Base64 data values are missing, for diff {%s}.", diff.getId()));
        }

        if (left.equals(right)) {

            return new DiffCalculationResponse(noDifference, null);
        }

        byte[] leftBytes = left.getBytes();
        byte[] rightBytes = right.getBytes();

        if (leftBytes.length != rightBytes.length) {

            return new DiffCalculationResponse(differentSize, null);
        }

        List<Integer> diffOffsetsList = getListOfDifferentOffsets(leftBytes, rightBytes);

        return new DiffCalculationResponse(null, diffOffsetsList.toString());
    }

    private List<Integer> getListOfDifferentOffsets(byte[] leftBytes, byte[] rightBytes) {

        List<Integer> diffOffsetsList = new ArrayList<>();

        for (int i = 0; i < leftBytes.length; i++) {
            if (leftBytes[i] != rightBytes[i]) {
                diffOffsetsList.add(i);
            }
        }

        return diffOffsetsList;
    }
}
