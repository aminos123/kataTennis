package fr.company.service.impl;

import fr.company.service.ScoreTennis;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("unit tests on ScoreTennisImplTest to test ScoreTennisImpl")
class ScoreTennisImplTest {

    private ScoreTennis scoreTennis;

    @BeforeEach
    void setUp() {
        scoreTennis = new ScoreTennisImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest(name = "input={0} | expectedMessage={1}")
    @MethodSource(value="invalidArguments")
    @DisplayName("should throw IllegalArgumentException when getScoreMessage inputs are invalid")
    void shouldGetIllegalArgumentExceptionWhenInputIsInConsistent(final String input, final String expectedMessage) {
        //given
        assertThatThrownBy(() -> scoreTennis.printScoreMessage(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    @ParameterizedTest(name = "input={0} | expectedMessageScore={1}")
    @MethodSource(value="validArguments")
    @DisplayName("should print a valid score as expected")
    void shouldPrintAValidScoreAsExpected(final String input, final String expectedMessageScore) {
        //given

        //when
        final String scoreMessage = scoreTennis.printScoreMessage(input);
        //then
        Assertions.assertAll("Assert groups",
                () -> assertThat(scoreMessage).isNotNull(),
                () -> assertThat(scoreMessage).isEqualTo(expectedMessageScore)
                );
    }

    static Stream<Arguments> invalidArguments(){
        var args = new LinkedList<>(List.of(
                arguments(null, "input is missing"),
                arguments("", "input is empty"),
                arguments("ABXY", "input is not valid"),
                arguments("XYBA", "input is not valid")
        ));
        return args.stream();
    }

    static Stream<Arguments> validArguments(){
        var args = new LinkedList<>(List.of(
                arguments("ABABAA", """
                        Player A : 15 / Player B : 0
                        Player A : 15 / Player B : 15
                        Player A : 30 / Player B : 15
                        Player A : 30 / Player B : 30
                        Player A : 40 / Player B : 30
                        Player A wins
                        """),
                arguments("ABABBB", """
                        Player A : 15 / Player B : 0
                        Player A : 15 / Player B : 15
                        Player A : 30 / Player B : 15
                        Player A : 30 / Player B : 30
                        Player A : 30 / Player B : 40
                        Player B wins
                        """),
                arguments("ABABBAAA", """
                        Player A : 15 / Player B : 0
                        Player A : 15 / Player B : 15
                        Player A : 30 / Player B : 15
                        Player A : 30 / Player B : 30
                        Player A : 30 / Player B : 40
                        Player A : 40 / Player B : 40
                        Player A advantage
                        Player A wins
                        """),
                arguments("ABABBAABAA", """
                        Player A : 15 / Player B : 0
                        Player A : 15 / Player B : 15
                        Player A : 30 / Player B : 15
                        Player A : 30 / Player B : 30
                        Player A : 30 / Player B : 40
                        Player A : 40 / Player B : 40
                        Player A advantage
                        Player A : 40 / Player B : 40
                        Player A advantage
                        Player A wins
                        """),
                arguments("ABABBABB", """
                        Player A : 15 / Player B : 0
                        Player A : 15 / Player B : 15
                        Player A : 30 / Player B : 15
                        Player A : 30 / Player B : 30
                        Player A : 30 / Player B : 40
                        Player A : 40 / Player B : 40
                        Player B advantage
                        Player B wins
                        """),
                arguments("BAABABBB", """
                        Player A : 0 / Player B : 15
                        Player A : 15 / Player B : 15
                        Player A : 30 / Player B : 15
                        Player A : 30 / Player B : 30
                        Player A : 40 / Player B : 30
                        Player A : 40 / Player B : 40
                        Player B advantage
                        Player B wins
                        """),
                arguments("BABABABABB", """
                        Player A : 0 / Player B : 15
                        Player A : 15 / Player B : 15
                        Player A : 15 / Player B : 30
                        Player A : 30 / Player B : 30
                        Player A : 30 / Player B : 40
                        Player A : 40 / Player B : 40
                        Player B advantage
                        Player A : 40 / Player B : 40
                        Player B advantage
                        Player B wins
                        """)
        ));
        return args.stream();
    }
}