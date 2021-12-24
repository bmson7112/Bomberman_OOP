package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;


public class AIHight extends AI {
    Enemy _e;
    Bomb _bomb;
    Bomber _bomber;

    public AIHight(Bomb bomb, Enemy e) {
        _bomb = bomb;
        _e = e;
    }

    @Override
    public int calculateDirection() {
        // TODO: cài đặt thuật toán tìm đường đi
        if (_bomber == null) return random.nextInt(4);
        //if (_bomb == null) return random.nextInt(4);

        if (_bomb.getXTile() >= (_e.getXTile()) )
            return 3;
        else if (_bomb.getXTile() <= (_e.getXTile()))
            return 1;
        else if (_bomb.getYTile() >= (_e.getYTile()) )
            return 0;
        else if (_bomb.getYTile() <= (_e.getYTile()))
            return 2;
        return -1;
    }

}