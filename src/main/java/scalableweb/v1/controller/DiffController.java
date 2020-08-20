package scalableweb.v1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scalableweb.domain.Diff;
import scalableweb.domain.DiffCalculationResponse;
import scalableweb.domain.JsonBase64DTO;
import scalableweb.service.IDiffService;

/**
 * Responsible for handling http requests on specified endpoints and provide appropriate responses.
 */
@Api(value = "diffControllerV1")
@RestController("diffControllerV1")
@RequestMapping("/v1/diff")
public class DiffController {

    @Autowired
    private IDiffService diffService;

    /**
     * Responds upon requests to compute the difference between two sides of a {@link Diff}, providing the result if available.
     *
     * @param id the {@link Diff} id
     * @return {@link ResponseEntity} including a {@link DiffCalculationResponse}
     */
    @ApiOperation(
            value = "Responds upon requests to compute the difference between two sides of a Diff, providing the result if available.",
            notes = "If either left or right side is null, or Diff with specified id does not exist, errors are expected.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffCalculationResponse> calculateDifference(@PathVariable Long id) {

        DiffCalculationResponse diffCalculationResponse = diffService.calculateDifference(id);

        return new ResponseEntity<>(diffCalculationResponse, HttpStatus.OK);
    }

    /**
     * Handles requests for base64 binary encoded data to be stored on the left side of a {@link Diff}.
     *
     * @param id       the {@link Diff} id
     * @param diffLeft includes the base64 binary encoded data
     * @return the updated {@link Diff}
     */
    @ApiOperation(
            value = "Handles requests for base64 binary encoded data to be stored on the left side of a Diff.")
    @PutMapping(value = "/{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Diff> createDiffLeft(
            @PathVariable Long id, @RequestBody @Valid JsonBase64DTO diffLeft) {

        Diff savedDiff = diffService.saveLeft(id, diffLeft);

        return new ResponseEntity<>(savedDiff, HttpStatus.OK);
    }

    /**
     * Handles requests for base64 binary encoded data to be stored on the right side of a {@link Diff}.
     *
     * @param id        the {@link Diff} id
     * @param diffRight includes base64 binary encoded data
     * @return the updated {@link Diff}
     */
    @ApiOperation(
            value = "Handles requests for base64 binary encoded data to be stored on the right side of a Diff.")
    @PutMapping(value = "/{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Diff> createDiffRight(
            @PathVariable Long id, @RequestBody @Valid JsonBase64DTO diffRight) {

        Diff savedDiff = diffService.saveRight(id, diffRight);

        return new ResponseEntity<>(savedDiff, HttpStatus.OK);
    }
}
