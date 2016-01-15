package ettjavaspel.speletjava;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	@Override
	public void create () {
		batch = new SpriteBatch();
		textureIce = new Texture("ice.png");
		textureGreen = new Texture("green.png");
		textureYellow = new Texture("yellow.png");
		textureBlue = new Texture("blue.png");
		texturePurpink = new Texture("purpink.png");

		board = new Board(9,12);
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

    boolean dragging;

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
        System.out.println("Pressed: " + screenX + " " + screenY);
        dragging = true;
		return true;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		System.out.println("Released: " + screenX + " " + screenY);
        dragging = false;
		board.Move(5,5, Board.Direction.UP);
		return true;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
        if(!dragging) return false;
        System.out.println("Dragging");
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

}
