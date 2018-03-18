package ru.spbau.alferov.javahw.reversi.player.ai;

import ru.spbau.alferov.javahw.reversi.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AIStorage {
    private List<AIPlayer> players = new ArrayList<>();

    public Map<String, Player> getPlayers() {
        return players.stream().collect(Collectors.toMap(AIPlayer::getName,
                                                         Function.identity(),
                                                         (v1, v2) -> { throw new RuntimeException("Keys equal"); },
                                                         TreeMap::new));
    }

    public void registerAIPlayer(AIPlayer player) {
        players.add(player);
    }
}
