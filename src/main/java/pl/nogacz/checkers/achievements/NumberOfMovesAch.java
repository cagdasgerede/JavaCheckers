package pl.nogacz.checkers.achievements;

public class NumberOfMovesAch extends Achievement{

    public NumberOfMovesAch(String AchName, boolean isAchUnlocked){
        super(AchName, isAchUnlocked);
    }

    public void checkAchievement(Achievement [] achievements, int countOfMoves, long countOfTime){
        if(achievements == null)
            throw new IllegalArgumentException();

        boolean unlock = false;

        if(countOfMoves > 25)
            unlock = true;

        if(unlock == true && getisAchUnlocked() == false){
            UnlockAch();
            showAchievement();
        }

    }
    
}
