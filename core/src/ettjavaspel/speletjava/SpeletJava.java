package ettjavaspel.speletjava;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class SpeletJava extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
    Animation animation;
    Animation polyphony;
    Animation polyphony2;
	private OrthographicCamera camera;
	Texture textureIce;
	Texture textureIceSel;
	Texture textureGreen;
	Texture texturePurpink;
	Texture textureYellow;
	Texture textureBlue;
	Texture pixel;
    ScreenUtils fetchTexture;
	Board board;
	float pieceSize;
    boolean dragging = false;
    boolean marking = false;
    int dragStartX;
    int dragStartY;
    Position position;
	Music backgroundMusic;

	int pixelX[] = new int[1000];
	int pixelY[] = new int[1000];
	int pixelCounter;

	@Override
	public void create () {
		batch = new SpriteBatch();
		textureIce = new Texture("ice.png");
		textureIceSel = new Texture("ice_sel.png");
		textureGreen = new Texture("green.png");
		textureYellow = new Texture("yellow.png");
		textureBlue = new Texture("blue.png");
		texturePurpink = new Texture("purpink.png");

		board = new Board(7,11);
        position = new Position(7, 11);

        this.animation = new Animation();
        this.polyphony = new Animation();
        this.polyphony2 = new Animation();
        this.fetchTexture = new ScreenUtils();

        /*
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("laBamba.mp3"));
		backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(1);
		backgroundMusic.play();
		*/

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
				if(!board.pieces[col][row].moving) {
					if (board.pieces[col][row].selected) {
						batch.setColor(0.7f, 0.7f, 0.7f, 1);
					} else {
						batch.setColor(1, 1, 1, 1);
					}
					Texture texture = pixel;

					if (board.pieces[col][row].piececolor == Piece.PieceColor.ICE) {
						texture = textureIce;
					} else if (board.pieces[col][row].piececolor == Piece.PieceColor.GREEN) {
						texture = textureGreen;
					} else if (board.pieces[col][row].piececolor == Piece.PieceColor.BLUE) {
						texture = textureBlue;
					} else if (board.pieces[col][row].piececolor == Piece.PieceColor.PURPINK) {
						texture = texturePurpink;
					} else if (board.pieces[col][row].piececolor == Piece.PieceColor.YELLOW) {
						texture = textureYellow;
					}
					batch.draw(texture, col * pieceSize, row * pieceSize, pieceSize, pieceSize);
				}
			}
		}

        animation.render(batch, board);
        polyphony.render(batch, board);
        polyphony2.render(batch, board);

        /*
		for (int i=0; i<pixelCounter; i++) {
			batch.draw(pixel, pixelX[i], pixelY[i]);
		}
        */

        batch.end();

	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {

        if(animation.active && polyphony.active && polyphony2.active) return false;

        Vector3 mouse = new Vector3(screenX, screenY, 0);
        camera.unproject(mouse);
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
		pixelCounter = 0;
        dragging = true;
        Position pos = this.getPiece((int)mouse.x, (int)mouse.y);

        dragStartX = (int)mouse.x;
        dragStartY = (int)mouse.y;

		return true;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;

		Vector3 mouse = new Vector3(screenX, screenY, 0);
		camera.unproject(mouse);

		Position pos = this.getPiece((int)mouse.x, (int)mouse.y);
		Piece piece = board.GetPiece(pos.col, pos.row);

		if (!marking) {

			if (piece.selected) {
				System.out.println("selected");
				piece.selected = false;
			} else
			if (dragging && board.NextPiece(piece).length != 0 ) {
				board.DeselectAll();
				Piece ps[] = board.group(board.pieces[pos.col][pos.row]);
				board.select(ps);
			}

		}


		marking = false;
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

		if (marking) {
			Position startPosition = getPiece(dragStartX, dragStartY);
			Position currentPosition = getPiece((int) mouse.x, (int) mouse.y);

			if (startPosition == null || currentPosition == null) {
				return false;
			}
				Piece startPiece = board.pieces[startPosition.col][startPosition.row];
				Piece currentPiece = board.pieces[currentPosition.col][currentPosition.row];

				Piece group[] = board.group(startPiece);

				if ( board.contains(group, currentPiece)) {
					currentPiece.selected = true;
				} else {
					//dragging = false;
				}

		} else if (dragXD > (pieceSize * 0.75) || dragYD > (pieceSize * 0.75)) {
			Position startPosition = getPiece(dragStartX, dragStartY);
			Position endPosition = getPiece((int) mouse.x, (int) mouse.y);

			if (startPosition == null || endPosition == null) {
				return false;
			}

			Piece startPiece = board.pieces[startPosition.col][startPosition.row];
			Piece endPiece = board.pieces[endPosition.col][endPosition.row];

			if (startPiece.piececolor != endPiece.piececolor) {
				if(!animation.active) {
                    animation.animationSound1.dispose();
                    animation.animationSound2.dispose();
                    this.animation = new Animation();
                    animation.pieceSize = pieceSize;

                    this.animation.Move(startPosition.col, startPosition.row, this.whichDirection(startPosition, endPosition), board);

                    if (this.animation.active) {
                        animation.Step(board);
                    }
                    this.dragging = false;
                }
                else if(!polyphony.active) {
                    polyphony.animationSound1.dispose();
                    polyphony.animationSound2.dispose();
                    this.polyphony = new Animation();
                    polyphony.pieceSize = pieceSize;

                    this.polyphony.Move(startPosition.col, startPosition.row, this.whichDirection(startPosition, endPosition), board);

                    if (this.polyphony.active) {
                        polyphony.Step(board);
                    }
                    this.dragging = false;
                }
                else if(!polyphony2.active) {
                    polyphony2.animationSound1.dispose();
                    polyphony2.animationSound2.dispose();
                    this.polyphony2 = new Animation();
                    polyphony2.pieceSize = pieceSize;

                    this.polyphony2.Move(startPosition.col, startPosition.row, this.whichDirection(startPosition, endPosition), board);

                    if (this.polyphony2.active) {
                        polyphony2.Step(board);
                    }
                    this.dragging = false;
                }
			} else {
				board.DeselectAll();
				startPiece.selected = true;
				endPiece.selected = true;
				System.out.println("Start of selecting group");
				marking = true;
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
		animation.pieceSize = pieceSize = Math.min(pieceWidth, pieceHeight);
		//viewport should be updated for it to work properly:
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
