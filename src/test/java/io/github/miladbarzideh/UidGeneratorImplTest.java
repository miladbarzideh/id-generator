package io.github.miladbarzideh;


import io.github.miladbarzideh.impl.UidGeneratorImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Unit test for unique ID generator.
 */
class UidGeneratorImplTest {

    private final UidGeneratorImpl uidGenerator = UidGeneratorImpl.build();
    private final static int SIZE = 10000;

    @Test
    void idGeneratorTest() {

//      When (Action)
        long id = uidGenerator.nextId();
        ParsedId parsedId = uidGenerator.parseId(id);

//      Then (Output)
        assertThat(id > 0L).isTrue();
        assertThat((parsedId.getTimeStamp().before(new Date()))).isTrue();
        assertThat((parsedId.getSequence())).isSameAs(0L);
    }

    @Test
    void serialGenerateTest() {

//      When (Action)
        Set<Long> idSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            idSet.add(uidGenerator.nextId());
        }

//      Then (Output)
        AssertionsForClassTypes.assertThat((idSet.size())).isSameAs(SIZE);
    }
}
