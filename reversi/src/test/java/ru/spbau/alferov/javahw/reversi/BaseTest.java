package ru.spbau.alferov.javahw.reversi;

import org.junit.jupiter.api.BeforeAll;
import static org.mockito.Mockito.*;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.player.ai.AIStorage;
import ru.spbau.alferov.javahw.reversi.stats.PlayedGame;
import ru.spbau.alferov.javahw.reversi.stats.StatisticsController;
import ru.spbau.alferov.javahw.reversi.ui.ReversiUI;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class BaseTest {
    private static Field reversiApplicationInstance;
    private static ReversiApplication reversiApplication;
    protected static AIStorage aiStorage;
    private static StatisticsController statsController;
    protected static List<PlayedGame> lastStatsSet = Collections.emptyList();

    private static void setUpFields() {
        try {
            reversiApplicationInstance = ReversiApplication.class.getDeclaredField("instance");
            reversiApplicationInstance.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("ReversiApplication.instance is not declared");
        }
    }

    @BeforeAll
    public static void setUpMocks() {
        setUpFields();

        reversiApplication = mock(ReversiApplication.class);
        try {
            reversiApplicationInstance.set(null, reversiApplication);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Could not set up mocks", ex);
        }

        aiStorage = new AIStorage();
        statsController = new StatisticsController();

        when(reversiApplication.getAiStorage()).thenReturn(aiStorage);
        when(reversiApplication.getStatsController()).thenReturn(statsController);
        doNothing().when(reversiApplication).updateField(any());
        doAnswer(invocationOnMock -> {
            lastStatsSet = invocationOnMock.getArgument(0);
            return null;
        }).when(reversiApplication).updateStatsList(anyList());
    }
}
