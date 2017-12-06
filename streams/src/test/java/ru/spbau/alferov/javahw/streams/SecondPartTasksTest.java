package ru.spbau.alferov.javahw.streams;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static ru.spbau.alferov.javahw.streams.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        ClassLoader classLoader = getClass().getClassLoader();
        File quote1 = new File("src/test/resources/quotes1.txt");
        File quote2 = new File("src/test/resources/quotes2.txt");
        assertEquals(ImmutableList.of(
                    "He smote the Picts in battle,",
                    "Quoth the Pict to the King.",
                    "The Picts were a tribal confederation of peoples who lived in",
                    "Pictish society was typical of many Iron Age societies in"
                ),
                findQuotes(ImmutableList.of(quote1.getAbsolutePath(), quote2.getAbsolutePath()), "Pict"));
    }

    @Test
    public void testPiDividedBy4() {
        double x = piDividedBy4();
        assertTrue(x >= 0);
        assertTrue(x <= 1);
    }

    @Test
    public void testFindPrinter() {
        List<String> listLeoTolstoy = ImmutableList.of(
                "— Eh bien, mon prince. Gênes et Lucques ne sont plus que des apanages, des поместья, de la famille Buonaparte. Non, je vous préviens que si vous ne me dites pas que nous avons la guerre, si vous vous permettez encore de pallier toutes les infamies, toutes les atrocités de cet Antichrist (ma parole, j'y crois) — je ne vous connais plus, vous n'êtes plus mon ami, vous n'êtes plus мой верный раб, comme vous dites",
                "Вечер Анны Павловны был пущен. Веретена с разных сторон равномерно и не умолкая шумели. Кроме ma tante, около которой сидела только одна пожилая дама с исплаканным, худым лицом, несколько чужая в этом блестящем обществе, общество разбилось на три кружка. В одном, более мужском, центром был аббат; в другом, молодом, — красавица княжна Элен, дочь князя Василия, и хорошенькая, румяная, слишком полная по своей молодости, маленькая княгиня Болконская. В третьем — Мортемар и Анна Павловна.",
                "В четвертому часу вечера князь Андрей, настояв на своей просьбе у Кутузова, приехал в Грунт и явился к Багратиону. Адъютант Бонапарте еще не приехал в отряд Мюрата, и сражение еще не начиналось. В отряде Багратиона ничего не знали об общем ходе дел, говорили о мире, но не верили в его возможность. Говорили о сражении и тоже не верили и в близость сражения.",
                "Николай, как и всегда, замучив две пары лошадей и то не успев побывать во всех местах, где ему надо было быть и куда его звали, приехал домой перед самым обедом. Как только он вошел, он заметил и почувствовал напряженность любовной атмосферы в доме, но, кроме того, он заметил странное замешательство, царствующее между некоторыми из членов общества. Особенно взволнованы были Соня, Долохов, старая графиня и немного Наташа. Николай понял, что что-то должно было случиться до обеда между Соней и Долоховым, и, с свойственною ему чуткостью сердца, был очень нежен и осторожен во время обеда в обращении с ними обоими. В этот же вечер третьего дня праздников должен был быть один из тех балов у Иогеля (танцевального учителя), которые он давал по праздникам для всех своих учеников и учениц.",
                "— Non, ce n'est rien, je voulais dire seulement... 4 (Он намерен был повторить шутку, которую он слышал в Вене и которую он целый вечер собирался поместить.) Je voulais dire seulement, que nous avons tort de faire la guerre pour le Roi de Prusse 5."
        );
        List<String> listAnthonyBurgess = ImmutableList.of(
                "There was me, that is Alex, and my three droogs, that is Pete, Georgie, and Dim. Dim being really dim, and we sat in the Korova Milkbar making up our rassoodocks what to do with the evening, a flip dark chill winter bastard though dry.",
                "Then we saw one young malchick with his sharp, lubbilubbing under a tree, so we stopped and cheered at them, then we bashed into them both with a couple of half-hearted tolchocks, making them cry, and on we went. What we were after now was the old surprise visit. That was a real kick and good for smecks and lashings of the ultra-violent. We came at last to a sort of village, and just outside this village was a small sort of a cottage on its own with a bit of garden.",
                "The devotchka sort of hesitated and then said: “Wait.” Then she went off, and my three droogs had got out of the auto quiet and crept up horrorshow stealthy, putting their maskies on now, then I put mine on, then it was only a matter of me putting in the old rooker and undoing the chain, me having softened up this devotchka with my gent’s goloss, so that she hadn’t shut the door like she should have done, us being strangers of the night. The four of us then went roaring"
        );
        List<String> listToveJannson = ImmutableList.of(
                "It must have been late in the afternoon one day at the end of August when Moomintroll and his mother arrived at the deepest part of the great forest. It was completely quiet, and so dim between the trees that it was as though twilight had already fallen. Here and there giant flowers grew, glowing with a peculiar light like flickering lamps, and furthest in among the shadows small, cold green points moved.",
                "‘I’m a moomintroll,’ answered Moomintroll, who had got his courage back. ‘And this is my mother. I hope we didn’t disturb you.’ (You can see that his mother had taught him to be polite.)",
                "It was quite hard to climb up the rope-ladder, especially for Moomintroll and his mother, as they had such short legs. ‘Now you must dry your feet,’ said the old gentleman, and drew the ladder up after them. Then he closed the door very carefully, so that nothing harmful could sneak inside. They all went up a moving staircase that carried them right inside the mountain. ‘Are you sure this gentleman is to be trusted?’ whispered the small creature. ‘Remember, on your own heads be it.’ And then he made himself as small as he could and hid behind Moominmamma. Then a bright light shone towards them, and the moving staircase took them straight into a wonderful landscape."
        );

        Map<String, List<String>> printer = ImmutableMap.of(
                "Leo Tolstoy", listLeoTolstoy,
                "Anthony Burgess", listAnthonyBurgess,
                "Tove Jannson", listToveJannson
        );
        assertEquals("Leo Tolstoy", findPrinter(printer));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> m1 = ImmutableMap.of(
                "Clockwork Orange", 1,
                "1984", 10,
                "Brave New World", 3,
                "Fahrenheit 451", 17
        ), m2 = ImmutableMap.of(
                "Fahrenheit 451", 4,
                "Clockwork Orange", 179
        ), m3 = ImmutableMap.of(
                "1984", 20,
                "Brave New World", 30
        );
        List<Map<String, Integer>> test = ImmutableList.of(m1, m2, m3);
        assertEquals(calculateGlobalOrder(test), ImmutableMap.of(
                    "Clockwork Orange", 180,
                    "1984", 30,
                    "Brave New World", 33,
                    "Fahrenheit 451", 21
                ));
    }
}