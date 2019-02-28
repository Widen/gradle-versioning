package com.widen.versioning

import spock.lang.Specification
import spock.lang.Unroll

class VersionGeneratorTest extends Specification {
    @Unroll("git describe '#describe' should become '#version'")
    def "parse git describe to version"() {
        expect:
        VersionGenerator.generateFromString(describe, new Settings(tagPrefix: prefix)).orElse(null) == version

        where:
        prefix | describe           | version
        null   | null               | null
        null   | ""                 | ""
        null   | "0.1.0-4-ge7426f0" | "0.1.0+4-ge7426f0"
        "v"    | "v1.2.3"           | "1.2.3"
    }
}
