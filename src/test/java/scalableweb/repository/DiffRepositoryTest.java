package scalableweb.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import scalableweb.domain.Diff;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DiffRepositoryTest {

    private static final Long ID = 1L;
    private static final String LEFT = "ewogInBsYW5ldCIgOiAicGx1dG8iLAogInNpemUiIDogIjZrbSIsCiAibWFzcyIgOiAiM2tnIiwKICJ0aW1lIiA6ICI3c2Vjb25kIgp9";
    private static final String RIGHT = "ewogInBsYW5ldCIgOiAiZWFydGgiLAogInNpeiIgOiAiMWttIiwKICJtYXNzIiA6ICIxN2tnIiwKICJ0aW1lIiA6ICIxc2Vjb25kIgp9";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IDiffRepository diffRepository;

    private Diff diff;

    @Before
    public void setUp() {

        diff = new Diff(ID, LEFT, RIGHT);

        entityManager.persist(diff);
        entityManager.flush();
    }

    @Test
    public void findById_whenDiffIsFound_thenReturnDiff() {

        Optional<Diff> foundOpt = diffRepository.findById(diff.getId());

        assertTrue(foundOpt.isPresent());
        assertEquals(foundOpt.get().getId(), diff.getId());
    }

    @Test
    public void save_whenDiffIsSaved_thenReturnDiff() {

        Diff saved = diffRepository.save(diff);

        assertNotNull(saved);
        assertEquals(saved.getId(), diff.getId());
    }

}
