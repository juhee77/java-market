package com.lahee.market.exception;

public class CommentNotFoundException extends Status404Exception {
    public CommentNotFoundException() {
        super("찾을 수 없는 댓글 입니다.");
    }
}