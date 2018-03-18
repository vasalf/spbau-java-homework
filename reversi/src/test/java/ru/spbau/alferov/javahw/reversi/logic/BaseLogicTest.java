package ru.spbau.alferov.javahw.reversi.logic;

import org.junit.jupiter.api.BeforeAll;
import ru.spbau.alferov.javahw.reversi.BaseTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseLogicTest extends BaseTest {
    private static java.lang.reflect.Field fieldField;
    private static java.lang.reflect.Field fieldBlackScore;
    private static java.lang.reflect.Field fieldWhiteScore;
    private static java.lang.reflect.Field fieldBlackTurn;

    private static java.lang.reflect.Field gameField;

    @BeforeAll
    public static void setUpFields() {
        try {
            fieldField = Field.class.getDeclaredField("field");
            fieldField.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Field.field is not declared", ex);
        }
        try {
            fieldBlackScore = Field.class.getDeclaredField("blackScore");
            fieldBlackScore.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Field.blackScore is not declared", ex);
        }
        try {
            fieldWhiteScore = Field.class.getDeclaredField("whiteScore");
            fieldWhiteScore.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Field.whiteScore is not declared", ex);
        }
        try {
            fieldBlackTurn = Field.class.getDeclaredField("blackTurn");
            fieldBlackTurn.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Field.blackTurn is not declared", ex);
        }
        try {
            gameField = Game.class.getDeclaredField("field");
            gameField.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Game.field is not declared", ex);
        }
    }

    protected static void assertTurnSetsEqual(List<Turn> expected, List<Turn> actual) {
        assertEquals(expected.size(), actual.size());
        boolean[] used = new boolean[expected.size()];
        for (int i = 0; i < expected.size(); i++)
            used[i] = false;
        for (int i = 0; i < actual.size(); i++) {
            boolean found = false;
            for (int j = 0; j < expected.size() && !found; j++) {
                if (actual.get(j).getRow() == expected.get(i).getRow()
                        && actual.get(j).getColumn() == expected.get(i).getColumn()
                        && !used[j]){
                    used[j] = true;
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    protected Field createFieldFromPosition(String[] position) {
        Field ret = new Field();
        SquareType[][] toBePut = new SquareType[8][8];
        int whiteScore = 0, blackScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[7 - i].charAt(j) == '.') {
                    toBePut[i][j] = SquareType.EMPTY;
                } else if (position[7 - i].charAt(j) == 'B') {
                    toBePut[i][j] = SquareType.BLACK;
                    blackScore++;
                } else {
                    toBePut[i][j] = SquareType.WHITE;
                    whiteScore++;
                }
            }
        }
        try {
            fieldField.set(ret, toBePut);
            fieldBlackScore.setInt(ret, blackScore);
            fieldWhiteScore.setInt(ret, whiteScore);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Illegal access occurred while trying to setup a field", ex);
        }
        return ret;
    }

    protected String[] createPositionFromField (Field field) {
        SquareType[][] inner;
        try {
            inner = (SquareType[][])fieldField.get(field);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Illegal access occurred while trying to read a field", ex);
        }

        String[] ret = new String[8];
        for (int i = 0; i < 8; i++) {
            char[] ans = new char[8];
            for (int j = 0; j < 8; j++) {
                if (inner[i][j] == SquareType.EMPTY) {
                    ans[j] = '.';
                } else if (inner[i][j] == SquareType.BLACK) {
                    ans[j] = 'B';
                } else {
                    ans[j] = 'W';
                }
            }
            ret[7 - i] = new String(ans);
        }
        return ret;
    }

    protected void setBlackTurnInField(Field field, boolean blackTurn) {
        try {
            fieldBlackTurn.setBoolean(field, blackTurn);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Illegal access occurred while trying to set the turn for player", ex);
        }
    }

    protected void setGameField(Game game, Field field) {
        try {
            gameField.set(game, field);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Illegal access occurred while trying to set the field for game");
        }
    }

    protected Field getGameField(Game game) {
        try {
            return (Field)gameField.get(game);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Illegal access occurred while trying to get the game field");
        }
    }
}
