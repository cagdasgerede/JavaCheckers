package pl.nogacz.checkers.achievements;

public class ShortGameAch extends Achievement{

    public ShortGameAch(String AchName, boolean isAchUnlocked){
        super(AchName, isAchUnlocked);
    }

    public void checkAchievement(Achievement [] achievements, int countOfMoves, long countOfTime){
        if(achievements == null)
            throw new IllegalArgumentException();

        boolean unlock = false;

        if(countOfTime < 45)
            unlock = true;

        if(unlock == true){
            UnlockAch();
            showAchievement();
        }

    }
    
}
