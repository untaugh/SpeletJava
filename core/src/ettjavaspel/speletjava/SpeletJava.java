package ettjavaspel.speletjava;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SpeletJava extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	private OrthographicCamera camera;
	Texture textureIce;
	Texture textureGreen;
	Texture texturePurpink;
	Texture textureYellow;
	Texture textureBlue;
	Board board;
	float pieceSize;
    boolean dragging;
    boolean marked;
    int dragStartX;
    int dragStartY;
    Position position;

	@Override
	public void create () {
		batch = new SpriteBatch();
		textureIce = new Texture("ice.png");
		textureGreen = new Texture("green.png");
		textureYellow = new Texture("yellow.png");
		textureBlue = new Texture("blue.png");
		texturePurpink = new Texture("purpink.png");

        marked = false;

		board = new Board(9,12);
        position = new Position(9, 12);

		camera = new OrthographicCamera();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		
		// Draw the board
		for (int col=0; col < board.pieces.length; col++) {
			for (int row=0; row < board.pieces[col].length; row++) {
				if (board.pieces[col][row].piececolor == Piece.PieceColor.ICE) {
					batch.draw(textureIce, col*pieceSize, row*pieceSize, pieceSize, pieceSize);
				}
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.GREEN) {
                    batch.draw(textureGreen, col * pieceSize, row * pieceSize, pieceSize, pieceSize);
                }
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.BLUE) {
						batch.draw(textureBlue, col * pieceSize, row * pieceSize, pieceSize, pieceSize);
                }
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.PURPINK) {
						batch.draw(texturePurpink, col * pieceSize, row * pieceSize, pieceSize, pieceSize);
                }
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.YELLOW) {
						batch.draw(textureYellow, col * pieceSize, row * pieceSize, pieceSize, pieceSize);
                }
			}
		}

		batch.end();
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        Vector3 mouse = new Vector3(screenX, screenY, 0);
        camera.unproject(mouse);
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        System.out.println("Pressed: " + (int)mouse.x + " " + (int)mouse.y);
        dragging = true;
        position = this.getPiece((int)mouse.x, (int)mouse.y);
        dragStartX = position.col;
        dragStartY = position.row;
		return true;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;

		Vector3 mouse = new Vector3(screenX, screenY, 0);
		camera.unproject(mouse);

		System.out.println("Released: " + mouse.x + " " + mouse.y);
        dragging = false;

		return true;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        Vector3 mouse = new Vector3(screenX, screenY, 0);
        camera.unproject(mouse);
        if(!dragging) return false;
        //System.out.println("Dragging");
        if(!marked) {
            position = getPiece((int)mouse.x, (int)mouse.y);
            Board.Direction dir;
            if(position.col != dragStartX || position.row != dragStartY) {
                if(board.pieces[position.col][position.row].piececolor != board.pieces[dragStartX][dragStartY].piececolor) {
                    dir = whichDirection(board, position, dragStartX, dragStartY);
                    board.Move(dragStartX, dragStartY, dir);
                    dragging = false;
                }
                else {

                    //TODO: Selecting of group

                    // Temporary lines:
                    System.out.println("Start of selecting group");
                    dragging = false;

                }
            }

        }
		return true;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public void resize (int width, int height) {
		camera.setToOrtho(false, width, height);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		float pieceWidth = (float)width/board.pieces.length;
		float pieceHeight = (float)height/board.pieces[0].length;
		pieceSize = Math.min(pieceWidth, pieceHeight);
		// viewport must be updated for it to work properly
		//viewport.update(width, height, true);
	}

	private Position getPiece(int x, int y) {

		float boardWidth = board.cols*pieceSize;
		float boardHeight = board.rows*pieceSize;

		int col;
		int row;


		if (x < boardWidth && y < boardHeight) {
			col = (int)( ((float)x/boardWidth)*board.cols);
			row = (int)( ((float)y/boardHeight)*board.rows);
		}
		else {
			return null;
		}

		return new Position(col, row);
	}

    private Board.Direction whichDirection(Board board, Position position, int dsX, int dsY) {
        if(position.col > dsX) {
            return Board.Direction.RIGHT;
        }
        else if(position.col < dsX) {
            return Board.Direction.LEFT;
        }
        else if(position.row > dsY) {
            return Board.Direction.UP;
        }
        else {
            return Board.Direction.DOWN;
        }
    }

}
