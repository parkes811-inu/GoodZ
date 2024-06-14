## Spring Batch 작업 흐름
1. Scheduler: 특정 시간에 배치 작업을 트리거함.
2. JobLauncher: 배치 작업 실행.
3. Job: 배치 작업 정의.
4. Step: 작업 단계 정의.
    - Tasklet: 단일 작업 수행.
    - Chunk-Oriented Step:
        - ItemReader: 데이터 읽기.
        - ItemProcessor: 데이터 처리.
        - ItemWriter: 데이터 쓰기.
5. Repository: 작업 실행 정보 저장.

### Spring Batch 용어
1. Scheduler: 스케줄러가 정해진 시간에 배치 작업을 시작합니다.
2. JobLauncher: 스케줄러에 의해 호출되어 배치 작업을 시작하는 역할을 합니다.
3. Job: 배치 작업의 전체 실행 단위를 나타냅니다.
4. Step: 하나의 배치 작업 안에서 실행되는 개별 단계로, 여러 단계로 구성될 수 있습니다.
5. Tasklet: 배치 작업의 단위 작업을 정의하며, 주로 단일 작업을 수행하는 데 사용됩니다.
6. ItemReader: 데이터 소스를 읽어오는 역할을 합니다.
7. ItemProcessor: 읽어온 데이터를 처리하는 역할을 합니다.
8. ItemWriter: 처리된 데이터를 대상 시스템에 쓰는 역할을 합니다.
9. Repository: 작업 실행 정보를 저장하고 관리합니다.

### Goodz 프로젝트 스프링 배치 작업 순서
1. Scheduler :
    - BatchScheduler.java 에서 설정된 스케줄링 어노테이션에 의해 @Scheduled(cron = "0 0 0 * * ?") runBatchJob() 메서드 호출 
    - runBatchJob() 메서드 호출되면 JobLauncher 를 사용하여 updateProductPricesJob 실행
    -> JobLauncher, updateProductPricesJob는 BatchScheduler 클래스의 생성자를 통해 의존성 주입을 받아서 사용된다.

2. JobLauncher, Job :
    - 스케줄러에 의해 호출된 ProductPriceJobConfig.java의 JobLauncher.
    - updateProductPricesJob() 메서드를 통해 Job 객체를 생성.
    - Job은 하나 이상의 Step으로 구성되기 때문에 updateProductPricesStep() 메서드를 호출하여 Reader, Processor, Writer로 구성된 작업 단위인 Step을 구성하고, Job 생성을 완료.

3. Step : 
    - step을 정의 하기 위해 각각의 기능을 처리하는 개별 단계로 각각 작업을 한다.
        - ItemReader: 데이터 읽기 -> 상품 목록 조회.
        - ItemProcessor: 데이터 처리 -> 조회수 확인 후 3이상 제품 확인 .
        - ItemWriter: 데이터 쓰기 -> 3이상 제품 가격 증가/ 0인 제품 가격 감소.
    - tasklet과 chunk 방식 2개 중 하나로 step을 구성
    - tasklet은 단일 작업 수행에 유리, chunk 방식은 대용량 처리에 유리

4. Repository (=Writer) : 
    - Step에서 차례대로 reader, processor를 통해 가공된 데이터들을 writer 단계에서 
    - BatchProductMapper를 통해 데이터베이스에 저장.