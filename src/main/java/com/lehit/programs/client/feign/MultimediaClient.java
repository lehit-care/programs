package com.lehit.programs.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${multimedia.url}", value = "${multimedia.url}")
@Service
@Profile("!test")
public interface MultimediaClient {
    @DeleteMapping(value = "${multimedia.bulk-url}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteMultimediaList(@RequestParam List<String> urls);

    @DeleteMapping(value = "${multimedia.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteSingleMultimedia(@RequestParam String url);
}