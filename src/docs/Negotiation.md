
Negotiation 문서
==============

* [Negotiation 업로드](#negotiation----)
    + [Request](#request)
    + [Response](#response)
* [Negotiation 페이징 조회(case : 판매자)](#negotiation--------case-------)
    + [Request](#request-1)
    + [Response](#response-1)
* [Negotiation 페이징 조회(case : 제안 작성자)](#negotiation--------case----------)
    + [Request](#request-2)
    + [Response](#response-2)
* [Negotiation 수정](#negotiation---)
    + [Request](#request-3)
    + [Response](#response-3)
* [Negotiation 삭제](#negotiation---)
    + [Request](#request-4)
    + [Response](#response-4)
* [Negotiation 상태 수정 case : 판매자가 수락하는 경우](#negotiation-------case---------------)
    + [Request](#request-5)
    + [Response](#response-5)
* [Negotiation 상태 수정 : 제안자가 확정하는 경우](#negotiation---------------------)
    + [Request](#request-6)
    + [Response](#response-6)


Negotiation 업로드
---------------

### Request

    POST /items/8/proposal HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 86
    Host: localhost:8080
    
    {
      "writer" : "jeeho.edu",
      "password" : "qwerty1234",
      "suggestedPrice" : 100000
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 59
    
    {
      "message" : "구매 제안이 등록되었습니다."
    }


Negotiation 페이징 조회(case : 판매자)
------------------------------

### Request

    GET /items/9/proposal?writer=jeeho.dev&password=1qaz2wsx&page=0 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 1228
    
    {
      "content" : [ {
        "id" : 3,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 4,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 5,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 6,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 7,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 8,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 9,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 10,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 11,
        "suggestedPrice" : 10000,
        "status" : "제안"
      }, {
        "id" : 12,
        "suggestedPrice" : 10000,
        "status" : "제안"
      } ],
      "pageable" : {
        "sort" : {
          "empty" : true,
          "sorted" : false,
          "unsorted" : true
        },
        "offset" : 0,
        "pageNumber" : 0,
        "pageSize" : 20,
        "paged" : true,
        "unpaged" : false
      },
      "totalPages" : 1,
      "totalElements" : 10,
      "last" : true,
      "size" : 20,
      "number" : 0,
      "sort" : {
        "empty" : true,
        "sorted" : false,
        "unsorted" : true
      },
      "numberOfElements" : 10,
      "first" : true,
      "empty" : false
    }

Negotiation 페이징 조회(case : 제안 작성자)
---------------------------------

### Request

    GET /items/10/proposal?writer=pWriter&password=pPassword&page=0 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 845
    
    {
      "content" : [ {
        "id" : 13,
        "suggestedPrice" : 0,
        "status" : "제안"
      }, {
        "id" : 14,
        "suggestedPrice" : 1000,
        "status" : "제안"
      }, {
        "id" : 15,
        "suggestedPrice" : 2000,
        "status" : "제안"
      }, {
        "id" : 16,
        "suggestedPrice" : 3000,
        "status" : "제안"
      }, {
        "id" : 17,
        "suggestedPrice" : 4000,
        "status" : "제안"
      } ],
      "pageable" : {
        "sort" : {
          "empty" : true,
          "sorted" : false,
          "unsorted" : true
        },
        "offset" : 0,
        "pageNumber" : 0,
        "pageSize" : 20,
        "paged" : true,
        "unpaged" : false
      },
      "totalPages" : 1,
      "totalElements" : 5,
      "last" : true,
      "size" : 20,
      "number" : 0,
      "sort" : {
        "empty" : true,
        "sorted" : false,
        "unsorted" : true
      },
      "numberOfElements" : 5,
      "first" : true,
      "empty" : false
    }


Negotiation 수정
--------------

### Request

    PUT /items/11/proposal/23 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 88
    Host: localhost:8080
    
    {
      "suggestedPrice" : "500000",
      "password" : "qwerty1234",
      "writer" : "jeeho.edu"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 52
    
    {
      "message" : "제안이 수정되었습니다."
    }

Negotiation 삭제
--------------

### Request

    DELETE /items/12/proposal/24 HTTP/1.1
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
      "message" : "제안을 삭제했습니다."
    }


Negotiation 상태 수정 case : 판매자가 수락하는 경우
-------------------------------------

### Request

    PUT /items/7/proposal/1 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 78
    Host: localhost:8080
    
    {
      "password" : "1qaz2wsx",
      "writer" : "jeeho.dev",
      "status" : "수락"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 62
    
    {
      "message" : "제안의 상태가 변경되었습니다."
    }


Negotiation 상태 수정 : 제안자가 확정하는 경우
--------------------------------

### Request

    PUT /items/13/proposal/25 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 80
    Host: localhost:8080
    
    {
      "password" : "qwerty1234",
      "writer" : "jeeho.edu",
      "status" : "확정"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 52
    
    {
      "message" : "구매가 확정되었습니다."
    }
