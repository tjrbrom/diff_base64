package tobase64.service;

import tobase64.domain.Diff;
import tobase64.domain.DiffCalculationResponse;
import tobase64.domain.JsonBase64DTO;

/**
 * Represents the basic structure and responsibilities of a diff service.
 */
public interface IDiffService {

    /**
     * Calculates the difference between the right and left side of a {@link Diff}.
     *
     * @param id id of the {@link Diff}
     * @return {@link DiffCalculationResponse} a response that describes the difference between left
     * and right, if applicable.
     */
    DiffCalculationResponse calculateDifference(Long id);

    /**
     * Populates left side of {@link Diff}.
     *
     * @param id       id of the {@link Diff}
     * @param diffLeft base64 encoded data representing the left side of a {@link Diff}
     * @return the updated {@link Diff}
     */
    Diff saveLeft(Long id, JsonBase64DTO diffLeft);

    /**
     * Populates right side of {@link Diff}.
     *
     * @param id        id of the {@link Diff}
     * @param diffRight base64 encoded data representing the right side of a {@link Diff}
     * @return the updated {@link Diff}
     */
    Diff saveRight(Long id, JsonBase64DTO diffRight);
}
