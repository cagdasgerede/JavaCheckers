package pl.nogacz.checkers.achievements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AchievementTest {

    @Test void test_nullParameter(){
        AllUnlockedAch ach1 = new AllUnlockedAch("AllUnlockedAch",false);
        LongGameAch ach2 = new LongGameAch("LongGameAch", false);
        NumberOfMovesAch ach3= new NumberOfMovesAch("NumberOfMovesAch",false);
        ShortGameAch ach4 = new ShortGameAch("ShortGameAch",false);

        assertThrows(
            IllegalArgumentException.class, 
            () -> { ach1.checkAchievement(null, 0, 0); });

        assertThrows(
            IllegalArgumentException.class, 
            () -> { ach2.checkAchievement(null, 0, 0); });  

        assertThrows(
            IllegalArgumentException.class, 
            () -> { ach3.checkAchievement(null, 0, 0); });
    
        assertThrows(
            IllegalArgumentException.class, 
             () -> { ach4.checkAchievement(null, 0, 0); }); 
 
    }

    @Test void test_AllUnlockedAch_check(){
        Achievement [] allAchievements = new Achievement[4];
        allAchievements[0] = new AllUnlockedAch("AllUnlockedAch",false);
        allAchievements[1] = new LongGameAch("LongGameAch",true);
        allAchievements[2] = new NumberOfMovesAch("NumberOfMovesAch",true);
        allAchievements[3] = new ShortGameAch("ShortGameAch",true);

        allAchievements[0].checkAchievement(allAchievements, 0, 0);

        assertTrue(allAchievements[0].getisAchUnlocked());
    }

    @Test void test_LongGameAch_check(){
        Achievement [] allAchievements = new Achievement[1];

        LongGameAch ach1 = new LongGameAch("LongGameAch",false);
        LongGameAch ach2 = new LongGameAch("LongGameAch",false);

        ach1.checkAchievement(allAchievements, 0, 10);
        ach2.checkAchievement(allAchievements, 0, 200);
        
        assertFalse(ach1.getisAchUnlocked());
        assertTrue(ach2.getisAchUnlocked());

    }

    @Test void test_NumberOfMoves_check(){
        Achievement [] allAchievements = new Achievement[1];

        NumberOfMovesAch ach1 = new NumberOfMovesAch("NumberOfMovesAch",false);
        NumberOfMovesAch ach2 = new NumberOfMovesAch("NumberOfMovesAch",false);

        ach1.checkAchievement(allAchievements, 10, 0);
        ach2.checkAchievement(allAchievements, 50, 0);
        
        assertFalse(ach1.getisAchUnlocked());
        assertTrue(ach2.getisAchUnlocked());

    }

    @Test void test_ShortGameAch_check(){
        Achievement [] allAchievements = new Achievement[1];

        ShortGameAch ach1 = new ShortGameAch("ShortGameAch",false);
        ShortGameAch ach2 = new ShortGameAch("ShortGameAch",false);

        ach1.checkAchievement(allAchievements, 0, 10);
        ach2.checkAchievement(allAchievements, 0, 200);
        
        assertTrue(ach1.getisAchUnlocked());
        assertFalse(ach2.getisAchUnlocked());

    }

}
