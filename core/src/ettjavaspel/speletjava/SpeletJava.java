package ettjavaspel.speletjava;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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
	Texture pixel;
	Board board;
	float pieceSize;
    boolean dragging;
    boolean marked;
    int dragStartX;
    int dragStartY;
    Position position;

	int pixelX[] = new int[1000];
	int pixelY[] = new int[1000];
	int pixelCounter;

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

		Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0, 0, 3, 3);
		pixel = new Texture(pixmap, Pixmap.Format.RGB888, false);
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

		for (int i=0; i<pixelCounter; i++) {
			batch.draw(pixel, pixelX[i], pixelY[i]);
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
		pixelCounter = 0;
        System.out.println("Pressed: " + (int)mouse.x + " " + (int)mouse.y);
        dragging = true;
        position = this.getPiece((int)mouse.x, (int)mouse.y);

        dragStartX = (int)mouse.x;
        dragStartY = (int)mouse.y;
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

		if (pixelCounter < 1000) {
			pixelX[pixelCounter] = (int) mouse.x;
			pixelY[pixelCounter] = (int) mouse.y;
			pixelCounter++;
		}

		if(!dragging) return false;

		int dragXD = Math.abs((int) mouse.x - dragStartX);
		int dragYD = Math.abs((int) mouse.y - dragStartY);

		System.out.println("drag: " + dragXD + " " + dragYD);

		if (dragXD > pieceSize || dragYD > pieceSize) {
			Position startPosition = getPiece(dragStartX, dragStartY);
			Position endPosition = getPiece((int) mouse.x, (int) mouse.y);

			if (startPosition == null || endPosition == null) {
				return false;
			}

			Piece startPiece = board.pieces[startPosition.col][startPosition.row];
			Piece endPiece = board.pieces[endPosition.col][endPosition.row];

			if (startPiece.piececolor != endPiece.piececolor) {
				Board.Direction dir = whichDirection(startPosition, endPosition);
				board.Move(startPosition.col, startPosition.row, dir);
				dragging = false;
			} else {
				//TODO: Selecting of group
				// Temporary lines:
				System.out.println("Start of selecting group");
				dragging = false;
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

    private Board.Direction whichDirection(Position startPosition, Position endPosition) {
        if(startPosition.col < endPosition.col && startPosition.row == endPosition.row) {
            return Board.Direction.RIGHT;
        }
        else if(startPosition.col > endPosition.col && startPosition.row == endPosition.row) {
            return Board.Direction.LEFT;
        }
        else if(startPosition.row < endPosition.row && startPosition.col == endPosition.col) {
            return Board.Direction.UP;
        }
        else if(startPosition.row > endPosition.row && startPosition.col == endPosition.col) {
            return Board.Direction.DOWN;
        } else {
			return Board.Direction.NONE;
		}
    }

}
