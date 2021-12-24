package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.Sound;

import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {

    private List<Bomb> _bombs;
    protected Keyboard _input;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0
        if (_input.space && _timeBetweenPutBombs < 0 && Game.getBombRate() > 0) {
            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile(_y - _sprite.getSize()+ 1);
            placeBomb(xt, yt);
            Game.addBombRate(-1);
            _timeBetweenPutBombs = 30;
        }

    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb e = new Bomb(x, y, _board);
        _board.addBomb(e);
        Sound.play("BOM_SET");
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
        Sound.play("Nope-sound-effect-www_tiengdong_com");
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        if (_input.down) {
            _moving = true;
            move(_x, _y + Game.getBomberSpeed());
        }
        if (_input.left) {
            _moving = true;
            move(_x - Game.getBomberSpeed(), _y);
        }
        if (_input.right) {
            _moving = true;
            move(_x + Game.getBomberSpeed(), _y);
        }
        if (_input.up) {
            _moving = true;
            move(_x, _y - Game.getBomberSpeed());
        }
        if (!(_input.up || _input.right || _input.left || _input.down)) {
            _moving = false;
        }
    }


    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không

        int x1 = Coordinates.pixelToTile(x + 1);
        int x2 = Coordinates.pixelToTile(x - 6 + Game.TILES_SIZE);
        int y1 = Coordinates.pixelToTile(y + 1 - Game.TILES_SIZE);
        int y2 = Coordinates.pixelToTile(y - 1);
        Entity a = _board.getEntity(x1, y1, this);
        Entity b = _board.getEntity(x2, y1, this);
        Entity c = _board.getEntity(x1, y2, this);
        Entity d = _board.getEntity(x2, y2, this);
        System.out.println(x);
        System.out.println(y);
        if (!a.collide(this)  ||!b.collide(this)  ||!c.collide(this) || !d.collide(this))
        return false;

        return true;
    }

    private void check(double xa, double ya) {
        if (xa != _x && _y == ya) {
            double near1 = ((int) ya / Game.TILES_SIZE) * Game.TILES_SIZE;
            double near2 = ((int) ya / Game.TILES_SIZE + 1) * Game.TILES_SIZE;
            if (ya - near1 <= 8) {
                if (canMove(xa, near1)) {
                    _y--;
                    check(xa, ya--);
                    if (xa > _x)
                        _direction = 4;
                    else
                        _direction = 3;
                }
            }
            if (near2 - ya <= 8) {
                if (canMove(xa, near2)) {
                    _y++;
                    move(xa, ya++);
                    if (xa > _x)
                        _direction = 4;
                    else
                        _direction = 3;
                }
            }
        } else if (xa == _x && _y != ya) {
            double near1 = ((int) xa / Game.TILES_SIZE) * Game.TILES_SIZE;
            double near2 = ((int) xa / Game.TILES_SIZE + 1) * Game.TILES_SIZE;
            if (xa - near1 <= 8) {
                if (canMove(near1, ya)) {
                    _x--;
                    check(xa--, ya);
                }
            }
            if (near2 - xa <= 8) {
                if (canMove(near2, ya)) {
                    _x++;
                    check(xa++, ya);
                    _direction = 1;
                }
            }
        }
    }

    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
        if (_y < ya) _direction = 2;
        if (_y > ya) _direction = 0;
        if (_x > xa) _direction = 3;
        if (_x < xa) _direction = 4;
        if (canMove(xa, ya)) {
            _x = xa;
            _y = ya;
        } else {
            check(xa, ya);
        }


    }


    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
        if (e instanceof Flame) {
            this.kill();
            return false;
        }
        if (e instanceof Enemy) {
            this.kill();
            return true;
        }
        return true;
    }

    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
}
