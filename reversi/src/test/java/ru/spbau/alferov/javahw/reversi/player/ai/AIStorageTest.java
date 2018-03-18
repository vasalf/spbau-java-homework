package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.reversi.BaseTest;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;
import ru.spbau.alferov.javahw.reversi.logic.Field;
import ru.spbau.alferov.javahw.reversi.logic.Turn;
import ru.spbau.alferov.javahw.reversi.player.Player;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AIStorageTest extends BaseTest {
    private static class NamedSkippingPlayer extends AIPlayer {
        @NotNull
        String name;

        public NamedSkippingPlayer(@NotNull String name) {
            this.name = name;
        }

        @Override
        public Turn makeTurn(Field field) throws GameInterruptedException {
            return new Turn(-1, -1);
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }
    }

    @BeforeEach
    public void cleanAIStorage() {
        aiStorage = new AIStorage();
    }

    @Test
    public void testAIStorageMapOrder() {
        AIStorage aiStorage = ReversiApplication.getInstance().getAiStorage();
        aiStorage.registerAIPlayer(new NamedSkippingPlayer("c"));
        aiStorage.registerAIPlayer(new NamedSkippingPlayer("b"));
        aiStorage.registerAIPlayer(new NamedSkippingPlayer("a179"));
        Object[] names = aiStorage.getPlayers().entrySet().stream().map(Map.Entry::getKey).toArray();
        Object[] exp = {"a179", "b", "c"};
        assertArrayEquals(exp, names);
    }

    @Test
    public void testAIStorageEqualKeys() {
        AIStorage aiStorage = ReversiApplication.getInstance().getAiStorage();
        aiStorage.registerAIPlayer(new NamedSkippingPlayer("aaaa"));
        aiStorage.registerAIPlayer(new NamedSkippingPlayer("aaaa"));
        assertThrows(RuntimeException.class, aiStorage::getPlayers);
    }
}