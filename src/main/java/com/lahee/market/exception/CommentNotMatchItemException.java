package com.lahee.market.exception;

public class CommentNotMatchItemException extends Status400Exception {
    public CommentNotMatchItemException() {
        super("댓글 번호와 아이템의 번호가 잘못 요청 되었습니다.");
    }

    public CommentNotMatchItemException(String message) {
        super(message);
    }
}