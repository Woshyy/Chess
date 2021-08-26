package se.liu.chrwa634.pieces;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * This class has the objective to create and return new pieces to put on the board.
 * This class have a map of different images to use for the pieces.
 */
public class PieceFactory
{
    private Map<PieceType, String> imageMap;

    public PieceFactory() {
        imageMap = createImageMap();
    }

    /**
     * This method retrieves a piece of a type and a color.
     * @param pieceType The type of the piece.
     * @param color The color of the piece.
     * @return The wanted piece.
     */
    public Piece getPiece(PieceType pieceType, Color color) {
        URL imageUrl = getImageURL(pieceType, color);
        switch(pieceType) {
            case PAWN:
                return new Pawn(color, imageUrl);
            case KNIGHT:
                return new Knight(color, imageUrl);
            case BISHOP:
                return new Bishop(color, imageUrl);
            case ROOK:
                return new Rook(color, imageUrl);
            case QUEEN:
                return new Queen(color, imageUrl);
            default:
                return new King(color, imageUrl);
        }
    }

    /**
     * This is a help method to create a url for the picture of the piece.
     * @param pieceType The type of the piece.
     * @param color The color of the piece.
     * @return An image url for the piece picture.
     */
    private URL getImageURL(PieceType pieceType, Color color) {
        String colorPath = color.equals(Color.WHITE) ? "w" : "b";
        String imagePath = "images" + File.separator + colorPath + getImageFile(pieceType);
        return ClassLoader.getSystemResource(imagePath);
    }

    /**
     * This method helps to construst the static global variable IMAGE_MAP.
     * @return An enummap for the IMAGE_MAP constant map.
     */
    private Map<PieceType, String> createImageMap() {
        Map<PieceType, String> imageMap = new EnumMap<>(PieceType.class);
        imageMap.put(PieceType.PAWN, "_pawn.png");
        imageMap.put(PieceType.BISHOP, "_bishop.png");
        imageMap.put(PieceType.KING, "_king.png");
        imageMap.put(PieceType.QUEEN, "_queen.png");
        imageMap.put(PieceType.ROOK, "_rook.png");
        imageMap.put(PieceType.KNIGHT, "_knight.png");
        return imageMap;
    }

    /**
     * This method retrieves the string url for a specific pieceType.
     * @param pieceType The wanted pieceType string.
     * @return A string that is a part of the url for the picture.
     */
    private String getImageFile(PieceType pieceType) {
        return imageMap.get(pieceType);
    }
}
