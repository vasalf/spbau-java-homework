package ru.spbau.alferov.javahw.reversi.stats;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

public class StatisticsTest extends BaseTest {
    private void assertPlayedGameEquals(PlayedGame expected, PlayedGame actual) {
        assertEquals(expected.getBlackPlayerName(), actual.getBlackPlayerName());
        assertEquals(expected.getWhitePlayerName(), actual.getWhitePlayerName());
        assertEquals(expected.getBlackPlayerScore(), actual.getBlackPlayerScore());
        assertEquals(expected.getWhitePlayerScore(), actual.getWhitePlayerScore());
    }

    @BeforeEach
    public void clearStats() {
        lastStatsSet.clear();
    }

    @Test
    public void testOnePlayedGameStats() {
        PlayedGame playedGame = new PlayedGame("a", "b", 32, 32);
        StatisticsController statisticsController = new StatisticsController();
        statisticsController.add(playedGame);
        assertEquals(1, lastStatsSet.size());
        assertPlayedGameEquals(playedGame, lastStatsSet.get(0));
    }

    @Test
    public void testStatsOrder() {
        StatisticsController statisticsController = new StatisticsController();
        PlayedGame[] playedGames = new PlayedGame[179];
        for (int i = 0; i < 179; i++) {
            playedGames[i] = new PlayedGame(
                        String.valueOf(i + 1) + "a",
                        String.valueOf(i + 1) + "b",
                        i % 65,
                        64 - (i % 65)
                    );
            statisticsController.add(playedGames[i]);
        }
        for (int i = 0; i < 179; i++) {
            assertPlayedGameEquals(playedGames[178 - i], lastStatsSet.get(i));
        }
    }
}