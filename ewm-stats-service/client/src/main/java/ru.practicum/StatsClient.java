package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StatsClient {
/*
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
*/

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        //super(new RestTemplate());
        //super();

    }
/*
    public ResponseEntity<Object> create(StatsHitDto dto) {
        return post("/hit", dto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.toString(),
                "end", end.toString(),
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }*/
}
