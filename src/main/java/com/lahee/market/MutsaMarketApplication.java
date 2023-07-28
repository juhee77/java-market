package com.lahee.market;

import com.lahee.market.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MutsaMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutsaMarketApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() { //등록자와 수정자를 처리해 주는 AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
