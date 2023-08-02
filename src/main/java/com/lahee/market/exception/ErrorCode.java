package com.lahee.market.exception;

public enum ErrorCode {
    INVALID_REQUEST(400, "COMMON-1", "잘못된 요청 입니다."),

    COMMENT_NOT_FOUND_EXCEPTION(404, "", "찾을 수 없는 댓글입니다."),
    COMMENT_NOT_IN_ITEM_EXCEPTION(400, "", "댓글 번호와 아이템의 번호가 잘못 요청 되었습니다."),

    ITEM_NOT_FOUND_EXCEPTION(404, "", "찾을 수 없는 아이템 입니다."),
    ITEM_NOT_IN_USER_EXCEPTION(403, "", "제안 번호와 아이템의 유저가 잘못 요청 되었습니다,"),

    NEGOTIATION_NOT_FOUND_EXCEPTION(404, "", "찾을 수 없는 제안 입니다."),
    NEGOTIATION_INVALID_STATUS_EXCEPTION(400, "", "판매자가 제안을 수락하지 않은 상태 입니다."),

    NEGOTIATION_NOT_ITEM_EXCEPTION(400, "", "제안 번호와 아이템의 번호가 잘못 요청 되었습니다."),
    USER_NOT_FOUND_EXCEPTION(404, "", "찾을 수 없는 유저 입니다."),

    SECURITY_INVALID_USER(403, "", "시큐리티 컨택스 내의 유저가 데이터 베이스에 존재하지 않습니다."),
    INVALID_COMMENT_USER(403, "", "해당 코멘트를 작성한 유저가 아닙니다 "),
    INVALID_COMMENT_ITEM_USER(403, "", "해당 코멘트가 작성된 아이템의 작성자가 아닙니다 "),
    INVALID_NEGOTIATION_EXCEPTION(403, "", "이 제안을 작성한 제안자가 아닙니다."),


    INVALID_NEGOTIATION_USER_EXCEPTION(403, "", "해당 제안 아이템의 작성자가 아닙니다."),
    INVALID_NEGOTIATION_STATUS(403, "", "정의되지 않은 제안 상태입니다."),
    SECURITY_INVALID_USER_CONTEXT(400, "", "context에 정보가 없습니다."),
    INVALID_PASSWORD(404, "", "패스워드가 일치하지 않습니다."),
    PASSWORD_NOT_EQUAL(404, "", "패스워드, 패스워트 채크가 일치하지 않습니다."),
    ITEM_SOLD_OUT(400, "", "이미 판매 완료 되었습니다"),
    INVALID_USER(400, "", "구독 유저가 이상합니다."),
    DUPLICATE_CHAT_ROOM(400, "", "이미 채팅방이 존재합니다"),
    INVALID_CHAT_ROOM_USER(400, "", "해당방의 권한 있는 사용자가 아닙니다."),

    INVALID_CHAT_ROOM_ID(400, "", "유효하지 않은 채팅방 아이디 입니다.");

    int status;
    String code;
    String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
