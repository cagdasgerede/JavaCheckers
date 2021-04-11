package pl.nogacz.checkers.achievements;

public class LongGameAch extends Achievement{

    public LongGameAch(String AchName, boolean isAchUnlocked){
        super(AchName, isAchUnlocked);
    }

    public void checkAchievement(Achievement [] achievements, int countOfMoves, long countOfTime){
        if(achievements == null)
            throw new IllegalArgumentException();

        boolean unlock = false;

        if(countOfTime > 120)
            unlock = true;

        if(unlock == true){
            UnlockAch();
            showAchievement();
        }

    }
    
}
