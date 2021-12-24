package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.enemy.ai.AIMedium;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

public class Kondoria extends Enemy {
    // di xuyen tuong lua ng
    public Kondoria(int x, int y, Board board) {
        super(x, y, board, Sprite.kondoria_dead, 0.5, 500);

        _sprite = Sprite.kondoria_left1;

        _ai = new AIMedium(_board.getBomber(), this);
        _direction = _ai.calculateDirection();
    }

    @Override
    protected void chooseSprite() {
        switch (_direction) {
            case 0:
            case 1:
                if (_moving)
                    _sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, _animate, 60);
                else
                    _sprite = Sprite.kondoria_left1;
                break;
            case 2:
            case 3:
                if (_moving)
                    _sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, _animate, 60);
                else
                    _sprite = Sprite.kondoria_left1;
                break;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không

        double xr = _x, yr = _y - 16; //subtract y to get more accurate results

        //the thing is, subract 15 to 16 (sprite size), so if we add 1 tile we get the next pixel tile with this
        //we avoid the shaking inside tiles with the help of steps
        if (_direction == 0) {
            yr += _sprite.getSize() - 1;
            xr += _sprite.getSize() / 2;
        }
        if (_direction == 1) {
            yr += _sprite.getSize() / 2;
            xr += 1;
        }
        if (_direction == 2) {
            xr += _sprite.getSize() / 2;
            yr += 1;
        }
        if (_direction == 3) {
            xr += _sprite.getSize() - 1;
            yr += _sprite.getSize() / 2;
        }

        int xx = Coordinates.pixelToTile(xr) + (int) x;
        int yy = Coordinates.pixelToTile(yr) + (int) y;

        Entity a = _board.getEntity(xx, yy, this); //entity of the position we want to go

        if (a instanceof Wall) {
            return false;
        }
        return true;
    }

}
