package ettjavaspel.speletjava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class Animation
{
    boolean active;
    public float backHeight;
    public float backWidth;
    public float backX;
    public float backY;
    public options bearing;
    public float currentCoordinateX;
    public float currentCoordinateY;
    public int currentPositionX;
    public int currentPositionY;
    public Board.Direction direction;
    public int endPositionX;
    public int endPositionY;
    public TextureRegion leftPart;
    public float oneX;
    public float oneY;
    public int positionStepSizeX;
    public int positionStepSizeY;
    public TextureRegion rightPart;
    public float twoX;
    public float twoY;
    public float stepSize;
    public float pieceSize;
    public float tempEndCoordinateX;
    public float tempEndCoordinateY;
    Texture textureHorisontal;
    Texture textureVertical;

    Sound animationSound1;
    Sound animationSound2;

    public Animation() {
        this.active = false;
        this.currentPositionY = 0;
        this.currentPositionX = 0;
        this.textureVertical = new Texture("vertical.png");
        this.textureHorisontal = new Texture("horisontal.png");
        animationSound1 = Gdx.audio.newSound(Gdx.files.internal("MA.mp3"));
        animationSound2 = Gdx.audio.newSound(Gdx.files.internal("RS.mp3"));
    }

    public enum options
    {
        HORISONTAL,
        VERTICAL
    }


    public boolean Move(final int currentPositionX, final int currentPositionY, final Board.Direction direction, Board board) {
        if (!this.MoveTest(currentPositionX, currentPositionY, direction, board)) {
            return false;
        }
        int n2 = 0;
        int n3 = 0;
        this.stepSize = 0.0f;
        this.backHeight = this.pieceSize;
        this.backWidth = this.pieceSize;
        switch (direction) {
            case UP: {
                n3 = 1;
                this.backX = currentPositionX * this.pieceSize;
                this.backY = currentPositionY * this.pieceSize;
                this.positionStepSizeY = 1;
                this.backHeight = 2.0f * this.pieceSize;
                this.bearing = Animation.options.VERTICAL;
                break;
            }
            case DOWN: {
                n3 = -1;
                this.backX = currentPositionX * this.pieceSize;
                this.backY = (currentPositionY * this.pieceSize) - this.pieceSize;
                this.positionStepSizeY = -1;
                this.backHeight = 2.0f * this.pieceSize;
                this.bearing = Animation.options.VERTICAL;
                break;
            }
            case LEFT: {
                n2 = -1;
                this.backX = (currentPositionX * this.pieceSize) - this.pieceSize;
                this.backY = currentPositionY * this.pieceSize;
                this.positionStepSizeX = -1;
                this.backWidth = 2.0f * this.pieceSize;
                this.bearing = Animation.options.HORISONTAL;
                break;
            }
            case RIGHT: {
                n2 = 1;
                this.backX = currentPositionX * this.pieceSize;
                this.backY = currentPositionY * this.pieceSize;
                this.positionStepSizeX = 1;
                this.backWidth = 2.0f * this.pieceSize;
                this.bearing = Animation.options.HORISONTAL;
                break;
            }
        }
        this.active = true;
        this.stepSize = this.pieceSize / 4f;
        this.direction = direction;
        this.currentPositionX = currentPositionX;
        this.currentPositionY = currentPositionY;
        for (int endPositionX = currentPositionX + n2, endPositionY = currentPositionY + n3; board.pieces[currentPositionX][currentPositionY].piececolor != board.pieces[endPositionX][endPositionY].piececolor; endPositionX += n2, endPositionY += n3) {
            this.endPositionX = endPositionX;
            this.endPositionY = endPositionY;
        }
        return true;
    }

    private boolean MoveTest(int n, int n2, final Board.Direction direction, Board board) {

        final Piece.PieceColor piececolor = board.pieces[n][n2].piececolor;

        int n3 = 0;
        int n4 = 0;
        switch (direction) {
            case UP: {
                n4 = 1;
                break;
            }
            case DOWN: {
                n4 = -1;
                break;
            }
            case LEFT: {
                n3 = -1;
                break;
            }
            case RIGHT: {
                n3 = 1;
                break;
            }
        }
        final int n5 = n + n3;
        n = n2 + n4;

        if (n >= 0 && n < board.rows && n5 >= 0 && n5 < board.cols && board.pieces[n5][n].piececolor != piececolor) {
            for (n2 = n5; n2 >= 0 && n2 < board.cols && n >= 0 && n < board.rows; n2 += n3, n += n4) {
                if (board.pieces[n2][n].piececolor == piececolor) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean Step(Board board) {

        final int n = this.currentPositionX;
        final int n2 = this.currentPositionY;

        animationSound1.play(0.5f);

        if (n2 >= 0 && n2 < board.rows && n >= 0 && n < board.cols) {
            int n3 = 0;
            int n4 = 0;
            switch (this.direction) {
                case UP: {
                    n3 = n2 + 1;
                    this.currentCoordinateX = this.oneX = this.twoX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.oneY = this.currentPositionY * this.pieceSize;
                    this.twoY = this.oneY + pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX ;
                    this.tempEndCoordinateY = this.currentCoordinateY + this.pieceSize;
                    break;
                }
                case DOWN: {
                    n3 = n2 - 1;
                    this.currentCoordinateX = this.oneX = this.twoX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.twoY = this.currentPositionY * this.pieceSize;
                    this.oneY = this.twoY - pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX;
                    this.tempEndCoordinateY = this.currentCoordinateY;
                    break;
                }
                case LEFT: {
                    n4 = n - 1;
                    this.currentCoordinateX = this.twoX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.twoY = this.oneY = this.currentPositionY * this.pieceSize;
                    this.oneX = this.twoX - pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX;
                    this.tempEndCoordinateY = this.currentCoordinateY;
                    break;
                }
                case RIGHT: {
                    n4 = n + 1;
                    this.currentCoordinateX = this.oneX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.oneY = this.twoY = this.currentPositionY * this.pieceSize;
                    this.twoX = this.oneX + this.pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX + this.pieceSize;
                    this.tempEndCoordinateY = this.currentCoordinateY;
                    break;
                }
            }
            if (n3 >= 0 && n3 < board.rows && n4 >= 0 && n4 < board.cols) {
                this.leftPart = ScreenUtils.getFrameBufferTexture((int) this.oneX, (int) this.oneY, (int) this.pieceSize, (int) this.pieceSize);
                this.rightPart = ScreenUtils.getFrameBufferTexture((int)(this.twoX), (int)this.twoY, (int)this.pieceSize, (int)this.pieceSize);
                final Piece piece = board.pieces[this.currentPositionX][this.currentPositionY];
                board.pieces[this.currentPositionX][this.currentPositionY] = board.pieces[this.currentPositionX + this.positionStepSizeX][this.currentPositionY + this.positionStepSizeY];
                board.pieces[this.currentPositionX + this.positionStepSizeX][this.currentPositionY + this.positionStepSizeY] = piece;
                this.currentPositionX += this.positionStepSizeX;
                this.currentPositionY += this.positionStepSizeY;
                if (n == this.endPositionX && n2 == this.endPositionY) {
                    this.active = false;
                    animationSound2.play(0.2f);
                }
                return true;
            }
        }
        return false;
    }

    public void render(SpriteBatch batch, Board board) {

        if (this.active) {
            if (this.direction == Board.Direction.RIGHT && this.oneX > this.tempEndCoordinateX) {
                this.backX += this.pieceSize;
                this.Step(board);
            }
            if (this.direction == Board.Direction.LEFT && this.oneX > this.tempEndCoordinateX) {
                this.backX -= this.pieceSize;
                this.Step(board);

            }
            if (this.direction == Board.Direction.UP && this.oneY > this.tempEndCoordinateY) {
                this.backY += this.pieceSize;
                this.Step(board);

            }
            if (this.direction == Board.Direction.DOWN && this.oneY > this.tempEndCoordinateY) {
                this.backY -= this.pieceSize;
                this.Step(board);
            }

            if(this.bearing == Animation.options.HORISONTAL) {
                batch.draw(this.textureHorisontal, (int) this.backX, (int) this.backY, this.backWidth, this.backHeight);
            }
            if(this.bearing == Animation.options.VERTICAL) {
                batch.draw(this.textureVertical, (int) this.backX, (int) this.backY, this.backWidth, this.backHeight);
            }
            if(this.direction == Board.Direction.RIGHT || this.direction == Board.Direction.UP ) {
                batch.draw(this.rightPart, this.twoX, this.twoY, this.pieceSize, this.pieceSize);
                batch.draw(this.leftPart, this.oneX, this.oneY, this.pieceSize, this.pieceSize);
            }
            else {
                batch.draw(this.leftPart, this.oneX, this.oneY, this.pieceSize, this.pieceSize);
                batch.draw(this.rightPart, this.twoX, this.twoY, this.pieceSize, this.pieceSize);
            }
            if (this.bearing == Animation.options.HORISONTAL) {
                this.oneX += this.stepSize;
                this.twoX -= this.stepSize;
            }
            if (this.bearing == Animation.options.VERTICAL) {
                this.oneY += this.stepSize;
                this.twoY -= this.stepSize;
            }

        }

    }

}