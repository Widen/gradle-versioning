package com.widen.versioning

import spock.lang.Specification
import spock.lang.Unroll

class VersionGeneratorTest extends Specification {
    @Unroll("git describe '#describe' should become '#version'")
    def "parse git describe to version"() {
        expect:
        VersionGenerator.generateFromString(describe) == version

        where:
        describe           | version
        null               | ""
        ""                 | ""
        "0.1.0-4-ge7426f0" | "0.1.0+4-ge7426f0"
    }
}
