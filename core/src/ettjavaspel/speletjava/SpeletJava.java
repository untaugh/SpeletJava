package ettjavaspel.speletjava;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpeletJava extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	Texture img;
	Texture textureIce;
	Texture textureGreen;
	Texture texturePurpink;
	Texture textureYellow;
	Texture textureBlue;
	Board board;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("ice.png");
		textureIce = new Texture("ice.png");
		textureGreen = new Texture("green.png");
		textureYellow = new Texture("yellow.png");
		textureBlue = new Texture("blue.png");
		texturePurpink = new Texture("purpink.png");

		board = new Board();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1500, 1500);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		// Draw the board
		for (int col=0; col < board.pieces.length; col++) {
			for (int row=0; row < board.pieces[col].length; row++) {
				if (board.pieces[col][row].piececolor == Piece.PieceColor.ICE) {
					batch.draw(textureIce, col*150, row*150);
				}
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.GREEN) {
                    batch.draw(textureGreen, col * 150, row * 150);
                }
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.BLUE) {
                    batch.draw(textureBlue, col*150, row*150);
                }
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.PURPINK) {
                    batch.draw(texturePurpink, col*150, row*150);
                }
                else if (board.pieces[col][row].piececolor == Piece.PieceColor.YELLOW) {
                    batch.draw(textureYellow, col*150, row*150);
                }
			}
		}

		batch.end();
	}
}
