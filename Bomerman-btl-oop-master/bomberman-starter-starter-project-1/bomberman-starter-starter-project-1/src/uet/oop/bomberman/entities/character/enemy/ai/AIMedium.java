package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIMedium extends AI {
    Bomber _bomber;
    Enemy _e;

    public AIMedium(Bomber bomber, Enemy e) {
        _bomber = bomber;
        _e = e;
    }

    @Override
    public int calculateDirection() {
        // TODO: cài đặt thuật toán tìm đường đi
        if (_bomber == null) return random.nextInt(4);

        //lua theo
        if (_bomber.getXTile() < _e.getXTile())
            return 3;
        else if (_bomber.getXTile() > _e.getXTile())
            return 1;
        if (_bomber.getYTile() < _e.getYTile())
            return 0;
        else if (_bomber.getYTile() > _e.getYTile())
            return 2;
        return -1;
    }

}