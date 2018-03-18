package ru.spbau.alferov.javahw.reversi.stats;

import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.reversi.ReversiApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsController {
    @NotNull
    public List<PlayedGame> stats = new ArrayList<>();

    public void add(PlayedGame game) {
        Collections.reverse(stats);
        stats.add(game);
        Collections.reverse(stats);
        ReversiApplication.getInstance().updateStatsList(stats);
    }
}
