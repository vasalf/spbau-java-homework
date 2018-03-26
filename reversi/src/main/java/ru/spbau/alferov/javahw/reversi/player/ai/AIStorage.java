package ru.spbau.alferov.javahw.reversi.player.ai;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is the storage of AI players.
 */
public class AIStorage {
    /**
     * Current list of players.
     */
    @NotNull
    private List<AIPlayer> players = new ArrayList<>();

    /**
     * Maps the player name to the Player instance.
     */
    public @NotNull Map<String, Player> getPlayers() {
        return players.stream().collect(Collectors.toMap(AIPlayer::getName,
                                                         Function.identity(),
                                                         (v1, v2) -> { throw new RuntimeException("Keys equal"); },
                                                         TreeMap::new));
    }

    /**
     * Adds a new player to the storage.
     * The players name should be distinct.
     */
    public void registerAIPlayer(AIPlayer player) {
        players.add(player);
    }
}
