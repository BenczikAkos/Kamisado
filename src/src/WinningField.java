package src;

public class WinningField extends Field {

	WinningField(Game g) {
		super(g);
	}
	public void entered(Tower t) {
		if(tower)
		currGame.turnPassed();
		currTower = t;
		for(Tower samecolor: sameColorTowers) {
			if(samecolor.activeRound())
				samecolor.activate();
		}

	}
}
