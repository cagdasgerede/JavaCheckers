package pl.nogacz.checkers.achievements;

public class AllUnlockedAch extends Achievement{

    public AllUnlockedAch(String AchName, boolean isAchUnlocked){
        super(AchName, isAchUnlocked);
    }

    public void checkAchievement(Achievement [] achievements, int countOfMoves, long countOfTime){
        if(achievements == null)
            throw new IllegalArgumentException();

        boolean unlock = true;

        for(int i=1 ; i< achievements.length ; i++){
            if(achievements[i].getisAchUnlocked() == false)
                unlock = false;
        }

        if(unlock == true && getisAchUnlocked() == false){
            UnlockAch();
            showAchievement();
        }
    }
    
}
