
COMMENT 문서
==========

* [댓글 업로드](#------)
    + [Request](#request)
    + [Response](#response)
* [댓글 단건 조회](#--------)
    + [Request](#request-1)
    + [Response](#response-1)
* [댓글 페이징 조회](#---------)
    + [Request](#request-2)
    + [Response](#response-2)
* [댓글 수정](#-----)
    + [Request](#request-3)
    + [Response](#response-3)
* [댓글의 댓글 업데이트](#-----------)
    + [Request](#request-4)
    + [Response](#response-4)
* [댓글 삭제](#-----)
    + [Request](#request-5)
    + [Response](#response-5)


댓글 업로드
------

### Request

    POST /items/1/comments HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 101
    Host: localhost:8080
    
    {
      "writer" : "jeeho.edu",
      "password" : "qwerty1234",
      "content" : "할인 가능하신가요?"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 52
    
    {
      "message" : "댓글이 등록되었습니다."
    }


댓글 단건 조회
--------

### Request

    GET /items/5/comments/29 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 77
    
    {
      "id" : 29,
      "content" : "할인 가능하신가요?",
      "reply" : null
    }


댓글 페이징 조회
---------

### Request

    GET /items/2/comments?page=2&limit=10 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 833
    
    {
      "content" : [ {
        "id" : 22,
        "content" : "cContent20",
        "reply" : null
      }, {
        "id" : 23,
        "content" : "cContent21",
        "reply" : null
      }, {
        "id" : 24,
        "content" : "cContent22",
        "reply" : null
      }, {
        "id" : 25,
        "content" : "cContent23",
        "reply" : null
      }, {
        "id" : 26,
        "content" : "cContent24",
        "reply" : null
      } ],
      "pageable" : {
        "sort" : {
          "empty" : false,
          "sorted" : true,
          "unsorted" : false
        },
        "offset" : 20,
        "pageNumber" : 2,
        "pageSize" : 10,
        "paged" : true,
        "unpaged" : false
      },
      "totalPages" : 3,
      "totalElements" : 25,
      "last" : true,
      "size" : 10,
      "number" : 2,
      "sort" : {
        "empty" : false,
        "sorted" : true,
        "unsorted" : false
      },
      "numberOfElements" : 5,
      "first" : false,
      "empty" : false
    }


댓글 수정
-----

### Request

    PUT /items/3/comments/27 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 81
    Host: localhost:8080
    
    {
      "writer" : "jeeho.edu",
      "password" : "qwerty1234",
      "content" : "MODIFY"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 52
    
    {
      "message" : "댓글이 수정되었습니다."
    }


댓글의 댓글 업데이트
-----------

### Request

    PUT /items/4/comments/28/reply HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 76
    Host: localhost:8080
    
    {
      "writer" : "jeeho.dev",
      "password" : "1qaz2wsx",
      "reply" : "REPLY"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 62
    
    {
      "message" : "댓글에 답변이 추가되었습니다."
    }

댓글 삭제
-----

### Request

    DELETE /items/6/comments/30 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 57
    Host: localhost:8080
    
    {
      "writer" : "jeeho.edu",
      "password" : "qwerty1234"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 49
    
    {
      "message" : "댓글을 삭제했습니다."
    }
