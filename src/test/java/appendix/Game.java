package appendix;

public class Game {

    private GameNumGen gameNumGen;
    public Game(GameNumGen gameNumGen) {
        this.gameNumGen =  gameNumGen;
    }

    public void init(GameLevel easy) {
        this.gameNumGen.generate(easy);
    }
}
