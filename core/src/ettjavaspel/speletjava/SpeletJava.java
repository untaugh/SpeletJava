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
	Board board;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("ice.png");
		textureIce = new Texture("ice.png");
		textureGreen = new Texture("green.png");

		board = new Board();
		board.pieces[5][3].piececolor = Piece.PieceColor.GREEN;
		board.pieces[2][7].piececolor = Piece.PieceColor.GREEN;
		board.pieces[4][4].piececolor = Piece.PieceColor.GREEN;
		camera = new OrthographicCamera();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera.setToOrtho(false, width, height);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		float pieceWidth = width/10;
		float pieceHeight = height/10;
		float pieceSize = Math.min(pieceWidth,pieceHeight);

		// Draw the board
		for (int col=0; col < board.pieces.length; col++) {
			for (int row=0; row < board.pieces[col].length; row++) {
				if (board.pieces[col][row].piececolor == Piece.PieceColor.ICE) {
					batch.draw(textureIce, col*pieceSize, row*pieceSize, pieceSize, pieceSize);
				} else {
					batch.draw(textureGreen, col*pieceSize, row*pieceSize, pieceSize, pieceSize);
				}
			}
		}

		batch.end();
	}
}
