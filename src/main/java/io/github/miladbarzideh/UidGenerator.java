package io.github.miladbarzideh;


public interface UidGenerator {

    /**
     * Generate a unique ID. <br>
     *
     * @return ID
     */
    long nextId();

    /**
     * Parse the ID into elements which are used to generate the ID. <br>
     *
     * @param id Id
     * @return {@link ParsedId} information
     */
    ParsedId parseId(long id);
}
