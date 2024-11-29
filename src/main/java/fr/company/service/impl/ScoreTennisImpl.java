package fr.company.service.impl;

import fr.company.service.ScoreTennis;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public class ScoreTennisImpl implements ScoreTennis {

    public static final String PLAYER = "Player ";
    public static final String ADVANTAGE = " advantage";
    public static final String WINS = " wins";

    public String printScoreMessage(@NotNull String input) {

        checkConsistency(input);
        StringBuilder scoreMessage = new StringBuilder();
        final AtomicInteger scoreA = new AtomicInteger();
        final AtomicInteger scoreB = new AtomicInteger();
        final AtomicReference<Boolean> deuce = new AtomicReference<>(false);
        final AtomicReference<Boolean> isAdvantageA = new AtomicReference<>(false);
        final AtomicReference<Boolean> isAdvantageB = new AtomicReference<>(false);

        Stream<Character> characterStream = input
                .chars()
                .mapToObj(c -> (char) c);

        characterStream
                .forEach(c -> {
                    if (compareChars(c, 'A')) {
                        calculateScoreWhenPlayerA(c, deuce, scoreA, isAdvantageB, scoreMessage, isAdvantageA, scoreB);
                    } else {
                        calculateScoreWhenPlayerB(c, deuce, scoreA, isAdvantageB, scoreMessage, isAdvantageA, scoreB);
                    }
                });

        return scoreMessage.toString();
    }

    /**
     * Compare chars
     * @param c1
     * @param c2
     * @return
     */
    private boolean compareChars(char c1, char c2) {
        return c1 <= c2;
    }

    /**
     *
     * @param supplier
     * @param message
     */
    private void prevent(BooleanSupplier supplier, String message) {
        if (supplier.getAsBoolean())
            throw new IllegalArgumentException(message);
    }

    /**
     *
     * @param input
     */
    private void checkConsistency(String input) {
        prevent(
                () -> null == input, "input is missing"
        );
        prevent(
                input::isEmpty, "input is empty"
        );
        prevent(
                () -> !Pattern.matches("^[AB]*$", input), "input is not valid"
        );
    }

    /**
     *
     * @param c
     * @param deuce
     * @param scoreA
     * @param isAdvantageB
     * @param scoreMessage
     * @param isAdvantageA
     * @param scoreB
     */
    private void calculateScoreWhenPlayerA(
            Character c,
            AtomicReference<Boolean> deuce,
            AtomicInteger scoreA,
            AtomicReference<Boolean> isAdvantageB,
            StringBuilder scoreMessage,
            AtomicReference<Boolean> isAdvantageA,
            AtomicInteger scoreB) {

        if (calculateScoreAndPrintScoreMessageWhenPlayerWins(c, deuce, scoreA, isAdvantageB, scoreMessage)) return;
        if (isPlayerWins(isAdvantageA, scoreMessage, c)) {
            return;
        }
        if (isDeuce(deuce)) {
            printScoreAdvantageForPlayerA(c, deuce, isAdvantageB, scoreMessage, isAdvantageA);
        }
        doCommonMessageScore(
                isAdvantageA,
                isAdvantageB,
                deuce,
                scoreMessage,
                scoreA,
                scoreB,
                c
        );
    }

    /**
     *
     * @param c
     * @param deuce
     * @param scoreA
     * @param isAdvantageB
     * @param scoreMessage
     * @param isAdvantageA
     * @param scoreB
     */
    private void calculateScoreWhenPlayerB(Character c,
                                           AtomicReference<Boolean> deuce,
                                           AtomicInteger scoreA,
                                           AtomicReference<Boolean> isAdvantageB,
                                           StringBuilder scoreMessage,
                                           AtomicReference<Boolean> isAdvantageA,
                                           AtomicInteger scoreB) {
        if (calculateScoreAndPrintScoreMessageWhenPlayerWins(c, deuce, scoreB, isAdvantageA, scoreMessage)) return;
        if (isPlayerWins(isAdvantageB, scoreMessage, c)) {
            return;
        }
        if (isDeuce(deuce)) {
            printScoreAdvantageForPlayerB(c, deuce, isAdvantageB, scoreMessage, isAdvantageA);
        }
        doCommonMessageScore(
                isAdvantageA,
                isAdvantageB,
                deuce,
                scoreMessage,
                scoreA,
                scoreB,
                c
        );
    }

    private void printScoreAdvantageForPlayerB(Character c, AtomicReference<Boolean> deuce, AtomicReference<Boolean> isAdvantageB, StringBuilder scoreMessage, AtomicReference<Boolean> isAdvantageA) {
        printScoreInCaseOfDeuceAnSetDefaultValueOfDeuce(c, deuce, scoreMessage);
        isAdvantageB.set(true);
        isAdvantageA.set(false);
    }

    private void printScoreInCaseOfDeuceAnSetDefaultValueOfDeuce(Character c, AtomicReference<Boolean> deuce, StringBuilder scoreMessage) {
        scoreMessage.append(PLAYER)
                .append(c)
                .append(ADVANTAGE)
                .append(System.lineSeparator());
        deuce.set(false);
    }

    /**
     *
     * @param c
     * @param deuce
     * @param isAdvantageB
     * @param scoreMessage
     * @param isAdvantageA
     */
    private void printScoreAdvantageForPlayerA(Character c, AtomicReference<Boolean> deuce, AtomicReference<Boolean> isAdvantageB, StringBuilder scoreMessage, AtomicReference<Boolean> isAdvantageA) {
            printScoreInCaseOfDeuceAnSetDefaultValueOfDeuce(c, deuce, scoreMessage);
            isAdvantageB.set(false);
            isAdvantageA.set(true);
    }


    private boolean isDeuce(AtomicReference<Boolean> deuce) {
        return Boolean.TRUE.equals(deuce.get());
    }

    /**
     * calcul dus score et affichage du score message
     * en cas un player gagne
     * @param c
     * @param deuce
     * @param scoreA
     * @param isAdvantageB
     * @param scoreMessage
     * @return
     */
    private boolean calculateScoreAndPrintScoreMessageWhenPlayerWins(Character c, AtomicReference<Boolean> deuce, AtomicInteger scoreA, AtomicReference<Boolean> isAdvantageB, StringBuilder scoreMessage) {
        if (Boolean.FALSE.equals(deuce.get()) && 40 == scoreA.get() && Boolean.TRUE.equals(!isAdvantageB.get())) {
            scoreMessage
                    .append(PLAYER)
                    .append(c)
                    .append(WINS)
                    .append(System.lineSeparator());
            return true;
        } else {
            scoreA.addAndGet(scoreA.get() < 30 ? 15 : 10);
        }
        return false;
    }

    /**
     *
     * @param isAdvantageA
     * @param isAdvantageB
     * @param deuce
     * @param scoreMessage
     * @param scoreA
     * @param scoreB
     * @param c
     */
    private void doCommonMessageScore(AtomicReference<Boolean> isAdvantageA,
                                             AtomicReference<Boolean> isAdvantageB,
                                             AtomicReference<Boolean> deuce,
                                             StringBuilder scoreMessage,
                                             AtomicInteger scoreA,
                                             AtomicInteger scoreB,
                                             Character c) {

        if (Boolean.TRUE.equals(isAdvantageB.get()) && 'A' == c) {
            setEqualsScore(scoreA, scoreB);
            isAdvantageB.set(false);
        }
        if (Boolean.TRUE.equals(isAdvantageA.get()) && 'B' == c) {
            setEqualsScore(scoreA, scoreB);
            isAdvantageA.set(false);
        }
        if (scoreA.get() == scoreB.get() && scoreA.get() == 40) {
            deuce.set(true);
        }

        if ((Boolean.FALSE.equals(isAdvantageA.get()) && 'A' == c) ||
                (Boolean.FALSE.equals(isAdvantageB.get()) && 'B' == c)) {
            scoreMessage.append("Player A : ")
                    .append(scoreA)
                    .append(" / ")
                    .append("Player B : ")
                    .append(scoreB)
                    .append(System.lineSeparator());
        }
    }

    /**
     *
     * @param scoreA
     * @param scoreB
     */
    private void setEqualsScore(AtomicInteger scoreA, AtomicInteger scoreB) {
        scoreB.set(40);
        scoreA.set(40);
    }

    /**
     *
     * @param isAdvantage
     * @param scoreMessage
     * @param player
     * @return
     */
    private boolean isPlayerWins(AtomicReference<Boolean> isAdvantage, StringBuilder scoreMessage, Character player) {
        if (Boolean.TRUE.equals(isAdvantage.get())) {
            scoreMessage
                    .append(PLAYER)
                    .append(player)
                    .append(WINS)
                    .append(System.lineSeparator());
            isAdvantage.set(false);
            return true;
        }
        return false;
    }

}


