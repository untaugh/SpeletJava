package ettjavaspel.speletjava;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class Animation
{
    boolean active;
    public options bearing;
    public float currentCoordinateX;
    public float currentCoordinateY;
    public int currentPositionX;
    public int currentPositionY;
    public Board.Direction direction;
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
    Sound animationSound1;
    Sound animationSound2;

    public Animation() {
        this.active = false;
        this.currentPositionY = 0;
        this.currentPositionX = 0;

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
        switch (direction) {
            case UP: {
                n3 = 1;
                this.positionStepSizeY = 1;
                this.bearing = Animation.options.VERTICAL;
                break;
            }
            case DOWN: {
                n3 = -1;
                this.positionStepSizeY = -1;
                this.bearing = Animation.options.VERTICAL;
                break;
            }
            case LEFT: {
                n2 = -1;
                this.positionStepSizeX = -1;
                this.bearing = Animation.options.HORISONTAL;
                break;
            }
            case RIGHT: {
                n2 = 1;
                this.positionStepSizeX = 1;
                this.bearing = Animation.options.HORISONTAL;
                break;
            }
        }
        this.active = true;
        this.stepSize = this.pieceSize / 4f;
        this.direction = direction;
        this.currentPositionX = currentPositionX;
        this.currentPositionY = currentPositionY;
        this.leftPart = this.rightPart = ScreenUtils.getFrameBufferTexture((int) (this.currentPositionX * pieceSize), (int) (this.currentPositionY * pieceSize), (int) this.pieceSize, (int) this.pieceSize);

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
            //for (n2 = n5; n2 >= 0 && n2 < board.cols && n >= 0 && n < board.rows; n2 += n3, n += n4) {
                //if (board.pieces[n2][n].piececolor == piececolor) {
                    //return true;
                //}
            //}
            if(board.pieces[n5][n].piececolor != piececolor) {
                return true;
            }
        }
        return false;
    }

    public boolean Step(Board board) {

        final int n = this.currentPositionX;
        final int n2 = this.currentPositionY;
        animationSound1.play(0.5f);

        board.pieces[n][n2].moving = false;


        if (n2 >= 0 && n2 < board.rows && n >= 0 && n < board.cols) {
            int n3 = n2;
            int n4 = n;
            switch (this.direction) {
                case UP: {
                    if(n2 != 0) {
                        board.pieces[n][n2 - 1].moving = false;
                    }
                    if (n2 == board.rows - 1|| board.pieces[n][n2].piececolor == board.pieces[n][n2 + 1].piececolor) {
                        this.active = false;
                        animationSound2.play(0.2f);
                        return false;
                    }
                    n3 = n2 + 1;
                    this.currentCoordinateX = this.oneX = this.twoX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.oneY = this.currentPositionY * this.pieceSize;
                    this.twoY = this.oneY + pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX ;
                    this.tempEndCoordinateY = this.currentCoordinateY + this.pieceSize;
                    break;
                }
                case DOWN: {
                    if(n2 != board.rows - 1) {
                        board.pieces[n][n2 + 1].moving = false;
                    }
                    if (n2 == 0 || board.pieces[n][n2].piececolor == board.pieces[n][n2 - 1].piececolor) {
                        this.active = false;
                        animationSound2.play(0.2f);
                        return false;
                    }
                    n3 = n2 - 1;
                    this.currentCoordinateX = this.oneX = this.twoX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.twoY = this.currentPositionY * this.pieceSize;
                    this.oneY = this.twoY - pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX;
                    this.tempEndCoordinateY = this.currentCoordinateY;
                    break;
                }
                case LEFT: {
                    if(n != board.cols - 1) {
                        board.pieces[n + 1][n2].moving = false;
                    }
                    if (n == 0 || board.pieces[n][n2].piececolor == board.pieces[n - 1][n2].piececolor) {
                        this.active = false;
                        animationSound2.play(0.2f);
                        return false;
                    }
                    n4 = n - 1;
                    this.currentCoordinateX = this.twoX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.twoY = this.oneY = this.currentPositionY * this.pieceSize;
                    this.oneX = this.twoX - pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX;
                    this.tempEndCoordinateY = this.currentCoordinateY;
                    break;
                }
                case RIGHT: {
                    if(n != 0) {
                        board.pieces[n - 1][n2].moving = false;
                    }
                    if (n == board.cols - 1|| board.pieces[n][n2].piececolor == board.pieces[n + 1][n2].piececolor) {
                        this.active = false;
                        animationSound2.play(0.2f);
                        return false;
                    }
                    n4 = n + 1;
                    this.currentCoordinateX = this.oneX = this.currentPositionX * this.pieceSize;
                    this.currentCoordinateY = this.oneY = this.twoY = this.currentPositionY * this.pieceSize;
                    this.twoX = this.oneX + this.pieceSize;
                    this.tempEndCoordinateX = this.currentCoordinateX + this.pieceSize;
                    this.tempEndCoordinateY = this.currentCoordinateY;
                    break;
                }
            }

            board.pieces[n][n2].moving = true;
            board.pieces[n4][n3].moving = true;

            if (n3 >= 0 && n3 < board.rows && n4 >= 0 && n4 < board.cols) {

                if(this.direction == Board.Direction.DOWN || this.direction == Board.Direction.LEFT) {
                    this.leftPart = ScreenUtils.getFrameBufferTexture((int) this.oneX, (int) this.oneY, (int) this.pieceSize, (int) this.pieceSize);
                }
                else {
                    this.rightPart = ScreenUtils.getFrameBufferTexture((int) (this.twoX), (int) this.twoY, (int) this.pieceSize, (int) this.pieceSize);
                }

                final Piece piece = board.pieces[this.currentPositionX][this.currentPositionY];
                board.pieces[this.currentPositionX][this.currentPositionY] = board.pieces[this.currentPositionX + this.positionStepSizeX][this.currentPositionY + this.positionStepSizeY];
                board.pieces[this.currentPositionX + this.positionStepSizeX][this.currentPositionY + this.positionStepSizeY] = piece;
                this.currentPositionX += this.positionStepSizeX;
                this.currentPositionY += this.positionStepSizeY;

                return true;
            }
        }
        return false;
    }

    public void render(SpriteBatch batch, Board board) {

        if (this.active) {

            if(this.direction == Board.Direction.UP || this.direction == Board.Direction.RIGHT) {
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

            if (this.direction == Board.Direction.RIGHT && this.oneX > this.tempEndCoordinateX) {
                this.Step(board);
            }
            if (this.direction == Board.Direction.LEFT && this.oneX > this.tempEndCoordinateX) {
                this.Step(board);

            }
            if (this.direction == Board.Direction.UP && this.oneY > this.tempEndCoordinateY) {
                this.Step(board);

            }
            if (this.direction == Board.Direction.DOWN && this.oneY > this.tempEndCoordinateY) {
                this.Step(board);
            }

        }

    }

}