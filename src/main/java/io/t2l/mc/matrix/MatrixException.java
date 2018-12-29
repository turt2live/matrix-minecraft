package io.t2l.mc.matrix;

public class MatrixException extends Exception {

    public final int statusCode;
    public final String errcode;

    public MatrixException(String message, String errcode, int statusCode) {
        super(errcode + ":" + message);
        this.statusCode = statusCode;
        this.errcode = errcode;
    }

    public MatrixException(String message, String errcode, int statusCode, Exception inner) {
        super(errcode + ":" + message, inner);
        this.statusCode = statusCode;
        this.errcode = errcode;
    }
}
