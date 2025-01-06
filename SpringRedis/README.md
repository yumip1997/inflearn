### Redis 기본 개념

---

1. Redis란?
    
    다양한 구조의 데이터를 저장하는 인메모리 데이터 저장소입니다.
    
2. Redis의 장점
    
    레디스는 인메모리에 모든 데이터를 저장하기에, 데이터 처리 성능이 굉장히 빠르다.
    

### Redis 사용법

---

1. 기본 명령어
- 데이터 저장하기 : **set [key 이름] [value]**
    
    ```bash
    $ set yumi:name "yumi park"
    $ set yumi:hobby coding
    ```
    
    띄어 쓰기가 포함되어 있다면 쌍따옴표로 묶어주면 됩니다.
    
    만약 이미 존재하는 key로 value를 저장한다면 기존의 값이 덮어씌어집니다.
    
- 데이터 조회하기 : **get [key 이름]**
    
    ```bash
    $ get yumi:name
    ```
    
    존재하지 않는 key로 조회 시 (nil) 반환
    
- 저장된 모든 key 조회하기
    
    ```bash
    $ keys *
    ```
    

- 데이터 삭제하기 : **del [key 이름]**
    
    ```bash
    $ del yumi:name
    ```
    
    데이터 삭제가 완료되면 (integer) 1 이 반환됩니다.
    
    존재하지 않은 데이터 삭제 시 (integer) 0 이 반환됩니다.
    
- 데이터 저장 시 만료시간(TTL, Time To Live) 정하기 : **set [key 이름] [value] ex [만료 시간(초)]**
    
    ```bash
     $ set yumi:name "yumi park" ex 3
    ```
    
- 만료시간 확인하기 : ttl [key 이름]
    
    ```bash
    $ ttl yumi:name
    ```
    
    key가 없는 경우 (integer) -2가 반환되고, key는 있찌만 만료시간이 설정되어 있찌 않은 경우 (integer) -1이 반환됩니다.
    
- 모든 데이터 삭제하기
    
    ```bash
    $ flushall
    ```
    

2. 네이밍 컨벤션

    콜론(:)을 사용해서 계층적으로 의미를 구분한다.

### Redis 캐싱 전략

---

1. 캐시, 캐싱이란?
- 캐시란?
    
    원본 저장소보다 데이터를 빠르게 가져올 수 있는 임시 데이터 저장소
    
- 캐싱이란?
    
    캐시에 접근해서 데이터를 빠르게 가져오는 방식
    
2. 데이터를 캐싱할 때 사용하는 전략
- Cache Aside 전략
    
    캐시에서 먼저 데이터를 확인하고 없다면 DB를 통해 조회해오는 방식입니다. 
    
    데이터가 있다면(Cache Hit) 그대로 반환하고 없다면(Cache Miss) DB를 통해 조회 후 케시에 다시 데이터를 저장합니다.
    
- Write Around 전략
    
    쓰기 작업은 캐시에 반영하지 않고, DB에만 반영하는 전략입니다.
    
3. Cache Aside + Write Around 전략의 한계점 및 해결방안
- 데이터의 일관성 보장할 수 없습니다.
    
    Write Around 전략의 경우 쓰기 작업은 DB에만 반영하고 캐시에 반영하지 않기에 캐시된 데이터와 DB 데이터가 일치하지 않을 수 있습니다.
    
    해결방안 : 주기적으로 동기화가 필요하기에 TTL 기능을 활용
    
- 저장공간이 비교적 작습니다.
    
    DB 보다는 캐시의 저장공간은 작습니다.
    
    해결방안 : TTL을 활용해 일정 시간이 지나면 데이터를 삭제
    

### Spring Boot + Redis

---

- @Cacheable 어노테이션 추가
    
    ```java
    @Cacheable(cacheNames = "getBoards", key = "'boards:page:' + #page + ':size:' + #size", cacheManager = "boardCacheManager")
    public List<Board> getBoards(int page, int size) {
    		Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> pageOfBoards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return pageOfBoards.getContent();
    }
    ```
    
    - `cacheNames` : 캐시 이름
    - `key` : Redis에 저장할 key 이름
    - `cacheManager`  : 캐시를 관리하는 주체 → 캐시의 생성/조회/삭제 처리

- 성능 향상 (로컬 환경에서 단순 테스트)
    
    캐시 적용 전 : 500ms
    
    캐시 적용 후 : 5ms
