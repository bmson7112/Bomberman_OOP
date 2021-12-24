package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.character.Bomber;

public class Portal extends Tile {
	Board _board;
	public Portal(int x, int y, Sprite sprite, Board board) {
		super(x, y, sprite);
		_board = board;
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi Bomber đi vào
		if (e instanceof Bomber)
		{
			if (_board.detectNoEnemies() == false) return false;
			if (_board.detectNoEnemies() && e.getXTile() == getX() && e.getYTile() == getY())
			{
				_board.nextLevel();

			}
			return true;
		}
		return false;
	}

}
