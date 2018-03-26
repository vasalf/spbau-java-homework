package ru.spbau.alferov.javahw.reversi.stats;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class handles all of the stats-related actions.
 */
public class StatisticsController {
    /**
     * List of the played games.
     */
    @NotNull
    private List<PlayedGame> stats = new ArrayList<>();

    /**
     * Adds a new played game to the list.
     */
    public void add(@NotNull PlayedGame game) {
        Collections.reverse(stats);
        stats.add(game);
        Collections.reverse(stats);
        ReversiApplication.getInstance().updateStatsList(stats);
    }
}
