ITEM 문서
=======

* [아이템 업로드](#-------)
    + [Request](#request)
    + [Response](#response)
* [아이템 단건 조회](#---------)
    + [Request](#request-1)
    + [Response](#response-1)
* [아이템 페이징 조회](#----------)
    + [Request](#request-2)
    + [Response](#response-2)
* [아이템 수정](#------)
    + [Request](#request-3)
    + [Response](#response-3)
* [아이템 삭제](#------)
    + [Request](#request-4)
    + [Response](#response-4)
* [아이템 사진 업데이트](#-----------)
  + [Request](#request-5)
  + [Response](#response-5)

-------

### Request

    POST /items HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 190
    Host: localhost:8080
    
    {
      "title" : "중고 맥북 팝니다",
      "description" : "2019년 맥북 프로 13인치 모델입니다",
      "minPriceWanted" : 10000,
      "writer" : "jeeho.dev",
      "password" : "1qaz2wsx"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 53
    
    {
      "message" : "등록이 완료 되었습니다."
    }


아이템 단건 조회
---------

### Request

    GET /items/15 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 171
    
    {
      "id" : 15,
      "title" : "중고 맥북 팝니다",
      "description" : "2019년 맥북 프로 13인치 모델입니다",
      "minPriceWanted" : 10000,
      "status" : "SELL"
    }


아이템 페이징 조회
----------

### Request

    GET /items?page=2&limit=10 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 1128
    
    {
      "content" : [ {
        "id" : 37,
        "title" : "title20",
        "description" : "desc20",
        "minPriceWanted" : 200000,
        "status" : "SELL"
      }, {
        "id" : 38,
        "title" : "title21",
        "description" : "desc21",
        "minPriceWanted" : 210000,
        "status" : "SELL"
      }, {
        "id" : 39,
        "title" : "title22",
        "description" : "desc22",
        "minPriceWanted" : 220000,
        "status" : "SELL"
      }, {
        "id" : 40,
        "title" : "title23",
        "description" : "desc23",
        "minPriceWanted" : 230000,
        "status" : "SELL"
      }, {
        "id" : 41,
        "title" : "title24",
        "description" : "desc24",
        "minPriceWanted" : 240000,
        "status" : "SELL"
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


아이템 수정
------

### Request

    PUT /items/16 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 133
    Host: localhost:8080
    
    {
      "title" : "MODIFY",
      "description" : "MODIFY",
      "minPriceWanted" : 10000,
      "writer" : "jeeho.dev",
      "password" : "1qaz2wsx"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 52
    
    {
      "message" : "물품이 수정되었습니다."
    }


아이템 삭제
------

### Request

    DELETE /items/42 HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 55
    Host: localhost:8080
    
    {
      "writer" : "jeeho.dev",
      "password" : "1qaz2wsx"
    }

### Response

    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 50
    
    {
      "message" : "물품을 삭제 했습니다."
    }


아이템 사진 업데이트
-----------
### Request

```http request
PUT /items/1/image HTTP/1.1
Host: localhost:8080
Content-Length: 393
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="image"; filename="free-icon-lion-of-judah-3436890.png"
Content-Type: image/png

(data)
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="writer"

qwer
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="password"

qwer
------WebKitFormBoundary7MA4YWxkTrZu0gW--

```

### Response

```http request
HTTP/1.1 200 OK

{
    "message":"이미지가 등록되었습니다."
}
```

